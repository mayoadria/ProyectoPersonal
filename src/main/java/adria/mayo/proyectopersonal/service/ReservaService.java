package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void crearReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }
}
