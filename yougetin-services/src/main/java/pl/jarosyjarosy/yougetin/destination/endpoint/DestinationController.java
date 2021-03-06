package pl.jarosyjarosy.yougetin.destination.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.DestinationMessage;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.PositionWithTime;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationMapperService;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointMapperService;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserService;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private DestinationService destinationService;
    private DestinationMapperService destinationMapperService;
    private UserService userService;
    private final RoutePointMapperService routePointMapperService;
    private final RoutePointService routePointService;

    @Autowired
    public DestinationController(DestinationService destinationService,
                                 DestinationMapperService destinationMapperService,
                                 UserService userService,
                                 RoutePointMapperService routePointMapperService,
                                 RoutePointService routePointService) {

        this.destinationService = destinationService;
        this.destinationMapperService = destinationMapperService;
        this.userService = userService;
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

        User user = userService.get(new Identity(request).getUserId());

        Destination newDestination = destinationService.validateAndCreate(destinationMapperService.mapDestinationMessage(destinationMessage), user.getId());

        routePointService.saveRoute(routePointMapperService.mapRouteMesage(destinationMessage.getRoute(), newDestination.getId()), user.getDestinationId());

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
    public List<PositionWithTime> getRoute(@PathVariable Long id, HttpServletRequest request) {

        return routePointMapperService.mapRoute(routePointService.getRoute(id));
    }
}
