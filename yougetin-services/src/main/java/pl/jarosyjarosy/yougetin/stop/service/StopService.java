package pl.jarosyjarosy.yougetin.stop.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.jarosyjarosy.yougetin.stop.model.Feature;
import pl.jarosyjarosy.yougetin.stop.model.FeatureList;
import pl.jarosyjarosy.yougetin.stop.model.Stop;
import pl.jarosyjarosy.yougetin.stop.repository.StopRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
@Component
public class StopService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopService.class);

    private StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    @Scheduled(cron = "0 0 4 * * wed", zone="Europe/Warsaw")
    public void getBusStops() {
        LOGGER.info("LOGGER: get bus stop from Poznań API");
        final String uri = "http://www.poznan.pl/mim/plan/map_service.html?mtype=pub_transport&co=cluster";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        Gson gson = new Gson();
        FeatureList featureList = gson.fromJson(result, FeatureList.class);

        List<Feature> features = featureList.getFeatures();

        if (!features.isEmpty()) {
            saveAndMapStopsFromBusStopsFeatures(features);
        }
    }

    private void saveAndMapStopsFromBusStopsFeatures(List<Feature> busStops) {
        LOGGER.info("LOGGER: saving new bus stop from Poznań API started");
        this.stopRepository.deleteAll();
        List<Stop> stops = new ArrayList<>();
        for (Feature busStop : busStops) {
            if (busStop.getProperties().getRouteType().equals("3")) {
                Stop newStop = new Stop();
                newStop.setLat(busStop.getGeometry().getCoordinates().get(1));
                newStop.setLng(busStop.getGeometry().getCoordinates().get(0));
                newStop.setName(busStop.getProperties().getStopName());
                stops.add(newStop);
                LOGGER.info("LOGGER: mapped bus stop {} ({}, {})", newStop.getName(), newStop.getLat(), newStop.getLng());
            }
        }
        stopRepository.saveAll(stops);
        LOGGER.info("LOGGER: saving new bus stop from Poznań API ended");
    }


}
