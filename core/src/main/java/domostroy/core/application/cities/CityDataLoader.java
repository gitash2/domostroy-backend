package domostroy.core.application.cities;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityDataLoader implements CommandLineRunner {
    private final CityRepository cityRepository;
    @Override
    public void run(String... args) throws Exception {
        if (cityRepository.count() > 0) {
            return;
        }

        ClassPathResource classPathResource = new ClassPathResource("cities.csv");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;

            List<CityProjection> cities = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String id = parts[0].trim();
                String name = parts[1].trim();
                cities.add(new CityProjection(Integer.valueOf(id), name));
            }
            cityRepository.save(cities);
        }
    }
}
