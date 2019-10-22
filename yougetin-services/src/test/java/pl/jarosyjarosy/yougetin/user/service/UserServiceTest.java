package pl.jarosyjarosy.yougetin.user.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getDriversInRadiusInKm() throws FactoryException, TransformException {
        User passenger = new User();
        passenger.setId(10L);
        passenger.setLat(52.401810);
        passenger.setLng(16.876383);

        User driver1 = new User();
        driver1.setId(20L);
        driver1.setLat(52.395817);
        driver1.setLng(16.950125);

        User driver2 = new User();
        driver2.setId(30L);
        driver2.setLat(52.407862);
        driver2.setLng(16.90188);

        when(userRepository.findActiveDrivers()).thenReturn(Arrays.asList(driver1, driver2));
        when(userRepository.findById(10L)).thenReturn(Optional.of(passenger));

        assertThat(userService.getDriversInRadiusInMeters(10L, 3000D)).isEqualTo(Collections.singletonList(driver2));

    }
}