package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vehicle")
public class VehiclesController {

    @Autowired
    private VehicleService vehicleService;


    @GetMapping("/crear_vehicle")
    public String mostrarFormulariVehicle(Model model) {
        Vehiculo vehicle = new Vehiculo();
        model.addAttribute("vehicle", vehicle);


        return "crearVehicle";
    }

    @PostMapping("/crear")
    public String crearVehicle(@ModelAttribute Vehiculo vehicle){

        vehicleService.guardarVehiculo(vehicle);
        return "redirect:/cataleg";

    }
}
