package ru.berezhnov.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.berezhnov.dto.SensorDTO;
import ru.berezhnov.services.SensorService;

@Component
public class SensorDTOValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorDTOValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
    return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;
        String sensorName = sensorDTO.getName();
        if(sensorService.findByName(sensorName).isPresent()) {
            errors.rejectValue("name", "", "Sensor with name " + sensorName + " already exists");
        }
    }
}
