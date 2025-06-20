package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.repository.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehiclesRepository vehiclesRepository;

    public void guardarVehiculo(Vehiculo vehiculo) {
        vehiclesRepository.save(vehiculo);
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiclesRepository.findAll();
    }
}
