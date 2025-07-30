package adria.mayo.proyectopersonal.VehiculosTest;

import adria.mayo.proyectopersonal.entity.Vehiculo;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class DataVehicles {

    public static List<Vehiculo> vehiculos(){
        return List.of(
                 new Vehiculo("1234ABC","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                        Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ACTIU,2022,15000,null,
                        null,null,null),
                new Vehiculo("1234ABD","Audi","A5",50.50,200.0,2,30,Places.CINC,
                        Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ACTIU,2022,15000,null,
                        null,null,null),
                new Vehiculo("1234ABD","Mercedes","A1",60.50,200.0,2,30,Places.CINC,
                        Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ACTIU,2022,15000,null,
                        null,null,null),
                new Vehiculo("1234ABD","Seat","Ibiza",30.50,200.0,2,30,Places.CINC,
                        Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.INACTIU,2022,15000,null,
                        null,null,null)
        );

    }

    public static Vehiculo crearVehiculo(){
        return new Vehiculo("3211CBD","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ACTIU,
                2022,15000,null,null,null,null);

    }
    public static Vehiculo crearVehiculoInactivo(){
        return new Vehiculo("3211CBD","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.INACTIU,
                2022,15000,null,null,null,null);

    }
    public static Vehiculo crearVehiculoEntregat(){
        return new Vehiculo("3211CBD","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ENTREGAT,
                2022,15000,null,null,null,null);

    }
    public static Vehiculo crearVehiculoReservat(){
        return new Vehiculo("3211CBD","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.RESERVAT,
                2022,15000,null,null,null,null);

    }

    public static Page<Vehiculo> vehiculosPage(){
            List<Vehiculo> vehiculosList = List.of(
                new Vehiculo("1234ABC","Toyota","Corolla",45.50,200.0,2,30,Places.CINC,
                        Portes.CINC,CaixaCanvis.MANUAL,Marxes.CINC,Combustible.DIESEL,Color.NEGRE,EstatVehicle.ACTIU,2022,15000,null,
                        null,null,null)
        );
        Page<Vehiculo> page = new PageImpl<>(vehiculosList);
return page;
    }


}
