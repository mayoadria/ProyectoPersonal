package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.dto.*;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final UsuariMapper usuariMapper;

    public DashboardAdmin(ReservaService reservaService, UsuariService usuariService, VehicleService vehiculoService,UsuariMapper usuariMapper) {
        this.reservaService = reservaService;
        this.usuariService = usuariService;
        this.vehiculoService = vehiculoService;
        this.usuariMapper = usuariMapper;
    }

    private void prepararFormularioCrear(Model model, boolean isEdit) {
        model.addAttribute("pais", Pais.values());
        model.addAttribute("rol", Rol.values());
        model.addAttribute("estat", EstatUsuari.values());
        model.addAttribute("isEdit", isEdit);
    }



    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        return "adminDashboard";
    }

    @GetMapping("/listaUsu")
    public String listaUsu(@ModelAttribute UsuariFiltroDTO filtro, Model model) {
        List<Usuari> usu = usuariService.buscarUsuarisAvançat(
                filtro.getDni(),
                filtro.getNom(),
                filtro.getCognom(),
                filtro.getEmail(),
                filtro.getNomUsuari(),
                filtro.getTelf(),
                filtro.getCodiPostal(),
                filtro.getDireccio(),
                filtro.getPoblacio(),
                filtro.getEstatUsuari(),
                filtro.getPais()
        );

        // Convertir a DTO
        List<UsuariRespuestaDTO> usuDTO = usu.stream()
                .map(u -> new UsuariRespuestaDTO(
                        u.getDni(),
                        u.getNom(),
                        u.getCognoms(),
                        u.getEmail(),
                        u.getNomUsuari(),
                        u.getEstat(),
                        u.getPais(),
                        u.getNumContacte(),
                        u.getCodiPostal(),
                        u.getDireccio(),
                        u.getPoblacio(),
                        u.getRol()
                ))
                .collect(Collectors.toList());



        model.addAttribute("usu", usuDTO);
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
        prepararFormularioCrear(model,false);
        return "CrearUsuari";
    }

    @PostMapping("/newUsuari")
    public String crearUsuarisAdmin(@Valid @ModelAttribute("usu") Usuari usuari, BindingResult result, Model model) {
        if (result.hasErrors()) {
            prepararFormularioCrear(model,false);
            return "CrearUsuari";
        }
        if (usuariService.findByEmail(usuari.getEmail()) != null) {
            result.rejectValue("email", "error.usu", "El email ya está registrado");
            prepararFormularioCrear(model,false);
            return "CrearUsuari";  // Volvemos a la vista con error sin insertar
        }
        if (usuariService.findByDni(usuari.getDni()).isPresent()) {
            result.rejectValue("dni", "error.usu", "El dni ya está registrado");
            prepararFormularioCrear(model,false);
            return "CrearUsuari";  // Volvemos a la vista con error sin insertar
        }
        usuariService.crearUsuari(usuari, usuari.getRol(), usuari.getEstat());
        return "redirect:/admin/listaUsu";
    }

    @GetMapping("/editarUsuari/{nomUsuari}")
    public String editarUsuari(@PathVariable String nomUsuari, Model model) {
        Usuari usuari = usuariService.findBynomUsuari(nomUsuari);
        if (usuari != null) {
            UsuariRespuestaDTO dto = new UsuariRespuestaDTO(
                    usuari.getDni(),
                    usuari.getNom(),
                    usuari.getCognoms(),
                    usuari.getEmail(),
                    usuari.getNomUsuari(),
                    usuari.getEstat(),
                    usuari.getPais(),
                    usuari.getNumContacte(),
                    usuari.getCodiPostal(),
                    usuari.getDireccio(),
                    usuari.getPoblacio(),
                    usuari.getRol()
            );
            model.addAttribute("usu", dto);
            prepararFormularioCrear(model,true);
            return "CrearUsuari";
        } else {
            return "redirect:/admin/listaUsu";
        }
    }

    @PostMapping("/editarUsuari")
    public String guardarEdicioUsuari(@Valid @ModelAttribute("usu") Usuari usuari, BindingResult result, Model model) {
        // Validación de errores del formulario
        if (result.hasErrors()) {
            prepararFormularioCrear(model, true);
            return "CrearUsuari";
        }

        // Verificamos si el email ya está en uso por otro usuario
        Usuari usuariConMismoEmail = usuariService.findByEmail(usuari.getEmail());
        if (usuariConMismoEmail != null && !usuariConMismoEmail.getNomUsuari().equals(usuari.getNomUsuari())) {
            result.rejectValue("email", "error.usu", "El email ya está registrado por otro usuario");
            prepararFormularioCrear(model, true);
            return "CrearUsuari";
        }

        // Guardamos la edición
        usuariService.actualizarUsuari(usuari);
        return "redirect:/admin/listaUsu";
    }



    @GetMapping("/llistaVehiculo")
    public String llistaVehiculo(
            @ModelAttribute VehicleFltroDTO filtro,
            @RequestParam(name = "minAny", required = false) Integer minAny,
            @RequestParam(name = "maxAny", required = false) Integer maxAny,
            Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);

        List<Vehiculo> vehicle = vehiculoService.buscarVehiculosFiltro(
                filtro.getMatricula(),
                filtro.getMarca(),
                filtro.getEstatVehicle(),
                filtro.getCombustible(),
                filtro.getCanvis()
        );

        if (minAny != null || maxAny != null) {
            int min = (minAny != null) ? minAny : Integer.MIN_VALUE;
            int max = (maxAny != null) ? maxAny : Integer.MAX_VALUE;

            vehicle = vehicle.stream()
                    .filter(v -> v.getAnyVehicle() >= min && v.getAnyVehicle() <= max)
                    .toList();
        }


        List<VehicleRespuestaDTO> vehicleDto = vehicle.stream().map(
                 vehiculo -> new VehicleRespuestaDTO(
                         vehiculo.getMatricula(),
                         vehiculo.getMarca(),
                         vehiculo.getModel(),
                         vehiculo.getPreuDia(),
                         vehiculo.getFianca(),
                         vehiculo.getDiesLloguerMinim(),
                         vehiculo.getDiesLloguerMaxim(),
                         vehiculo.getPlaces(),
                         vehiculo.getPortes(),
                         vehiculo.getCaixaCanvis(),
                         vehiculo.getMarxes(),
                         vehiculo.getCombustible(),
                         vehiculo.getColor(),
                         vehiculo.getEstatVehicle(),
                         vehiculo.getAnyVehicle(),
                         vehiculo.getKm(),
                         vehiculo.getFoto(),
                         usuariMapper.usuariToUsuariRespuestaDTO(vehiculo.getCreador())

                 )
        ).toList();

        if(usuari.getRol() == Rol.AGENTE){
            List<VehicleRespuestaDTO> vehicleDTOAgente = vehicleDto.stream().filter(
                    vehicleRespuestaDTO -> vehicleRespuestaDTO.getCreador().getNomUsuari().equals(usuari.getNomUsuari())
            ).toList();
            model.addAttribute("vehicle", vehicleDTOAgente);
        }else{
            model.addAttribute("vehicle", vehicleDto);
        }

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
