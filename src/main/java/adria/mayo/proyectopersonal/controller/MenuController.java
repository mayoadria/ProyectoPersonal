package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.security.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MenuController {

    @GetMapping("/")
    public String index(Model model) {
        UserUtils.getUsuariDetalls(model);
        return "home"; // Devuelve la vista "index"
    }
}
