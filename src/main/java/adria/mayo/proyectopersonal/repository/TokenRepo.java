package adria.mayo.proyectopersonal.repository;


import adria.mayo.proyectopersonal.entity.Token;
import adria.mayo.proyectopersonal.entity.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
    Optional<Token> findByUsuari(Usuari client);

    Optional<Token> findByTokenCode(String tokenCode);
}
