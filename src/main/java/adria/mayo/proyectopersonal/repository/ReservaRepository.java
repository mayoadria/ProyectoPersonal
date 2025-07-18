package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE (:dni is null or :dni = r.usuari.dni)")
    List<Reserva> findByDni(@RequestParam("dni") String dni);
}
