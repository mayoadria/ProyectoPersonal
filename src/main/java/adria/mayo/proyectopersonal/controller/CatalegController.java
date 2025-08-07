package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class CatalegController {

    private final VehicleService vehicleService;

    public CatalegController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/cataleg")
    public String cataleg(Model model){
        UserUtils.getUsuariDetalls(model);
        List<Vehiculo> vehiculos = vehicleService.listarVehiculosActivos(EstatVehicle.ACTIU);
        List<Vehiculo> vehiculosConCreador = vehiculos.stream().filter(v -> v.getCreador() !=null).toList();
        model.addAttribute("vehiculos", vehiculosConCreador);
        return "cataleg";
    }


    @GetMapping("/detallsVehicle/{matricula}")
    @PreAuthorize("hasAnyRole('CLIENTE','AGENTE','ADMINISTRADOR')")
    public String detallsVehiculo(@PathVariable String matricula, Model model){
        UserUtils.getUsuariDetalls(model);
        Vehiculo vehiculo = vehicleService.buscarVehiculo(matricula);
        if(vehiculo != null){
            model.addAttribute("vehicle", vehiculo);
        }

        return "infoVehiculo";
    }


}
