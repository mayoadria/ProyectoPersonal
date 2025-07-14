package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.security.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MenuController {

    @GetMapping("/")
    public String index(Model model) {
        UserUtils.getUsuariDetalls(model);
        return "home"; // Devuelve la vista "index"
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,Model model) {
        // Obtiene la autenticación actual del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario está autenticado, cierra la sesión
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/"; // Redirige a la página principal
    }
}
