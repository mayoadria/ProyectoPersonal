package adria.mayo.proyectopersonal.Config;

import adria.mayo.proyectopersonal.Excepciones.Usuari.UsuarioException;
import adria.mayo.proyectopersonal.Excepciones.Vehicle.VehicleException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsuarioException.class)
    public String manejarErroresUsuario(UsuarioException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/admin/listaUsu";
    }

    @ExceptionHandler(VehicleException.class)
    public String manejarErroresVehiculo(VehicleException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/admin/llistaVehiculo";
    }
}
