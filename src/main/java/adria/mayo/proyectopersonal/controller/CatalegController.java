package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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


    @GetMapping("/detallsVehicle/{matricula}")
    public String detallsVehiculo(@PathVariable String matricula, Model model){
        Optional<Vehiculo> vehiculo = vehicleService.buscarVehiculo(matricula);
        if(vehiculo.isPresent()){
            model.addAttribute("vehicle", vehiculo.get());
        }

        return "infoVehiculo";
    }
}
