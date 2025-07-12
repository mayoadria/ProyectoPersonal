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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class DashboardAdmin {

    private final ReservaService reservaService;
    private final UsuariService usuariService;
    private final VehicleService vehiculoService;

    public DashboardAdmin(ReservaService reservaService, UsuariService usuariService, VehicleService vehiculoService) {
        this.reservaService = reservaService;
        this.usuariService = usuariService;
        this.vehiculoService = vehiculoService;
    }


    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        return "adminDashboard";
    }

    @GetMapping("/listaUsu")
    public String listaUsu(
            @RequestParam(name = "Dni", required = false) String Dni,
            @RequestParam(name = "Nom", required = false) String Nom,
            @RequestParam(name = "Cognom", required = false) String Cognom,
            @RequestParam(name = "Email", required = false) String Email,
            @RequestParam(name = "nomUsuari", required = false) String nomUsuari,
            @RequestParam(name = "telf", required = false) String telf,
            @RequestParam(name = "codiPostal", required = false) String codiPostal,
            @RequestParam(name = "direccio", required = false) String direccio,
            @RequestParam(name = "poblacio", required = false) String poblacio,
            @RequestParam(name = "estat", required = false) EstatUsuari estat,
            @RequestParam(name = "pais", required = false) Pais pais,

            Model model) {
        List<Usuari> usu = usuariService.buscarUsuarisAvançat(
                Dni, Nom, Cognom, Email, nomUsuari, telf, codiPostal, direccio, poblacio, estat, pais
        );

        model.addAttribute("usu", usu);
        model.addAttribute("estats", EstatUsuari.values());
        model.addAttribute("pais", Pais.values());
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
        model.addAttribute("isEdit", false);
        return "CrearUsuari";
    }

    @PostMapping("/newUsuari")
    public String crearUsuarisAdmin(Usuari usuari) {
        usuariService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
        return "redirect:/admin/listaUsu";
    }

    @GetMapping("/editarUsuari/{nomUsuari}")
    public String editarUsuari(@PathVariable String nomUsuari, Model model) {
        Usuari usuariOptional = usuariService.findBynomUsuari(nomUsuari);
        if (usuariOptional != null) {
            model.addAttribute("usu", usuariOptional);
            model.addAttribute("pais", Pais.values());
            model.addAttribute("rol", Rol.values());
            model.addAttribute("estat", EstatUsuari.values());
            model.addAttribute("isEdit", usuariOptional.getDni() != null);
            return "CrearUsuari";
        } else {
            return "redirect:/admin/listaUsu";
        }
    }

    @PostMapping("/editarUsuari")
    public String guardarEdicioUsuari(@ModelAttribute("usu") Usuari usuari) {
        usuariService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
        return "redirect:/admin/listaUsu";
    }


    @GetMapping("/llistaVehiculo")
    public String llistaVehiculo(
            @RequestParam(name = "matricula", required = false) String matricula,
            @RequestParam(name = "marca", required = false) String marca,
            @RequestParam(name = "estatVehicle", required = false) EstatVehicle estatVehicle,
            @RequestParam(name = "combustible", required = false) Combustible combustible,
            @RequestParam(name = "canvis", required = false) CaixaCanvis canvis,
            @RequestParam(name = "minAny", required = false) Integer minAny,
            @RequestParam(name = "maxAny", required = false) Integer maxAny,
            Model model) {
        List<Vehiculo> vehicle = vehiculoService.buscarVehiculosFiltro(matricula,marca,estatVehicle,combustible,canvis);

        if (minAny != null || maxAny != null) {
            int min = (minAny != null) ? minAny : Integer.MIN_VALUE;
            int max = (maxAny != null) ? maxAny : Integer.MAX_VALUE;

            vehicle = vehicle.stream()
                    .filter(v -> v.getAnyVehicle() >= min && v.getAnyVehicle() <= max)
                    .collect(Collectors.toList());
        }
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("estatVehicle", EstatVehicle.values());
        model.addAttribute("combustible", Combustible.values());
        model.addAttribute("canvis", CaixaCanvis.values());
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
        model.addAttribute("isLogged", true);
        model.addAttribute("isEdit", false);
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

    @GetMapping("/editarVehicle/{matricula}")
    public String editarVehicle(@PathVariable String matricula, Model model) {
        Optional<Vehiculo> vehiculo = vehiculoService.buscarVehiculo(matricula);
        List<Usuari> usuaris = usuariService.findAll();
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            model.addAttribute("places", Places.values());
            model.addAttribute("portes", Portes.values());
            model.addAttribute("combustible", Combustible.values());
            model.addAttribute("caixaCanvis", CaixaCanvis.values());
            model.addAttribute("Marxes", Marxes.values());
            model.addAttribute("estat", EstatVehicle.values());
            model.addAttribute("usuaris", usuaris);
            model.addAttribute("isEdit", true);
            return "crearVehicle";
        } else {
            return "redirect:/admin/llistaVehiculo";
        }
    }

    @PostMapping("/editarVehicle")
    public String guardarEdicioVehicle(@ModelAttribute("vehiculo") Vehiculo vehiculo, @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String base64Foto = Base64.getEncoder().encodeToString(imagen.getBytes());
            vehiculo.setFoto(base64Foto);
        }
        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/admin/llistaVehiculo";
    }


    @GetMapping("/visualizarDetallsVehicle/{matricula}")
    public String verDetallsVehiculo(@PathVariable String matricula, Model model) {
        Optional<Vehiculo> vehiculo = vehiculoService.buscarVehiculo(matricula);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            model.addAttribute("places", Places.values());
            model.addAttribute("portes", Portes.values());
            model.addAttribute("combustible", Combustible.values());
            model.addAttribute("caixaCanvis", CaixaCanvis.values());
            model.addAttribute("Marxes", Marxes.values());
            model.addAttribute("estat", EstatVehicle.values());
            return "infoVehicleAdmin";
        } else {
            return "redirect:/admin/llistaVehiculo";
        }
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
        Optional<Vehiculo> vehiculo = vehiculoService.buscarVehiculo(matricula);
        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();
            reserva.setEstatReserva(EstatReserva.ANULLADA);
            if (vehiculo.isPresent()) {
                vehiculoService.activarVehiculo(matricula);
                vehiculoService.guardarVehiculo(reserva.getVehiculo());
            }
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
