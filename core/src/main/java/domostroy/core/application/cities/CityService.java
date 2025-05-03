package domostroy.core.application.cities;

import domostroy.core.adapters.adaptersInput.dto.output.cities.CityOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
