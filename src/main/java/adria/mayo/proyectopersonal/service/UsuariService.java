package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.Excepciones.Usuari.AdminException;
import adria.mayo.proyectopersonal.Excepciones.Usuari.EncontrarUsuarioException;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.repository.UsuarioRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariService {

    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuariService(UsuarioRepo usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear usuario
    public void crearUsuari(Usuari usuari, Rol rol, EstatUsuari estat) {
        if (usuari.getEmail() != null) {
            usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));
            usuari.setRol(rol);
            usuari.setEstat(estat);
            usuari.setNomUsuari(usuari.getEmail().substring(0, usuari.getEmail().indexOf("@")));
            usuarioRepo.save(usuari);
        }
    }

    // Métodos con excepción para uso crítico
    public Usuari findBynomUsuari(String nomUsuari) {
        return usuarioRepo.findBynomUsuari(nomUsuari)
                .orElseThrow(() -> new EncontrarUsuarioException("Usuario no encontrado en la base de datos"));
    }

    public Usuari findByEmail(String email) {
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new EncontrarUsuarioException("Usuario no encontrado en la base de datos"));
    }

    public Usuari findByDni(String dni) {
        return usuarioRepo.findById(dni)
                .orElseThrow(() -> new EncontrarUsuarioException("Usuario no encontrado en la base de datos"));
    }

    // Métodos seguros con Optional para validación (crear)
    public Optional<Usuari> findByEmailOptional(String email) {
        return usuarioRepo.findByEmail(email);
    }

    public Optional<Usuari> findByDniOptional(String dni) {
        return usuarioRepo.findById(dni);
    }

    public Optional<Usuari> findByNomUsuariOptional(String nomUsuari) {
        return usuarioRepo.findBynomUsuari(nomUsuari);
    }

    // Obtener todos los usuarios
    public List<Usuari> findAll() {
        return usuarioRepo.findAll();
    }

    // Eliminar usuario con validación de rol
    public void eliminarUsuari(String nomUsuari) {
        Usuari usuari = findBynomUsuari(nomUsuari);
        if (usuari.getRol() == Rol.ADMINISTRADOR) {
            throw new AdminException("No se puede eliminar un admin, si quieres eliminar el usuario cambia el rol primero");
        }
        usuarioRepo.delete(usuari);
    }

    // Actualizar usuario manteniendo la contraseña existente
    public void actualizarUsuari(Usuari usuari) {
        Usuari usuariExistente = findByDni(usuari.getDni());
        if (passwordEncoder.matches(usuari.getContrasenya(), usuariExistente.getContrasenya())) {
            // Si es igual, no se modifica
            usuari.setContrasenya(usuariExistente.getContrasenya());
        } else {
            // Si es diferente, se codifica
            usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));
        }

        usuarioRepo.save(usuari);
    }

    // Activar o desactivar usuario
    public void activarUsuari(String nomUsuari) {
        Usuari usuari = findBynomUsuari(nomUsuari);

        if (usuari.getRol() == Rol.ADMINISTRADOR) {
            throw new AdminException("No se puede cambiar el estado a un administrador, cambia el rol primero");
        }

        switch (usuari.getEstat()) {
            case ACTIVO:
                usuari.setEstat(EstatUsuari.INACTIVO);
                break;
            case INACTIVO:
                usuari.setEstat(EstatUsuari.ACTIVO);
                break;
        }
        usuarioRepo.save(usuari);
    }

    // Búsqueda avanzada
    public Page<Usuari> buscarUsuarisAvançat(String dni, String nom, String cognom, String email,
                                             String nomUsuari, String telf, String codiPostal, String direccio,
                                             String poblacio, EstatUsuari estat, Pais pais, Pageable pageable) {
        return usuarioRepo.buscarUsuarisAvançat(dni, nom, cognom, email, nomUsuari, telf,
                codiPostal, direccio, poblacio, estat, pais, pageable);
    }
}
