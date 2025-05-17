package domostroy.core.application.cities;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;

import java.util.List;
import java.util.Optional;

public interface CityRepository {
    Long count();
    void save(List<CityProjection> cities);
    CityProjection findById(Integer cityId);
    List<CityProjection> findPopular();

    List<CityProjection> findByMatch(String city);

    List<CityProjection> findAllByIds(List<Integer> ids);
}
