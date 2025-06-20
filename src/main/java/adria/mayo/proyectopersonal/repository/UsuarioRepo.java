package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuari, String> {
    Usuari findByEmail(String email);
}
