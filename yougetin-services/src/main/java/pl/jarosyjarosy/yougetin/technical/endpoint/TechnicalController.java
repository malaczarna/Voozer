package pl.jarosyjarosy.yougetin.technical.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.jarosyjarosy.yougetin.stops.service.StopService;

@RestController
public class TechnicalController implements ErrorController {

    @RequestMapping(
            value = "/error",
            method = RequestMethod.GET
    )
    public String error() {
        return "Wygląda na to, że wystąpił błąd lub nie potrafimy zweryfikować twojej tożsamości. " +
                "Zaloguj się ponownie i wykonaj akcję jeszcze raz.";
    }

    @RequestMapping(
            value = "/health/ping",
            method = RequestMethod.GET
    )
    public String ping() {
        return "pong";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
