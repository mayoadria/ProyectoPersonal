package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.enums.enumsReserva.EstatReserva;
import adria.mayo.proyectopersonal.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {


    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public void crearReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> buscarReservasPorUsuario(String dni) {
        return reservaRepository.findByDni(dni);
    }

    public Optional<Reserva> trobarReserva(Long idReserva) {
        return reservaRepository.findById(idReserva);
    }

    public Page<Reserva> buscarReservasFiltro(Pageable pageable, String matricula, String dni, EstatReserva estat) {

        return reservaRepository.listaFiltrada(matricula, dni, estat, pageable);

    }

    public Reserva trobarReservaIncidencia (String dni, String matricula, EstatReserva estat) {
        return reservaRepository.findByDniandMatriculaandEstat(dni,matricula,estat);
    }
}
