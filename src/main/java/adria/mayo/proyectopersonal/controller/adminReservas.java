package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.EnviarCorreo;
import adria.mayo.proyectopersonal.service.ReservaService;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class adminReservas {

    private final VehicleService vehicleService;
    private final ReservaService reservaService;
    private final EnviarCorreo enviarCorreo;


    public adminReservas(VehicleService vehicleService, ReservaService reservaService, EnviarCorreo enviarCorreo) {
        this.vehicleService = vehicleService;
        this.reservaService = reservaService;
        this.enviarCorreo = enviarCorreo;
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


    @GetMapping("/listarReserva")
    public String listar(Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        List<Reserva> reservas = reservaService.listarReservas();

        if (usuari.getRol() == Rol.AGENTE) {
            // Filtrar reservas en las que el coche pertenece al agente
            List<Reserva> reservasAgente = reservas.stream()
                    .filter(r -> r.getVehiculo().getCreador().getDni().equals(usuari.getDni()))
                    .toList();
            model.addAttribute("reserva", reservasAgente);
        } else if (usuari.getRol() == Rol.CLIENTE) {
            // Solo las reservas hechas por este usuario
            List<Reserva> reservasClientes = reservaService.buscarReservasPorUsuario(usuari.getDni());
            model.addAttribute("reserva", reservasClientes);
        } else {
            // ADMIN u otro rol con permisos completos
            model.addAttribute("reserva", reservas);
        }

        return "listaReservas";
    }

    @PostMapping("/cancelarReserva/{idReserva}/{matricula}")
    public String cancelarReserva(@PathVariable Long idReserva, @PathVariable String matricula) {
        Optional<Reserva> optionalReserva = reservaService.trobarReserva(idReserva);
        Vehiculo vehiculo = vehicleService.buscarVehiculo(matricula);
        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();
            reserva.setEstatReserva(EstatReserva.ANULLADA);
            if (vehiculo != null) {
                vehicleService.activarVehiculo(matricula);
                vehicleService.guardarVehiculo(reserva.getVehiculo());
            }
            reservaService.crearReserva(reserva);
            enviarCorreo.enviarCorreoReservaCancelada(reserva.getUsuari().getEmail(),reserva.getVehiculo().getMatricula(),reserva);
        }
        return "redirect:/admin/listarReserva";
    }


    @PostMapping("/activarReserva/{idReserva}")
    public String activarReserva(@PathVariable Long idReserva) {
        Optional<Reserva> optionalReserva = reservaService.trobarReserva(idReserva);

        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();
            reserva.setEstatReserva(EstatReserva.ACCEPTADA);
            reservaService.crearReserva(reserva);
            enviarCorreo.enviarCorreoReservaA(reserva.getUsuari().getEmail(),reserva.getVehiculo().getMatricula(),reserva);
        }
        return "redirect:/admin/listarReserva";
    }


}
