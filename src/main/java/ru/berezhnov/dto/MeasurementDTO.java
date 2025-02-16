package ru.berezhnov.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import ru.berezhnov.models.Sensor;

public class MeasurementDTO {

    @NotNull(message = "Measurement value shouldn't be empty")
    @DecimalMin(value = "-100", message = "Measurement value shouldn't be below -100")
    @DecimalMax(value = "100", message = "Measurement value shouldn't be above 100")
    private double value;

    @NotNull(message = "Raining status shouldn't be empty")
    private boolean raining;

    @NotNull(message = "Sensor shouldn't be empty")
    private Sensor sensor;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
