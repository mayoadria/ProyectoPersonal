package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehiculo, String> {
    List<Vehiculo> findByEstatVehicle(EstatVehicle estatVehicle);
}
