package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CatalegController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/cataleg")
    public String cataleg(Model model){
        List<Vehiculo> vehiculos = vehicleService.listarVehiculosActivos(EstatVehicle.ACTIU);
        model.addAttribute("vehiculos", vehiculos);
        return "cataleg";
    }
}
