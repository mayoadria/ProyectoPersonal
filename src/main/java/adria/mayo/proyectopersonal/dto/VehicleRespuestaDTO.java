package adria.mayo.proyectopersonal.dto;

import adria.mayo.proyectopersonal.entity.Usuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleRespuestaDTO {
    private String matricula;
    private String marca;
    private String model;
    private Double preuDia;
    private Double fianca;
    private int diesLloguerMinim;
    private int diesLloguerMaxim;
    private Places places;
    private Portes portes;
    private CaixaCanvis caixaCanvis;
    private Marxes marxes;
    private Combustible combustible;
    private Color color;
    private EstatVehicle estatVehicle;
    private int anyVehicle;
    private int km;
    private String foto;
    private UsuariRespuestaDTO creador;
}
