package adria.mayo.proyectopersonal.validaciones.Vehicle;

import adria.mayo.proyectopersonal.validaciones.Usuari.DniValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatriculaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMatricula {
    String message() default "Format de matricula incorrecte";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
