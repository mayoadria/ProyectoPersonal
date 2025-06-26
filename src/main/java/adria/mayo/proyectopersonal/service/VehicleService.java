package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.repository.VehiclesRepository;
import adria.mayo.proyectopersonal.security.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Vehiculo> listarVehiculosActivos(EstatVehicle estatVehicle) {
        return vehiclesRepository.findByEstatVehicle(estatVehicle);
    }

    public void eliminarVehiculo(String matricula) {
        Vehiculo vehiculo = buscarVehiculo(matricula)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle no trobat amb matrícula: " + matricula));
        vehiclesRepository.delete(vehiculo);
    }

    public Optional<Vehiculo> buscarVehiculo(String matricula) {
        return vehiclesRepository.findById(matricula);
    }

    public void activarVehiculo(String matricula) {
        Vehiculo vehiculo = buscarVehiculo(matricula).orElseThrow(() -> new EntityNotFoundException("Vehicle no trobat amb matrícula: " + matricula));
        switch (vehiculo.getEstatVehicle()) {
            case ACTIU:
                vehiculo.setEstatVehicle(EstatVehicle.INACTIU);
                vehiclesRepository.save(vehiculo);
                break;

            case INACTIU:
                vehiculo.setEstatVehicle(EstatVehicle.ACTIU);
                vehiclesRepository.save(vehiculo);
                break;
        }
    }
}
