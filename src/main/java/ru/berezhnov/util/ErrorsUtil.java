package ru.berezhnov.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorsUtil {
    public static void validateErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorsMsg.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage()).append("\n");
            }
            throw new MeasurementException(errorsMsg.toString());
        }
    }
}
