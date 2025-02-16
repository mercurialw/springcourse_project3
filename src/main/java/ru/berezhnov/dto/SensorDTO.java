package ru.berezhnov.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SensorDTO {

    @NotEmpty(message = "Sensor name shouldn't be empty")
    @Size(min = 3, max = 30, message = "Sensor name should contain from 3 to 30 symbols")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
