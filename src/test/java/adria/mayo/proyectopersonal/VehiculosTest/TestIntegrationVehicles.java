package adria.mayo.proyectopersonal.VehiculosTest;

import adria.mayo.proyectopersonal.Excepciones.Vehicle.ActivarVehiculoException;
import adria.mayo.proyectopersonal.Excepciones.Vehicle.EncontrarVehicleException;
import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.repository.VehiclesRepository;
import adria.mayo.proyectopersonal.service.VehicleService;
import adria.mayo.proyectopersonal.validaciones.Vehicle.ValidMatricula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestIntegrationVehicles {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehiclesRepository vehiclesRepository;

    private Vehiculo vehiculo;


    @BeforeEach
    public void setUp() {
        vehiclesRepository.deleteAll();
        vehiculo = new Vehiculo();
        vehiculo.setMatricula("1234DBC");
        vehiculo.setMarca("Toyota");
        vehiculo.setModel("Corolla");
        vehiculo.setPreuDia(45.99);
        vehiculo.setFianca(150.0);
        vehiculo.setDiesLloguerMinim(1);
        vehiculo.setDiesLloguerMaxim(30);
        vehiculo.setPlaces(Places.CINC); // Enum, por ejemplo: CINC (5 plazas)
        vehiculo.setPortes(Portes.CINC); // Enum: QUATRE (4 puertas)
        vehiculo.setCaixaCanvis(CaixaCanvis.MANUAL);
        vehiculo.setMarxes(Marxes.SIS); // Enum: SIS (6 marchas)
        vehiculo.setCombustible(Combustible.DIESEL10E);
        vehiculo.setColor(Color.BLAU);
        vehiculo.setEstatVehicle(EstatVehicle.ACTIU);
        vehiculo.setAnyVehicle(2022);
        vehiculo.setKm(12000);
        vehiculo.setFoto(null);

    }


    @Test
    public void crearVehiculo() {
        vehicleService.guardarVehiculo(vehiculo);
        assertEquals("1234DBC", vehiculo.getMatricula());
        assertEquals("Toyota", vehiculo.getMarca());
    }

    @Test
    public void actualizarVehiculo() {
        vehicleService.guardarVehiculo(vehiculo);
        vehiculo.setModel("pepe");
        vehiculo.setMarca("mercedes");
        vehicleService.actualizarVehiculo(vehiculo);
        assertEquals("mercedes", vehiculo.getMarca());
        assertEquals("pepe", vehiculo.getModel());
    }

    @Test
    public void actualizarVehiculoReservat(){
        vehiculo.setEstatVehicle(EstatVehicle.RESERVAT);
        assertThrows(ActivarVehiculoException.class,()-> vehicleService.actualizarVehiculo(vehiculo));
    }
    @Test
    public void actualizarVehiculoEntregat(){
        vehiculo.setEstatVehicle(EstatVehicle.ENTREGAT);
        assertThrows(ActivarVehiculoException.class,()-> vehicleService.actualizarVehiculo(vehiculo));
    }

    @Test
    public void listarVehiculosActivos(){
        Vehiculo vehiculo2 = DataVehicles.crearVehiculoInactivo();
        vehicleService.guardarVehiculo(vehiculo2);
        vehicleService.guardarVehiculo(vehiculo);

        List<Vehiculo> activos = vehicleService.listarVehiculosActivos(EstatVehicle.ACTIU);
        assertEquals(1,activos.size());
        assertEquals("1234DBC",activos.get(0).getMatricula());
    }

    @Test
    public void findById(){
        vehicleService.guardarVehiculo(vehiculo);
        Vehiculo vehiculoFind = vehicleService.buscarVehiculo(vehiculo.getMatricula());

        assertNotNull(vehiculoFind);
        assertEquals("1234DBC",vehiculoFind.getMatricula());
        assertEquals("Toyota",vehiculoFind.getMarca());
    }

    @Test
    public void findByIdNotFound(){
        Vehiculo vehiculo1 = new Vehiculo();
        vehiculo1.setMatricula("1238DBC");
        assertThrows(EncontrarVehicleException.class, ()->vehicleService.buscarVehiculo(vehiculo1.getMatricula()));
    }

    @Test
    public void findByIdOptional(){
        vehicleService.guardarVehiculo(vehiculo);
        vehicleService.buscarVehiculoOptional(vehiculo.getMatricula());
        assertNotNull(vehiculo);
        assertEquals("1234DBC",vehiculo.getMatricula());
        assertEquals("Toyota",vehiculo.getMarca());
    }

    @Test
    public void eliminarVehiculo(){
        vehicleService.guardarVehiculo(vehiculo);
        vehicleService.eliminarVehiculo(vehiculo.getMatricula());
    }

    @Test
    public void eliminarVehiculoNotFound(){
        assertThrows(EncontrarVehicleException.class, ()->vehicleService.eliminarVehiculo("1234"));
    }

    @Test
    public void eliminarVehiculoReservat(){
        Vehiculo vehiculo2 = DataVehicles.crearVehiculoReservat();
        vehicleService.guardarVehiculo(vehiculo2);
        assertThrows(ActivarVehiculoException.class, ()->vehicleService.eliminarVehiculo(vehiculo2.getMatricula()));
    }

    @Test
    public void eliminarVehiculoEntregat(){
        Vehiculo vehiculo2 = DataVehicles.crearVehiculoEntregat();
        vehicleService.guardarVehiculo(vehiculo2);
        assertThrows(ActivarVehiculoException.class, ()->vehicleService.eliminarVehiculo(vehiculo2.getMatricula()));
    }


    @Test
    public void activarVehiculo(){
        Vehiculo vehiculo1 = DataVehicles.crearVehiculoInactivo();

        vehicleService.guardarVehiculo(vehiculo1);
        vehicleService.activarVehiculo(vehiculo1.getMatricula());
        vehicleService.guardarVehiculo(vehiculo1);
        assertEquals(EstatVehicle.INACTIU, vehiculo1.getEstatVehicle());
    }

    @Test
    public void desactivarVehiculo(){
        Vehiculo vehiculo1 = DataVehicles.crearVehiculo();

        vehicleService.guardarVehiculo(vehiculo1);
        vehicleService.activarVehiculo(vehiculo1.getMatricula());
        vehicleService.guardarVehiculo(vehiculo1);
        assertEquals(EstatVehicle.ACTIU, vehiculo1.getEstatVehicle());
    }

    @Test
    public void desactivarVehiculoReservat(){
        Vehiculo vehiculo1 = DataVehicles.crearVehiculoReservat();
        vehicleService.guardarVehiculo(vehiculo1);
        assertThrows(ActivarVehiculoException.class,()->vehicleService.activarVehiculo(vehiculo1.getMatricula()));
    }

    @Test
    public void desactivarVehiculoEntregat(){
        Vehiculo vehiculo1 = DataVehicles.crearVehiculoEntregat();
        vehicleService.guardarVehiculo(vehiculo1);
        assertThrows(ActivarVehiculoException.class,()->vehicleService.activarVehiculo(vehiculo1.getMatricula()));
    }


    @Test
    public void ListaPage(){
        vehicleService.guardarVehiculo(vehiculo);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehiculo> resultado = vehicleService.buscarVehiculosFiltro(
                "1234DBC",
                "Toyota",
                null,
                null,
                null,
                pageable
        );
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "La búsqueda debería retornar al menos un vehiculo");
        assertEquals("Toyota", resultado.getContent().get(0).getMarca());
    }



}
