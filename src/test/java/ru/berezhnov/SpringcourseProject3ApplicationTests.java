package ru.berezhnov;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import ru.berezhnov.dto.MeasurementDTO;
import ru.berezhnov.dto.SensorDTO;

@SpringBootTest
public class SpringcourseProject3ApplicationTests {

	private final RestTemplate restTemplate;

	@Autowired
	public SpringcourseProject3ApplicationTests(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Test
	void contextLoads() {
		for (int i = 0; i < 1000; i++) {
			MeasurementDTO measurementDTO = new MeasurementDTO((Math.random() - 0.5) * 200,
					Math.random() > 0.5, new SensorDTO("test sensor"));
			restTemplate.postForObject("https://localhost:8080/measurements/add", measurementDTO, String.class);
		}
	}

}
