package adria.mayo.proyectopersonal.repository;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.CaixaCanvis;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Combustible;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.net.CacheRequest;
import java.util.List;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehiculo, String> {
    List<Vehiculo> findByEstatVehicle(EstatVehicle estatVehicle);


    @Query("SELECT v FROM Vehiculo v " +
            "WHERE (:matricula is null or lower(v.matricula) like lower(concat('%', :matricula ,'%') ) )" +
            "and (:marca is null or lower(v.marca) like lower(concat('%',:marca ,'%') ))" +
            "and(:estatVehicle is null or  :estatVehicle = v.estatVehicle) " +
            "and(:combustible is null or :combustible = v.combustible)" +
            "and(:canvis is null or :canvis = v.caixaCanvis)")
    List<Vehiculo> listaFiltrado(
            @Param("matricula") String matricula,
            @Param("marca") String marca,
            @Param("estatVehicle") EstatVehicle estatVehicle,
            @Param("combustible") Combustible combustible,
            @Param("canvis") CaixaCanvis caixaCanvis
    );
}
