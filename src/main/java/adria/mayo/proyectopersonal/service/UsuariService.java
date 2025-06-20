package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariService {

    @Autowired
    private UsuarioRepo usuarioRepo;


    public void crearUsuari(Usuari usuari){
        usuarioRepo.save(usuari);
    }

    public Usuari findByEmail(String email){
       return usuarioRepo.findByEmail(email);
    }
}
