package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.service.UsuariService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registrar")
public class RegistroController {


    private final UsuariService usuariService;

    public RegistroController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    @GetMapping("/mostrar_registro")
    public String registro(Model model) {
        model.addAttribute("usuari", new Usuari());
        model.addAttribute("pais", Pais.values());
        return "Registre";
    }

    @PostMapping("/crearUsuari")
    public String crearUsuari(@Valid @ModelAttribute("usuari") Usuari usuari, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pais", Pais.values());
            return "Registre";
        }
        if (usuariService.findByEmail(usuari.getEmail()) != null) {
            result.rejectValue("email", "error.usuari", "El email ya está registrado");
            model.addAttribute("pais", Pais.values());
            return "Registre";  // Volvemos a la vista con error sin insertar
        }
        if (usuariService.findByDni(usuari.getDni()) != null) {
            result.rejectValue("dni", "error.usu", "El dni ya está registrado");
            model.addAttribute("pais", Pais.values());
            return "CrearUsuari";  // Volvemos a la vista con error sin insertar
        }

        usuariService.crearUsuari(usuari, Rol.CLIENTE, EstatUsuari.INACTIVO);
        return "redirect:/login";
    }

}
