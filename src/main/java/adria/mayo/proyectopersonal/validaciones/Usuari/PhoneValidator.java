package adria.mayo.proyectopersonal.validaciones.Usuari;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidTelf, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) return false;

        // Normalizar (eliminar espacios, guiones, y prefijo internacional)
        phone = phone.replaceAll("[\\s\\-()]", "");
        if (phone.startsWith("+34")) {
            phone = phone.substring(3);
        }

        // Verificar que tenga 9 dígitos y empiece por 6-9
        return phone.matches("^[6-9]\\d{8}$");
    }
}

