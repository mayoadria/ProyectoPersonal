package adria.mayo.proyectopersonal.validaciones.Vehicle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatriculaValidator implements ConstraintValidator<ValidMatricula, String> {
    // Antiguo formato: XX-1234-YY
    private static final String REGEX_ANTIGUO = "^[A-Z]{1,2}\\d{1,4}-[A-Z]{1,2}$";

    // Nuevo formato desde 2000: 1234-BCD (sin vocales ni Ñ ni Q)
    private static final String REGEX_NUEVO = "^\\d{4}[B-DF-HJ-NP-TV-Z]{3}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        value = value.toUpperCase().trim();

        return value.matches(REGEX_ANTIGUO) || value.matches(REGEX_NUEVO);
    }
}
