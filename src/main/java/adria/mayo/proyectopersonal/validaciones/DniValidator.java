package adria.mayo.proyectopersonal.validaciones;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DniValidator implements ConstraintValidator<ValidDniNie, String> {

    private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        value = value.toUpperCase();

        if (value.matches("^\\d{8}[A-Z]$")) {
            return validarDni(value);
        }

        if (value.matches("^[XYZ]\\d{7}[A-Z]$")) {
            return validarNie(value);
        }

        return false;
    }

    private boolean validarDni(String dni) {
        int numero = Integer.parseInt(dni.substring(0, 8));
        char letraEsperada = LETTERS.charAt(numero % 23);
        return dni.charAt(8) == letraEsperada;
    }

    private boolean validarNie(String nie) {
        char letraInicial = nie.charAt(0);
        String conversion = switch (letraInicial) {
            case 'X' -> "0";
            case 'Y' -> "1";
            case 'Z' -> "2";
            default -> null;
        };

        if (conversion == null) return false;

        String numero = conversion + nie.substring(1, 8);
        char letraEsperada = LETTERS.charAt(Integer.parseInt(numero) % 23);
        return nie.charAt(8) == letraEsperada;
    }
}


