package adria.mayo.proyectopersonal.dto;

import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.CaixaCanvis;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Combustible;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.EstatVehicle;
import lombok.Data;

@Data
public class VehicleFltroDTO {

    private String matricula;
    private String marca;
    private EstatVehicle estatVehicle;
    private Combustible combustible;
    private CaixaCanvis canvis;
}
