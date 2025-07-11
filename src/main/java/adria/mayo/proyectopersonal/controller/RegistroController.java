package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String crearUsuari(@ModelAttribute("usuari") Usuari usuari) {
        if(usuariService.findBynomUsuari(usuari.getEmail()) == null) {
            usuariService.crearUsuari(usuari,Rol.CLIENTE,EstatUsuari.INACTIVO);
        }
        return "redirect:/login";
    }
}
