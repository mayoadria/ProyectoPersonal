package adria.mayo.proyectopersonal.security;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

public class UserUtils {



    public static Object getUsuariDetalls(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() &&
                !(auth.getPrincipal() instanceof String)) {

            Object principal = auth.getPrincipal();
            Usuari usuari = null;

            if (principal instanceof Usuari) {
                usuari = (Usuari) principal;
            } else if (principal instanceof User) {
                User user = (User) principal;
                // Crear un Usuari "temporal" solo con el nombre de usuario
                usuari = new Usuari();
                usuari.setNomUsuari(user.getUsername());
                // Opcionalmente asignar un rol por defecto o buscar en base de datos
                usuari.setRol(Rol.CLIENTE); // por ejemplo
            } else {
                // Otros tipos, puedes lanzar excepción o retornar null
                return null;
            }

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
