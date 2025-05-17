package domostroy.core.application.cities;

import domostroy.core.adapters.adaptersInput.dto.output.cities.CityOutput;
import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public CityOutput findPopularCities() {
        return new CityOutput(cityRepository.findPopular());

    }

    public CityOutput search(String city) {
        return new CityOutput(cityRepository.findByMatch(city));
    }

    public CityOutput getCity(Integer cityId) {
        CityProjection city = cityRepository
                .findById(cityId);
        return new CityOutput(Collections.singletonList(city));
    }
}
