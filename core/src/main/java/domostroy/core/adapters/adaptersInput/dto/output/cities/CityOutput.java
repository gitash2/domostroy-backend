package domostroy.core.adapters.adaptersInput.dto.output.cities;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;

import java.util.List;

public record CityOutput(
        List<CityProjection> cities
) {
}
