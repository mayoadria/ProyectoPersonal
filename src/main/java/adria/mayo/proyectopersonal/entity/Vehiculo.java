package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import adria.mayo.proyectopersonal.validaciones.Vehicle.ValidMatricula;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @ValidMatricula
    private String matricula;

    @Column(nullable = false)
    @NotEmpty(message = "La marca no pot estar buïda")
    private String marca;

    @Column(nullable = false)
    @NotEmpty(message = "El model no pot estar buït")
    private String model;

    @Column(nullable = false, name = "preu_dia")
    private Double preuDia;

    @Column(nullable = false)
    private Double fianca;

    @Column(nullable = false, name = "dies_lloguer_minim")
    private int diesLloguerMinim;

    @Column(nullable = false, name = "dies_lloguer_maxim")
    private int diesLloguerMaxim;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Places places;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Portes portes;

    @Column(nullable = false, name = "caixa_canvis")
    @Enumerated(EnumType.STRING)
    private CaixaCanvis caixaCanvis;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Marxes marxes;

    @Enumerated(EnumType.STRING)
    @Column(name = "combustible")
    private Combustible combustible;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "estat_vehicle")
    private EstatVehicle estatVehicle;

    @Column(nullable = true, name = "any_vehicle")
    private int anyVehicle;

    @Column(nullable = true)
    private int km;

    @Lob
    private String foto;

    @ManyToOne(optional = true)
    @JoinColumn(name = "usuari_dni", nullable = true)
    private Usuari creador;

    @ManyToOne(optional = true)
    @JoinColumn(name = "local_id", nullable = true)
    private Local local;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Reserva> reserves;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Incidencia> incidencies;


}
