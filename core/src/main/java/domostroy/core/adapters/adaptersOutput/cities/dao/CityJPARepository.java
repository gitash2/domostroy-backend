package domostroy.core.adapters.adaptersOutput.cities.dao;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import domostroy.core.application.cities.CityRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CityJPARepository implements CityRepository {
    private final CityDAO cityDAO;

    public Long count() {
        return cityDAO.count();
    }

    @Override
    public void save(List<CityProjection> cities) {
        cityDAO.saveAll(cities);
    }

    @Override
    public String findById(Integer cityId) {
        CityProjection city = cityDAO.findById(cityId)
                .orElseThrow(() -> new ObjectNotFoundException("City not found, id: " + cityId));
        return city.getName();
    }

    @Override
    public List<CityProjection> findPopular() {
        return cityDAO.findPopularCities();
    }

    @Override
    public List<CityProjection> findByMatch(String city) {
        return cityDAO.findByMatch(city);
    }


}
