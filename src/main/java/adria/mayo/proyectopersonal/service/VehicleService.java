package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.Excepciones.Vehicle.ActivarVehiculoException;
import adria.mayo.proyectopersonal.Excepciones.Vehicle.EncontrarVehicleException;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.CaixaCanvis;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Combustible;
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
        if(vehiculo.getEstatVehicle() == EstatVehicle.RESERVAT || vehiculo.getEstatVehicle() == EstatVehicle.ENTREGAT){
            throw new ActivarVehiculoException("El vehiculo con " + vehiculo.getMatricula() + " no se puede desactivar porque tiene una reserva");
        }
    }

    public List<Vehiculo> listarVehiculosActivos(EstatVehicle estatVehicle) {
        return vehiclesRepository.findByEstatVehicle(estatVehicle);
    }

    public void eliminarVehiculo(String matricula) {
        Vehiculo vehiculo = buscarVehiculo(matricula);
        if(vehiculo.getEstatVehicle() == EstatVehicle.RESERVAT || vehiculo.getEstatVehicle() == EstatVehicle.ENTREGAT){
            throw new ActivarVehiculoException("El vehiculo con " + matricula + " no se puede eliminar porque tiene una reserva");
        }
        vehiclesRepository.delete(vehiculo);
    }

    public Vehiculo buscarVehiculo(String matricula) {
        return vehiclesRepository.findById(matricula).orElseThrow(()->new EncontrarVehicleException("Vehiculo no encontrado: " + matricula));
    }

    public Optional<Vehiculo> buscarVehiculoOptional(String matricula) {
        return vehiclesRepository.findById(matricula);
    }

    public void activarVehiculo(String matricula) {
        Vehiculo vehiculo = buscarVehiculo(matricula);
        if(vehiculo.getEstatVehicle() == EstatVehicle.RESERVAT || vehiculo.getEstatVehicle() == EstatVehicle.ENTREGAT){
            throw new ActivarVehiculoException("El vehiculo con " + matricula + " no se puede desactivar porque tiene una reserva");
        }else {
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

    public List<Vehiculo> buscarVehiculosFiltro(String matricula, String marca,
                                                EstatVehicle estatVehicle, Combustible combustible,
                                                CaixaCanvis canvis) {
        return vehiclesRepository.listaFiltrado(matricula,marca,estatVehicle,
                combustible,canvis);
    }
}
