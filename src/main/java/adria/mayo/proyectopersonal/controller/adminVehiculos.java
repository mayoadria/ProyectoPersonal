package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.dto.UsuariMapper;
import adria.mayo.proyectopersonal.dto.VehicleFltroDTO;
import adria.mayo.proyectopersonal.dto.VehicleRespuestaDTO;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import adria.mayo.proyectopersonal.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class adminVehiculos {

    private final VehicleService vehiculoService;
    private final UsuariMapper usuariMapper;
    private final UsuariService usuariService;

    public adminVehiculos (VehicleService vehiculoService, UsuariMapper usuariMapper, UsuariService usuariService) {
        this.vehiculoService = vehiculoService;
        this.usuariMapper = usuariMapper;
        this.usuariService = usuariService;
    }


    private void prepararFormularioCrearVehiculo(Model model) {
        model.addAttribute("places", Places.values());
        model.addAttribute("portes", Portes.values());
        model.addAttribute("combustible", Combustible.values());
        model.addAttribute("caixaCanvis", CaixaCanvis.values());
        model.addAttribute("Marxes", Marxes.values());
        model.addAttribute("estat", EstatVehicle.values());
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
        prepararFormularioCrearVehiculo(model);
        model.addAttribute("isLogged", true);
        model.addAttribute("isEdit", false);
        return "crearVehicle";
    }

    @PostMapping("/newVehicle")
    public String crearVehiculoAdmin(@Valid @ModelAttribute("vehiculo")Vehiculo vehiculo, BindingResult result, Model model, @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);

        Optional<Vehiculo> vehiculoExistente = vehiculoService.buscarVehiculoOptional(vehiculo.getMatricula());
        if (result.hasErrors()){
            prepararFormularioCrearVehiculo(model);
            model.addAttribute("isLogged", true);
            model.addAttribute("isEdit", false);
            return "crearVehicle";
        }
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
        Vehiculo vehiculo = vehiculoService.buscarVehiculo(matricula);
        List<Usuari> usuaris = usuariService.findAll();
        if (vehiculo != null) {
            model.addAttribute("vehiculo", vehiculo);
            prepararFormularioCrearVehiculo(model);
            model.addAttribute("usuaris", usuaris);
            model.addAttribute("isEdit", true);
            return "crearVehicle";
        } else {
            return "redirect:/admin/llistaVehiculo";
        }
    }

    @PostMapping("/editarVehicle")
    public String guardarEdicioVehicle(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo, BindingResult result,
                                       @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                       Model model) throws IOException {
        if (result.hasErrors()){
            prepararFormularioCrearVehiculo(model);
            model.addAttribute("isLogged", true);
            model.addAttribute("isEdit", true);
            return "crearVehicle";
        }
        if (imagen != null && !imagen.isEmpty()) {
            String base64Foto = Base64.getEncoder().encodeToString(imagen.getBytes());
            vehiculo.setFoto(base64Foto);
        }
        vehiculoService.guardarVehiculo(vehiculo);
        return "redirect:/admin/llistaVehiculo";
    }


    @GetMapping("/visualizarDetallsVehicle/{matricula}")
    public String verDetallsVehiculo(@PathVariable String matricula, Model model) {
        Vehiculo vehiculo = vehiculoService.buscarVehiculo(matricula);
        if (vehiculo != null) {
            model.addAttribute("vehiculo", vehiculo);
            prepararFormularioCrearVehiculo(model);
            return "infoVehicleAdmin";
        } else {
            return "redirect:/admin/llistaVehiculo";
        }
    }


}
