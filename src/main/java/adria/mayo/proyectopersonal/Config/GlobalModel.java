package adria.mayo.proyectopersonal.Config;

import adria.mayo.proyectopersonal.security.UserUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModel {

    @ModelAttribute("isLogged")
    public boolean isLogged() {
        // Reemplaza con tu lógica real de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
    @ModelAttribute
    public void agregarDetallesUsuario(Model model) {
        UserUtils.getUsuariDetalls(model);
    }
}

