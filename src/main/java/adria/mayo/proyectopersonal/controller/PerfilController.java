package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuariService usuariService;

    @GetMapping("/mostrarPerfil")
    public String mostrarPerfil(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            String nomUsuari = authentication.getName();
            model.addAttribute("nomUsuari", nomUsuari);
            model.addAttribute("isLogged", true);
            model.addAttribute("pais", Pais.values());

            Usuari usuari = usuariService.findBynomUsuari(nomUsuari);
            model.addAttribute("cliente", usuari);

        }
        return "Perfil";
    }


    @PostMapping("/editar")
    public String editar(Model model, Usuari usuari) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            String nombreUsuarioLogueado = authentication.getName();

            Usuari clienteExistente = usuariService.findBynomUsuari(nombreUsuarioLogueado);

            if (clienteExistente != null) {
                if (!usuari.getEmail().equals(clienteExistente.getEmail())) {
                    clienteExistente.setEmail(usuari.getEmail());
                    clienteExistente.setNomUsuari(usuari.getEmail().substring(0, usuari.getEmail().indexOf("@")));
                } else {
                    clienteExistente.setEmail(usuari.getEmail());
                }

                clienteExistente.setNom(usuari.getNom());
                clienteExistente.setCognoms(usuari.getCognoms());
                clienteExistente.setDireccio(usuari.getDireccio());
                clienteExistente.setCodiPostal(usuari.getCodiPostal());
                clienteExistente.setNumContacte(usuari.getNumContacte());
                clienteExistente.setPoblacio(usuari.getPoblacio());
                clienteExistente.setPais(usuari.getPais());

                usuariService.crearUsuari(clienteExistente); // Guardar los cambios
            }
        }

        return "redirect:/perfil/mostrarPerfil";
    }

}
