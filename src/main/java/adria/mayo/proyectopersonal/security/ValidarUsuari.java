package adria.mayo.proyectopersonal.security;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.service.UsuariService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ValidarUsuari implements UserDetailsService {

    private final UsuariService usuariService;

    public ValidarUsuari(UsuariService usuariService) {
        this.usuariService = usuariService;
    }


    @Override
    public UserDetails loadUserByUsername(String nomUsuari) throws UsernameNotFoundException {
        Usuari usuari = usuariService.findBynomUsuari(nomUsuari);

        if (usuari.getEstat() != EstatUsuari.ACTIVO) {
            throw new UsernameNotFoundException("El compte està inactiu.");
        }

        return usuari;
    }


}
