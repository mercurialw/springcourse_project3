package ru.berezhnov.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.MeasurementDTO;
import ru.berezhnov.dto.SensorDTO;
import ru.berezhnov.models.Measurement;
import ru.berezhnov.models.Sensor;
import ru.berezhnov.services.MeasurementService;
import ru.berezhnov.services.SensorService;
import ru.berezhnov.util.MeasurementDTOValidator;
import ru.berezhnov.util.SensorNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final MeasurementDTOValidator measurementDTOValidator;

    @Autowired
    public MeasurementsController(MeasurementService measurementService, SensorService sensorService,
                                  ModelMapper modelMapper, MeasurementDTOValidator measurementDTOValidator) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.measurementDTOValidator = measurementDTOValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        measurementDTOValidator.validate(measurementDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorsMsg.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage()).append("\n");
            }
            throw new SensorNotFoundException(errorsMsg.toString());
        }
        Sensor sensor = sensorService.findByName(measurementDTO.getSensor().getName())
                .orElseThrow(() -> new SensorNotFoundException("Sensor does not exist"));
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setSensor(sensor);
        measurementService.save(measurement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(SensorNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        MeasurementDTO result = modelMapper.map(measurement, MeasurementDTO.class);
        result.setSensorDTO(modelMapper.map(measurement.getSensor(), SensorDTO.class));
        return result;
    }

    @GetMapping("/rainyDaysCount")
    ResponseEntity<Integer> getRainyDaysCount() {
        int result = measurementService.findAll().stream().filter(Measurement::isRaining)
                .toList().size();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
