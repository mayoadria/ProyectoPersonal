package adria.mayo.proyectopersonal.security;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

public class UserUtils {

    public static Object getUsuariDetalls(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() &&
                !(auth.getPrincipal() instanceof String)) {

            Usuari usuari = (Usuari) auth.getPrincipal();
            String nomUsuari = usuari.getNomUsuari();

            if (usuari.getRol() == Rol.ADMINISTRADOR) {
                model.addAttribute("nomUsuari", nomUsuari);
                model.addAttribute("isLogged", true);
                model.addAttribute("isAdmin", true);
                model.addAttribute("isAgent", false);
                model.addAttribute("isClient", false);

                return usuari;
            }
            if (usuari.getRol() == Rol.CLIENTE) {
                model.addAttribute("nomUsuari", nomUsuari);
                model.addAttribute("isLogged", true);
                model.addAttribute("isAdmin", false);
                model.addAttribute("isAgent", false);
                model.addAttribute("isClient", true);

                return usuari;
            }
            if (usuari.getRol() == Rol.AGENTE) {
                model.addAttribute("nomUsuari", nomUsuari);
                model.addAttribute("isLogged", true);
                model.addAttribute("isAdmin", false);
                model.addAttribute("isAgent", true);
                model.addAttribute("isClient", false);

                return usuari;
            }
        }
        return null;
    }
}
