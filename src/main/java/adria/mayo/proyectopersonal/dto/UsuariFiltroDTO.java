package adria.mayo.proyectopersonal.dto;

import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import lombok.Data;

@Data
public class UsuariFiltroDTO {
    private String dni;
    private String nom;
    private String cognom;
    private String email;
    private String nomUsuari;
    private EstatUsuari estatUsuari;
    private Pais pais;
    private String telf;
    private String codiPostal;
    private String direccio;
    private String poblacio;

}
