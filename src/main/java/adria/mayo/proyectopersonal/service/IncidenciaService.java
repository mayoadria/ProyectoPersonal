package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Incidencia;
import adria.mayo.proyectopersonal.repository.IncidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }
    public List<Incidencia> listaIncidencias() {
        return incidenciaRepository.findAll();
    }

    public void crearIncidencia(Incidencia incidencia) {
        incidenciaRepository.save(incidencia);
    }
}
