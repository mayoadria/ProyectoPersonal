package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.ReservaService;
import adria.mayo.proyectopersonal.service.UsuariService;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashboardAdmin {


    @Autowired
    private ReservaService reservaService;
    @Autowired
    private UsuariService usuariService;

    @Autowired
    private VehicleService vehiculoService;


    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        return "adminDashboard";
    }

    @GetMapping("/listaUsu")
    public String listaUsu(Model model) {
        List<Usuari> usu = usuariService.findAll();
        model.addAttribute("usu", usu);
        return "ListaUsu";
    }

    @PostMapping("/eliminar/{nomUsuari}")
    public String eliminarUsu(@PathVariable String nomUsuari) {
        usuariService.eliminarUsuari(nomUsuari);
        return "redirect:/admin/listaUsu";
    }

    @PostMapping("/activar/{nomUsuari}")
    public String activarUsu(@PathVariable String nomUsuari) {
        usuariService.activarUsuari(nomUsuari);
        return "redirect:/admin/listaUsu";
    }


    @GetMapping("/creaUsuariAdmin")
    public String crearUsuari(Model model) {
        model.addAttribute("usu", new Usuari());
        model.addAttribute("pais", Pais.values());
        model.addAttribute("rol", Rol.values());
        model.addAttribute("estat", EstatUsuari.values());
        return "CrearUsuari";
    }

    @PostMapping("/newUsuari")
    public String crearUsuarisAdmin(Usuari usuari) {
        usuariService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
        return "redirect:/admin/listaUsu";
    }

    @GetMapping("/llistaVehiculo")
    public String llistaVehiculo(Model model) {
        List<Vehiculo> vehicle = vehiculoService.listarVehiculos();
        model.addAttribute("vehicle", vehicle);
        return "ListaVehicles";
    }

    @PostMapping("/eliminarVeh/{matricula}")
    public String eliminarVehiculo(@PathVariable String matricula) {
        vehiculoService.eliminarVehiculo(matricula);
        return "redirect:/admin/llistaVehiculo";
    }

    @PostMapping("/activarVeh/{matricula}")
    public String activarVeh(@PathVariable String matricula) {
        vehiculoService.activarVehiculo(matricula);
        return "redirect:/admin/llistaVehiculo";
    }


    @GetMapping("/creaVehiculoAdmin")
    public String crearVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("places", Places.values());
        model.addAttribute("portes", Portes.values());
        model.addAttribute("combustible", Combustible.values());
        model.addAttribute("caixaCanvis", CaixaCanvis.values());
        model.addAttribute("Marxes", Marxes.values());
        model.addAttribute("isLogges", true);
        return "crearVehicle";
    }

    @PostMapping("/newVehicle")
    public String crearVehiculoAdmin(Vehiculo vehiculo, Model model, @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);

        Optional<Vehiculo> vehiculoExistente = vehiculoService.buscarVehiculo(vehiculo.getMatricula());

        if (vehiculoExistente.isEmpty()) {
            // Solo lo creamos si no existe
            if (usuari.getRol() == Rol.AGENTE) {
                vehiculo.setCreador(usuari);
            } else {
                vehiculo.setCreador(null);
            }
            if (imagen != null && !imagen.isEmpty()) {
                String base64Foto = Base64.getEncoder().encodeToString(imagen.getBytes());
                vehiculo.setFoto(base64Foto);
            }
            vehiculo.setEstatVehicle(EstatVehicle.INACTIU);
            vehiculoService.guardarVehiculo(vehiculo);
        } else {
            // El vehículo ya existe: puedes añadir un mensaje o lógica de error
            model.addAttribute("error", "Ja existeix un vehicle amb aquesta matrícula.");
            return "crearVehicle"; // Vista con el formulario, por ejemplo
        }

        return "redirect:/admin/llistaVehiculo";
    }


    /*
     *
     * RESERVAS
     *
     */

    @GetMapping("/listarReserva")
    public String listar(Model model) {
        List<Reserva> reservas = reservaService.listarReservas();
        model.addAttribute("reserva", reservas);
        return "listaReservas";
    }


    @PostMapping("/cancelarReserva/{idReserva}/{matricula}")
    public String cancelarReserva(@PathVariable Long idReserva, @PathVariable String matricula) {
        Optional<Reserva> optionalReserva = reservaService.trobarReserva(idReserva);
        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();
            reserva.setEstatReserva(EstatReserva.ANULLADA);

            vehiculoService.activarVehiculo(matricula);
            vehiculoService.guardarVehiculo(reserva.getVehiculo());
            reservaService.crearReserva(reserva);
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
        }
        return "redirect:/admin/listarReserva";
    }




}
