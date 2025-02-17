package ru.berezhnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berezhnov.models.Measurement;
import ru.berezhnov.repositories.MeasurementRepository;
import ru.berezhnov.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void add(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorRepository.findByName(measurement.getSensor().getName()).get());
        measurement.setCreationDate(LocalDateTime.now());
    }
}
