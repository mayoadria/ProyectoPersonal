package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.ReservaService;
import adria.mayo.proyectopersonal.service.UsuariService;
import adria.mayo.proyectopersonal.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private final VehicleService vehicleService;

    private final ReservaService reservaService;

    public ReservaController(VehicleService vehicleService, ReservaService reservaService) {
        this.vehicleService = vehicleService;
        this.reservaService = reservaService;
    }

    @PostMapping("/crearReserva/{matricula}")
    public String createReserva(
            @PathVariable String matricula,
            Model model, Reserva reserva) {

        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        Vehiculo vehiculo = vehicleService.buscarVehiculo(matricula);
        if (usuari != null) {
            reserva.setUsuari(usuari);
            reserva.setEstatReserva(EstatReserva.PENDENT);
            if (vehiculo != null) {
                reserva.setVehiculo(vehiculo);
            }
            reservaService.crearReserva(reserva);
            vehiculo.setEstatVehicle(EstatVehicle.INACTIU);
            vehicleService.guardarVehiculo(vehiculo);
        }
        return "redirect:/cataleg";

    }
}
