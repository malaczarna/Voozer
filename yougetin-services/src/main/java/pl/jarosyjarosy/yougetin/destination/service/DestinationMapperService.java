package pl.jarosyjarosy.yougetin.destination.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.DestinationMessage;
import pl.jarosyjarosy.yougetin.destination.model.Destination;

@Component
public class DestinationMapperService {

    public DestinationMessage mapDestination(Destination destination) {
        DestinationMessage destinationMessage = new DestinationMessage();
        BeanUtils.copyProperties(destination, destinationMessage);

        return destinationMessage;
    }

    public Destination mapDestinationMessage(DestinationMessage destinationMessage) {
        Destination destination = new Destination();
        BeanUtils.copyProperties(destinationMessage, destination);

        return destination;
    }
}
