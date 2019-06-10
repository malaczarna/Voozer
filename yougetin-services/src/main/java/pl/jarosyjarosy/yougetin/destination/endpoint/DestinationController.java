package pl.jarosyjarosy.yougetin.destination.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.DestinationMessage;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationMapperService;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private DestinationService destinationService;
    private DestinationMapperService destinationMapperService;

    @Autowired
    public DestinationController(DestinationService destinationService,
                                 DestinationMapperService destinationMapperService) {

        this.destinationService = destinationService;
        this.destinationMapperService = destinationMapperService;
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
}
