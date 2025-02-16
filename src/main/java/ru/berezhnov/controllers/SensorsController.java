package ru.berezhnov.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.SensorDTO;
import ru.berezhnov.models.Sensor;
import ru.berezhnov.services.SensorService;
import ru.berezhnov.util.SensorDTOValidator;
import ru.berezhnov.util.SensorNotCreatedException;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorService sensorService;
    private final SensorDTOValidator sensorDTOValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorService sensorService, SensorDTOValidator sensorDTOValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorDTOValidator = sensorDTOValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                        BindingResult bindingResult) {
        sensorDTOValidator.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorsMsg.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage()).append("\n");
            }
            throw new SensorNotCreatedException(errorsMsg.toString());
        }
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        sensorService.save(sensor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(SensorNotCreatedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
