package br.com.zup.proposta.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CpfOrCnpjValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfOrCnpj {
    String message() default
            "{br.com.zup.beanvalidation.cpforcnpj}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
