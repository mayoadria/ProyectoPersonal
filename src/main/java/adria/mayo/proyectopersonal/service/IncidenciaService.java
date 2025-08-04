package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.entity.Incidencia;
import adria.mayo.proyectopersonal.entity.enums.estatIncidencia.EstatIncidencia;
import adria.mayo.proyectopersonal.repository.IncidenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Incidencia> listaIncidenciasPorVehiculo(String matricula) {
        return incidenciaRepository.vehiclelistIncidencias(matricula);
    }

    public void crearIncidencia(Incidencia incidencia) {
        incidenciaRepository.save(incidencia);
    }

    public Incidencia buscarIncidencia(Long id) {
        return incidenciaRepository.findById(id).orElse(null);
    }

    public void actualizarIncidencia(Long idIncidencia) {
        Incidencia incidencia1 = buscarIncidencia(idIncidencia);
        if (incidencia1 != null) {
            if (incidencia1.getEstatIncidencia() == EstatIncidencia.OBERTA) {
                incidencia1.setEstatIncidencia(EstatIncidencia.EN_CURS);
                incidenciaRepository.save(incidencia1);
            } else if (incidencia1.getEstatIncidencia() == EstatIncidencia.EN_CURS) {
                incidencia1.setEstatIncidencia(EstatIncidencia.TANCADA);
                incidencia1.setDataFinal(LocalDate.now());
                incidenciaRepository.save(incidencia1);
            }

        }

    }
}
