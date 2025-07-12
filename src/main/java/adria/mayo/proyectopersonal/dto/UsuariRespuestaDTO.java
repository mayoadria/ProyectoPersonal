package adria.mayo.proyectopersonal.dto;

import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UsuariRespuestaDTO {
    private String dni;
    private String nom;
    private String cognoms;
    private String email;
    private String nomUsuari;
    private EstatUsuari estat;
    private Pais pais;
    private String numContacte;
    private String codiPostal;
    private String direccio;
    private String poblacio;
    private Rol rol;
}
