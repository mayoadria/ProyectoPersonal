package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import adria.mayo.proyectopersonal.validaciones.Usuari.ValidDniNie;
import adria.mayo.proyectopersonal.validaciones.Usuari.ValidTelf;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuari implements UserDetails {

    @Id
    @NotEmpty(message = "El dni no pot estar buït")
    @ValidDniNie
    private String dni;

    @Column(nullable = false)
    @NotEmpty(message = "El nom no pot estar buït")
    private String nom;

    @Column(nullable = false)
    @NotEmpty(message = "El cognom no pot estar buït")
    private String cognoms;

    @Column(nullable = false)
    @ValidTelf
    private String numContacte;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "El email no pot estar buït")
    @Email(message = "Format del correu electronìc no vàlid")
    private String email;

    @Column(nullable = false)
    private String contrasenya;

    @Column(nullable = false)
    private String nomUsuari;

    @Column(nullable = false)
    private Pais pais;

    @Column(nullable = false)
    @Size(min = 5,max = 5, message = "El codi postal a de tenir 5 digits")
    private String codiPostal;

    @Column(nullable = false)
    @NotEmpty(message = "La població no pot estar buïda")
    private String poblacio;

    @Column(nullable = false)
    @NotEmpty(message = "La direcció no pot estar buïda")
    private String direccio;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private EstatUsuari estat;

    @Lob
    private String foto;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Reserva> reserves;

    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vehiculo> vehicles;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Local> locals;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + rol.name());
    }


    @Override
    public String getPassword() {
        return contrasenya;
    }

    @Override
    public String getUsername() {
        return nomUsuari;
    }
}
