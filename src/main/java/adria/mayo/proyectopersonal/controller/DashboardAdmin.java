package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashboardAdmin {


    @Autowired
    private UsuariService usuariService;

    @Autowired
    private VehicleService vehiculoService;

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
        usuariService.crearUsuari(usuari,usuari.getRol(),usuari.getEstat());
        return "redirect:/admin/listaUsu";
    }

    @GetMapping("/llistaVehiculo")
    public String llistaVehiculo(Model model) {
        List<Vehiculo> vehicle = vehiculoService.listarVehiculos();
        model.addAttribute("vehicle" ,vehicle);
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
    public String crearVehiculoAdmin(Vehiculo vehiculo, Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);

        Optional<Vehiculo> vehiculoExistente = vehiculoService.buscarVehiculo(vehiculo.getMatricula());

        if (vehiculoExistente.isEmpty()) {
            // Solo lo creamos si no existe
            if (usuari.getRol() == Rol.AGENTE) {
                vehiculo.setCreador(usuari);
            } else {
                vehiculo.setCreador(null);
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


}
