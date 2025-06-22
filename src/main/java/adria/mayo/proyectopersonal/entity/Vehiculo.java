package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.enumsVehiculo.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    private String matricula;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuari_dni", nullable = false)
    private Usuari creador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Reserva> reserves;


}
