package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.service.UsuariService;
import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuariService usuariService;

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

}
