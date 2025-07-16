package adria.mayo.proyectopersonal.validaciones.Usuari;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DniValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDniNie {
    String message() default "Format de DNI o NIE incorrecte";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}