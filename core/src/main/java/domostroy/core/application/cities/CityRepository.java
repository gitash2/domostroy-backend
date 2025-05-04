package domostroy.core.application.cities;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;

import java.util.List;

public interface CityRepository {
    Long count();
    void save(List<CityProjection> cities);
    String findById(Integer cityId);
    List<CityProjection> findPopular();

    List<CityProjection> findByMatch(String city);
}
