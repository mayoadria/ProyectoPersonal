package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Incidencia;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    @Query("select i from Incidencia i where (:matricula is null or :matricula = i.vehicle.matricula)")
    List<Incidencia> vehiclelistIncidencias(@Param("matricula") String matricula);
}
