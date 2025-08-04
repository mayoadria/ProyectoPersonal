package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.security.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminD")
public class adminDashboard {

    @GetMapping("/adminDashboard")
    @PreAuthorize("hasAnyRole('AGENTE','ADMINISTRADOR')")
    public String adminDashboard(Model model) {
        Usuari usuari = (Usuari) UserUtils.getUsuariDetalls(model);
        boolean isAdmin = usuari != null && usuari.getRol() == Rol.ADMINISTRADOR;
        model.addAttribute("isAdmin", isAdmin);
        return "adminDashboard";
    }
}
