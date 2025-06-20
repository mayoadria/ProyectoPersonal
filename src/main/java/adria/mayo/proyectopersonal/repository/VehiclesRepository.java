package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehiculo, String> {
}
