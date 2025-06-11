package adria.mayo.proyectopersonal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String nomPersonaContacte;

    @Column(nullable = false)
    private String telefonPersonaContacte;

    @Column
    private String email;

    @Column
    private String observacions;

    @Lob
    private String foto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuari_dni", nullable = false)
    private Usuari usuari;

    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vehiculo> vehicles;

}
