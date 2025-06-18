package adria.mayo.proyectopersonal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalegController {

    @GetMapping("/cataleg")
    public String cataleg(){
        return "cataleg";
    }
}
