package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void crearUsuari(Usuari usuari, Rol rol, EstatUsuari estat) {
        if (usuari.getEmail() != null) {
            usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));
            usuari.setRol(rol);
            usuari.setEstat(estat);
            usuari.setNomUsuari(usuari.getEmail().substring(0, usuari.getEmail().indexOf("@")));
            usuarioRepo.save(usuari);
        }
    }

    public Usuari findBynomUsuari(String nomUsuari) {
        return usuarioRepo.findBynomUsuari(nomUsuari);
    }

    public List<Usuari> findAll() {
        return usuarioRepo.findAll();
    }

    public void eliminarUsuari(String nomUsuari) {
        Usuari usuari = findBynomUsuari(nomUsuari);
        usuarioRepo.delete(usuari);
    }

    public void actualizarUsuari(Usuari usuari) {
        usuarioRepo.save(usuari);
    }

    public void activarUsuari(String nomUsuari) {
        Usuari usuari = findBynomUsuari(nomUsuari);
        switch (usuari.getEstat()) {
            case ACTIVO:
                usuari.setEstat(EstatUsuari.INACTIVO);
                usuarioRepo.save(usuari);
                break;

            case INACTIVO:
                usuari.setEstat(EstatUsuari.ACTIVO);
                usuarioRepo.save(usuari);
                break;
        }


    }

    public List<Usuari> buscarUsuarisAvançat(String dni, String nom, String cognom, String email,
                                             String nomUsuari, String telf, String codiPostal, String direccio,
                                             String poblacio, EstatUsuari estat, Pais pais) {
        return usuarioRepo.buscarUsuarisAvançat(dni, nom, cognom, email, nomUsuari, telf,
                codiPostal, direccio, poblacio, estat, pais);
    }
}
