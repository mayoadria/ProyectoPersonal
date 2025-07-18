package adria.mayo.proyectopersonal.controller;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.security.UserUtils;
import adria.mayo.proyectopersonal.service.UsuariService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/perfil")
public class PerfilController {


    private final UsuariService usuariService;

    public PerfilController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

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
    public String editar(@Valid @ModelAttribute("cliente") Usuari usuari,
                         BindingResult results,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                          Model model) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {

            if (results.hasErrors()) {
                model.addAttribute("pais", Pais.values());
                model.addAttribute("cliente", usuari);
                return "Perfil";
            }
            String nombreUsuarioLogueado = authentication.getName();

            Usuari clienteExistente = usuariService.findBynomUsuari(nombreUsuarioLogueado);

            if (clienteExistente != null) {
                // Actualizar datos básicos
                clienteExistente.setNom(usuari.getNom());
                clienteExistente.setCognoms(usuari.getCognoms());
                clienteExistente.setDireccio(usuari.getDireccio());
                clienteExistente.setCodiPostal(usuari.getCodiPostal());
                clienteExistente.setNumContacte(usuari.getNumContacte());
                clienteExistente.setPoblacio(usuari.getPoblacio());
                clienteExistente.setPais(usuari.getPais());

                if(imagen != null && !imagen.isEmpty()) {
                    String base64Foto = Base64.getEncoder().encodeToString(imagen.getBytes());
                    clienteExistente.setFoto(base64Foto);
                }

                // Email y nomUsuari
                String nuevoEmail = usuari.getEmail();
                String nuevoNomUsuari = nuevoEmail.substring(0, nuevoEmail.indexOf("@"));

                // Verificar si ese nombre de usuario ya existe en otro usuario
                Usuari otroConMismoNomUsuari = usuariService.findBynomUsuari(nuevoNomUsuari);
                if (otroConMismoNomUsuari != null && !otroConMismoNomUsuari.getDni().equals(clienteExistente.getDni())) {
                    model.addAttribute("error", "El nombre de usuario derivado del nuevo email ya está en uso.");
                    model.addAttribute("cliente", clienteExistente);
                    return "Perfil"; // Vuelve a la vista sin guardar
                }

                boolean nomUsuariCambiado = !nuevoNomUsuari.equals(clienteExistente.getNomUsuari());

                clienteExistente.setEmail(nuevoEmail);
                clienteExistente.setNomUsuari(nuevoNomUsuari);

                // Guardar cambios
                usuariService.actualizarUsuari(clienteExistente);

                // Logout si ha cambiado el nombre de usuario
                if (nomUsuariCambiado) {
                    new SecurityContextLogoutHandler().logout(request, response, authentication);
                    return "redirect:/login?logout";
                }
            }
        }

        return "redirect:/perfil/mostrarPerfil";
    }

}
