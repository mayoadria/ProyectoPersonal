package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Incidencia;
import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import adria.mayo.proyectopersonal.entity.enums.estatIncidencia.EstatIncidencia;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.IncidenciaService;
import adria.mayo.proyectopersonal.service.ReservaService;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agent")
public class IncidenciasController {

    private final IncidenciaService incidenciaService;
    private final ReservaService reservaService;
    private final VehicleService vehicleService;


    public IncidenciasController(IncidenciaService incidenciaService, ReservaService reservaService, VehicleService vehicleService) {
        this.incidenciaService = incidenciaService;
        this.reservaService = reservaService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/listaIncidencias")
    public String listaIncidencias(Model model) {
        List<Incidencia> listaIncidencias = incidenciaService.listaIncidencias();
        model.addAttribute("incidencias", listaIncidencias);
        return "listaIncidencias";
    }
    @GetMapping("/listaIncidenciasPorVehiculo/{matricula}")
    public String listaIncidenciasPorVehiculo(@PathVariable String matricula, Model model) {
        List<Incidencia> listaIncidencias = incidenciaService.listaIncidenciasPorVehiculo(matricula);
        model.addAttribute("incidencias", listaIncidencias);
        return "listaIncidencias";
    }


    @GetMapping("/crearIncidencia/{matricula}")
    public String crearIncidencia(@PathVariable String matricula, Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        Optional<Vehiculo> vehiculoOptional = vehicleService.buscarVehiculoOptional(matricula);

        if (vehiculoOptional.isEmpty()) {
            // Maneja el error: muestra página de error o redirige
            return "redirect:/error"; // o cualquier página de error
        }

        Vehiculo vehiculo = vehiculoOptional.get();

        if (usuari != null) {
            Reserva reserva = reservaService.trobarReservaIncidencia(
                    usuari.getDni(), vehiculo.getMatricula(), EstatReserva.ACCEPTADA
            );
            if (reserva != null) {
                model.addAttribute("reserva", reserva);
            }
        }

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("incidencia", new Incidencia());

        return "crearIncidencia";
    }



    @PostMapping("/crear/{matricula}")
    public String crearIncidencia(@PathVariable String matricula, Incidencia incidencia, Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        incidencia.setUsuari(usuari);
        incidencia.setDataInici(LocalDate.now());
        incidencia.setDataFinal(null);
        incidencia.setEstatIncidencia(EstatIncidencia.OBERTA);

        // Buscar el vehículo persistido desde la base de datos por matrícula o ID
        Optional<Vehiculo> vehiculoOptional = vehicleService.buscarVehiculoOptional(matricula);

        if (vehiculoOptional.isPresent()) {
            incidencia.setVehicle(vehiculoOptional.get()); // establecer el objeto gestionado por Hibernate
            incidenciaService.crearIncidencia(incidencia);
            return "redirect:/agent/listaIncidencias";
        } else {
            model.addAttribute("error", "No se encontró el vehículo especificado.");
            return "crearIncidencia"; // o una página de error adecuada
        }
    }




}
