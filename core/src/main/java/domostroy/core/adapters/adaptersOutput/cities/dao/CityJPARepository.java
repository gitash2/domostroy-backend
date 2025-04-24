package domostroy.core.adapters.adaptersOutput.cities.dao;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import domostroy.core.application.cities.CityRepository;
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


}
