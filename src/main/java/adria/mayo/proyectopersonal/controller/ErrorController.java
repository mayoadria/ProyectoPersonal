package adria.mayo.proyectopersonal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "accesoDenegado";  // nombre de la vista
    }
}
