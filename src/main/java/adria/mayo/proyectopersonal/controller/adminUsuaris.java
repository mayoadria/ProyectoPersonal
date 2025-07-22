package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.dto.*;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class adminUsuaris {

    private final UsuariService usuariService;


    public adminUsuaris(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    private void prepararFormularioCrearUsuario(Model model, boolean isEdit) {
        model.addAttribute("pais", Pais.values());
        model.addAttribute("rol", Rol.values());
        model.addAttribute("estat", EstatUsuari.values());
        model.addAttribute("isEdit", isEdit);
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        model.addAttribute("isAdmin", usuari.getRol() == Rol.ADMINISTRADOR);
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
        prepararFormularioCrearUsuario(model,false);
        return "CrearUsuari";
    }

    @PostMapping("/newUsuari")
    public String crearUsuarisAdmin(@Valid @ModelAttribute("usu") Usuari usuari, BindingResult result, Model model) {
        if (result.hasErrors()) {
            prepararFormularioCrearUsuario(model,false);
            return "CrearUsuari";
        }
        if (usuariService.findByEmailOptional(usuari.getEmail()).isPresent()) {
            result.rejectValue("email", "error.usu", "El email ya está registrado");
            prepararFormularioCrearUsuario(model,false);
            return "CrearUsuari";  // Volvemos a la vista con error sin insertar
        }
        if (usuariService.findByDniOptional(usuari.getDni()).isPresent()) {
            result.rejectValue("dni", "error.usu", "El dni ya está registrado");
            prepararFormularioCrearUsuario(model,false);
            return "CrearUsuari";  // Volvemos a la vista con error sin insertar
        }

        usuariService.crearUsuari(usuari,usuari.getRol(),usuari.getEstat());
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
            prepararFormularioCrearUsuario(model,true);
            return "CrearUsuari";
        } else {
            return "redirect:/admin/listaUsu";
        }
    }

    @PostMapping("/editarUsuari")
    public String guardarEdicioUsuari(@Valid @ModelAttribute("usu") Usuari usuari, BindingResult result, Model model) {
        // Validación de errores del formulario
        if (result.hasErrors()) {
            prepararFormularioCrearUsuario(model, true);
            return "CrearUsuari";
        }

        // Verificamos si el email ya está en uso por otro usuario
        Usuari usuariConMismoEmail = usuariService.findByEmail(usuari.getEmail());
        if (usuariConMismoEmail != null && !usuariConMismoEmail.getNomUsuari().equals(usuari.getNomUsuari())) {
            result.rejectValue("email", "error.usu", "El email ya está registrado por otro usuario");
            prepararFormularioCrearUsuario(model, true);
            return "CrearUsuari";
        }

        // Guardamos la edición
        usuariService.actualizarUsuari(usuari);
        return "redirect:/admin/listaUsu";
    }

}
