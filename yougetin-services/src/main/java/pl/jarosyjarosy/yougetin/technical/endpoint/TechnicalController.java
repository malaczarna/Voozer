package pl.jarosyjarosy.yougetin.technical.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TechnicalController implements ErrorController {

    private String host;

    @Autowired
    public TechnicalController(@Value("${host}") String host) {
        this.host = host;
    }

    @RequestMapping(
            value = "/error",
            method = RequestMethod.GET
    )
    public String error() {
        return "<html><head></head><body style=\"text-align: center;\"><div style=\"margin-top: 50px;line-height: 30px;\">Wygląda na to, że wystąpił błąd lub nie potrafimy zweryfikować twojej tożsamości. <br>" +
                "<a href=\"" + host + "/#/login?logout=1\">Zaloguj się</a> ponownie i wykonaj akcję jeszcze raz.</div></body></html>";
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
