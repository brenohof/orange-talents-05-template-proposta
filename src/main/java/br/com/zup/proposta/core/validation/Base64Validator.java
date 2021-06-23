package br.com.zup.proposta.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class Base64Validator implements ConstraintValidator<Base64, String> {

    private static final Pattern BASE64_PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
    private static final String WHITE_SPACE_REGEX = "\\s";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final String sanitized = value.replaceAll(WHITE_SPACE_REGEX,"");
        return BASE64_PATTERN.matcher(sanitized).matches();
    }
}
