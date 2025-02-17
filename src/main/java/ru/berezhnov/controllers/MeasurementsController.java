package ru.berezhnov.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.berezhnov.dto.MeasurementDTO;
import ru.berezhnov.models.Measurement;
import ru.berezhnov.services.MeasurementService;
import ru.berezhnov.services.SensorService;
import ru.berezhnov.util.ErrorsUtil;
import ru.berezhnov.util.MeasurementDTOValidator;
import ru.berezhnov.util.MeasurementException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementDTOValidator measurementDTOValidator;

    @Autowired
    public MeasurementsController(MeasurementService measurementService,
                                  ModelMapper modelMapper, MeasurementDTOValidator measurementDTOValidator) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementDTOValidator = measurementDTOValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        measurementDTOValidator.validate(measurementDTO, bindingResult);
        ErrorsUtil.validateErrors(bindingResult);
        measurementService.add(modelMapper.map(measurementDTO, Measurement.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(MeasurementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @GetMapping("/rainyDaysCount")
    ResponseEntity<Integer> getRainyDaysCount() {
        int result = measurementService.findAll().stream().filter(Measurement::isRaining)
                .toList().size();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
