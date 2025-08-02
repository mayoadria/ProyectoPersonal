package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.estatIncidencia.EstatIncidencia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;

    @Column(nullable = false, name = "estat_incidencia")
    @Enumerated(EnumType.STRING)
    private EstatIncidencia estatIncidencia;

    @Column(nullable = false)
    private String titol;

    @Column(nullable = false)
    private String motiu;


    @Column(nullable = true)
    private Double cost;

    @Column(nullable = false, name = "data_inici")
    private LocalDate dataInici;


    @Column(nullable = true, name = "data_final")
    private LocalDate dataFinal;


    @ManyToOne
    @JoinColumn(name = "vehicle", referencedColumnName = "matricula")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Vehiculo vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuari_dni")
    private Usuari usuari;

}
