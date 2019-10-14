package pl.jarosyjarosy.yougetin.destination.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.DestinationMessage;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationMapperService;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointMapperService;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private DestinationService destinationService;
    private DestinationMapperService destinationMapperService;
    private final RoutePointMapperService routePointMapperService;
    private final RoutePointService routePointService;

    @Autowired
    public DestinationController(DestinationService destinationService,
                                 DestinationMapperService destinationMapperService,
                                 RoutePointMapperService routePointMapperService,
                                 RoutePointService routePointService) {

        this.destinationService = destinationService;
        this.destinationMapperService = destinationMapperService;
        this.routePointMapperService = routePointMapperService;
        this.routePointService = routePointService;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public DestinationMessage get(@PathVariable Long id, HttpServletRequest request) {

        return destinationMapperService.mapDestination(destinationService.get(id));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public DestinationMessage save(@RequestBody DestinationMessage destinationMessage, HttpServletRequest request) {

        Destination newDestination = destinationService.validateAndCreate(destinationMapperService.mapDestinationMessage(destinationMessage), new Identity(request).getUserId());

        return destinationMapperService.mapDestination(newDestination);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<DestinationMessage> getForUser(HttpServletRequest request) {

        return destinationService.getByUserId(new Identity(request).getUserId()).stream()
                .map(destinationMapperService::mapDestination)
                .collect(Collectors.toList());
    }

    @RequestMapping(
            value = "/{id}/route",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<Position> getRoute(@PathVariable Long id, HttpServletRequest request) {

        return routePointMapperService.mapRoute(routePointService.getRoute(id));
    }

    @RequestMapping(
            value = "/{id}/route",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public List<Position> saveRoute(@PathVariable Long id, @RequestBody List<Position> positions, HttpServletRequest request) {

        return routePointMapperService.mapRoute(routePointService.saveRoute(routePointMapperService.mapRouteMesage(positions, id)));

    }

}
