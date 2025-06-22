package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.EstatUsuari;
import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.Pais;
import adria.mayo.proyectopersonal.entity.enums.enumsUsuario.Rol;
import jakarta.persistence.*;
import lombok.*;
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
    private String dni;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String cognoms;

    @Column(nullable = false)
    private String numContacte;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasenya;

    @Column(nullable = false)
    private String nomUsuari;

    @Column(nullable = false)
    private Pais pais;

    @Column(nullable = false)
    private Long codiPostal;

    @Column(nullable = false)
    private String poblacio;

    @Column(nullable = false)
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
