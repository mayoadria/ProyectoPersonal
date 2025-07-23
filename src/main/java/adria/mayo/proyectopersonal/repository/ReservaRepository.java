package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE (:dni is null or :dni = r.usuari.dni)")
    List<Reserva> findByDni(@RequestParam("dni") String dni);

    @Query("SELECT r from Reserva r where" +
            "(:matricula is null or lower(r.vehiculo.matricula) like(concat('%',:matricula, '%') ))" +
            "and (:dni is null or lower(r.usuari.dni) like(concat('%',:dni,'%') ))" +
            "and (:estat is null or  r.estatReserva = :estat)")
    Page<Reserva> listaFiltrada(@Param("matricula") String matricula,
                                @Param("dni") String dni,
                                @Param("estat")EstatReserva estat,
                                Pageable pageable);
}
