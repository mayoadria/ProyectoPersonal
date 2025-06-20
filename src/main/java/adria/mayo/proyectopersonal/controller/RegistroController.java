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

@Controller
public class RegistroController {

    @Autowired
    private UsuariService usuariService;

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuari", new Usuari());
        model.addAttribute("pais", Pais.values());
        return "Registre";
    }

    @PostMapping("/crearUsuari")
    public String crearUsuari(@ModelAttribute("usuari") Usuari usuari) {
        if(usuariService.findByEmail(usuari.getEmail()) == null) {
            usuari.setRol(Rol.CLIENTE);
            usuari.setEstat(EstatUsuari.INACTIVO);
            usuari.setNomUsuari(usuari.getEmail().substring(0, usuari.getEmail().indexOf("@")));
            usuariService.crearUsuari(usuari);
        }
        return "redirect:/login";
    }
}
