package adria.mayo.proyectopersonal.VehiculosTest;

import adria.mayo.proyectopersonal.Excepciones.Vehicle.ActivarVehiculoException;
import adria.mayo.proyectopersonal.Excepciones.Vehicle.EncontrarVehicleException;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.CaixaCanvis;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Color;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Combustible;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import adria.mayo.proyectopersonal.repository.VehiclesRepository;
import adria.mayo.proyectopersonal.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestVehiclesService {

    @Mock
    private VehiclesRepository vehiclesRepository;

    @InjectMocks
    private VehicleService vehicleService;


    @Test
    public void testFindVehiclesActiu() {

        when(vehiclesRepository.findByEstatVehicle(EstatVehicle.ACTIU))
                .thenReturn(DataVehicles.vehiculos());

        List<Vehiculo> result = vehicleService.listarVehiculosActivos(EstatVehicle.ACTIU);

        assertNotNull(result);
        assertTrue(result.stream().allMatch(v -> v.getEstatVehicle() == EstatVehicle.ACTIU));

        long activosEsperados = DataVehicles.vehiculos().stream()
                .filter(v -> v.getEstatVehicle() == EstatVehicle.ACTIU)
                .count();

        assertEquals(activosEsperados, result.size());

        verify(vehiclesRepository).findByEstatVehicle(EstatVehicle.ACTIU);
    }

    @Test
    public void testFindVehiculo() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();

        when(this.vehiclesRepository.findById(anyString())).thenReturn(Optional.of(vehiculo));
        this.vehicleService.buscarVehiculo(vehiculo.getMatricula());
        assertNotNull(vehiculo);
        assertEquals("3211CBA", vehiculo.getMatricula());
        assertEquals("Corolla", vehiculo.getModel());
        assertEquals(CaixaCanvis.MANUAL, vehiculo.getCaixaCanvis());
        verify(vehiclesRepository).findById(vehiculo.getMatricula());
    }

    @Test
    public void TestfindVehiculoNotFound() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();

        when(this.vehiclesRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EncontrarVehicleException.class, () -> this.vehicleService.buscarVehiculo(vehiculo.getMatricula()));
    }

    @Test
    public void TestCrearVehiculo() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();

        this.vehicleService.guardarVehiculo(vehiculo);

        ArgumentCaptor<Vehiculo> argumentCaptor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiclesRepository).save(any(Vehiculo.class));
        verify(vehiclesRepository).save(argumentCaptor.capture());
        assertEquals("3211CBA", argumentCaptor.getValue().getMatricula());
        assertEquals("Corolla", argumentCaptor.getValue().getModel());
    }


    @Test
    public void testEliminarVehiculo() {
        // Arrange
        Vehiculo vehiculo = DataVehicles.crearVehiculo();

        when(vehiclesRepository.findById(vehiculo.getMatricula()))
                .thenReturn(Optional.of(vehiculo));

        // Act
        vehicleService.guardarVehiculo(vehiculo);
        vehicleService.eliminarVehiculo(vehiculo.getMatricula());

        // Assert
        // Captura del vehículo guardado
        ArgumentCaptor<Vehiculo> vehiculoCaptor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiclesRepository).save(vehiculoCaptor.capture());
        assertEquals(vehiculo.getMatricula(), vehiculoCaptor.getValue().getMatricula());

        // Captura del ID eliminado
        ArgumentCaptor<Vehiculo> deleteCaptor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiclesRepository).delete(deleteCaptor.capture());
        assertEquals(vehiculo.getMatricula(), deleteCaptor.getValue().getMatricula());

    }

    @Test
    public void testEliminarVehiculoEstadoReservado() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.RESERVAT);

        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));
        assertThrows(ActivarVehiculoException.class, () -> vehicleService.eliminarVehiculo(vehiculo.getMatricula()));
    }

    @Test
    public void testEliminarVehiculoEstadoEntregat() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.ENTREGAT);

        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));
        assertThrows(ActivarVehiculoException.class, () -> vehicleService.eliminarVehiculo(vehiculo.getMatricula()));
    }


    @Test
    public void testActualizarVehiculo() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();

        vehiculo.setModel("Test");
        vehiculo.setColor(Color.BLANCPERLAT);
        this.vehicleService.guardarVehiculo(vehiculo);

        ArgumentCaptor<Vehiculo> vehiculoCaptor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiclesRepository).save(vehiculoCaptor.capture());
        assertEquals("Test", vehiculoCaptor.getValue().getModel());
        assertEquals(Color.BLANCPERLAT, vehiculoCaptor.getValue().getColor());
    }


    @Test
    public void testActualizarVehiculoEstatReservat() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.RESERVAT);

        assertThrows(ActivarVehiculoException.class, () -> vehicleService.guardarVehiculo(vehiculo));

        verify(vehiclesRepository, never()).save(any());
    }


    @Test
    public void testActualizarVehiculoEstatEntregat() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.ENTREGAT);

        assertThrows(ActivarVehiculoException.class, () -> vehicleService.guardarVehiculo(vehiculo));

        verify(vehiclesRepository, never()).save(any());
    }


    @Test
    public void testBuscarVehiculosFiltro() {
        Pageable pageable = PageRequest.of(0, 10);

        when(vehiclesRepository.listaFiltrado(
                eq("1234ABC"),
                eq("Toyota"),
                eq(EstatVehicle.ACTIU),
                eq(Combustible.DIESEL),
                eq(CaixaCanvis.MANUAL),
                eq(pageable)
        )).thenReturn(DataVehicles.vehiculosPage());

        // Act
        Page<Vehiculo> resultado = vehicleService.buscarVehiculosFiltro("1234ABC", "Toyota", EstatVehicle.ACTIU,
                Combustible.DIESEL, CaixaCanvis.MANUAL, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("1234ABC", resultado.getContent().get(0).getMatricula());

        // Verificar que se llamó al repositorio con los parámetros correctos
        verify(vehiclesRepository).listaFiltrado(
                eq("1234ABC"),
                eq("Toyota"),
                eq(EstatVehicle.ACTIU),
                eq(Combustible.DIESEL),
                eq(CaixaCanvis.MANUAL),
                eq(pageable)
        );
    }

    @Test
    public void ActivarVehiculo() {
        Vehiculo vehiculo = DataVehicles.crearVehiculoInactivo();
        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));

        this.vehicleService.guardarVehiculo(vehiculo);

        this.vehicleService.activarVehiculo(vehiculo.getMatricula());

        assertNotNull(vehiculo);
        assertEquals(EstatVehicle.ACTIU, vehiculo.getEstatVehicle());

    }


    @Test
    public void DesactivarVehiculo() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));

        this.vehicleService.guardarVehiculo(vehiculo);

        this.vehicleService.activarVehiculo(vehiculo.getMatricula());

        assertNotNull(vehiculo);
        assertEquals(EstatVehicle.INACTIU, vehiculo.getEstatVehicle());

    }
    @Test
    public void DesactivarVehiculoEntregat() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.ENTREGAT);
        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));

        assertThrows(ActivarVehiculoException.class, () -> vehicleService.activarVehiculo(vehiculo.getMatricula()));
        verify(vehiclesRepository, never()).save(any());

    }
    @Test
    public void DesactivarVehiculoReservat() {
        Vehiculo vehiculo = DataVehicles.crearVehiculo();
        vehiculo.setEstatVehicle(EstatVehicle.RESERVAT);
        when(vehiclesRepository.findById(vehiculo.getMatricula())).thenReturn(Optional.of(vehiculo));

        assertThrows(ActivarVehiculoException.class, () -> vehicleService.activarVehiculo(vehiculo.getMatricula()));
        verify(vehiclesRepository, never()).save(any());

    }
}



    
