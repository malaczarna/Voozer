package pl.jarosyjarosy.yougetin.destination.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.DestinationMessage;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointMapperService;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;

@Component
public class DestinationMapperService {

    private final RoutePointMapperService routePointMapperService;
    private final RoutePointService routePointService;

    @Autowired
    DestinationMapperService(RoutePointMapperService routePointMapperService,
                             RoutePointService routePointService) {

        this.routePointMapperService = routePointMapperService;
        this.routePointService = routePointService;
    }

    public DestinationMessage mapDestination(Destination destination) {
        DestinationMessage destinationMessage = new DestinationMessage();
        BeanUtils.copyProperties(destination, destinationMessage);

        destinationMessage.setRoute(routePointMapperService.mapRoute(routePointService.getRoute(destination.getId())));

        return destinationMessage;
    }

    public Destination mapDestinationMessage(DestinationMessage destinationMessage) {
        Destination destination = new Destination();
        BeanUtils.copyProperties(destinationMessage, destination);

        return destination;
    }
}
