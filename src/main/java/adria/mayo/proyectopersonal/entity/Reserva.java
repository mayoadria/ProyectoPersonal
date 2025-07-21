package adria.mayo.proyectopersonal.entity;

import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, name = "fechaInici")
    private LocalDate fechaInici;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, name = "fechaFinal")
    private LocalDate fechaFinal;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false, name = "hora_inici")
    private LocalTime horaInici;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false, name = "hora_fin")
    private LocalTime horaFin;

    @Column(nullable = false, name = "preu_complert")
    private Double preuComplert;

    @Column(nullable = false, name = "estat_reserva")
    @Enumerated(EnumType.STRING)
    private EstatReserva estatReserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuari_dni") // Clau forana apuntant al dni del usuari
    private Usuari usuari;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula_vehiculo", nullable = false)
    private Vehiculo vehiculo;
}
