package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.output.cities.CityOutput;
import domostroy.core.application.cities.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/popular")
    public ResponseEntity<CityOutput> getPopular() {
        return ok(cityService.findPopularCities());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityOutput> getCity(@PathVariable int cityId) {
        return ok(cityService.getCity(cityId));
    }

    @GetMapping
    public ResponseEntity<CityOutput> search(@RequestParam String city) {
        return ok(cityService.search(city));
    }
}
