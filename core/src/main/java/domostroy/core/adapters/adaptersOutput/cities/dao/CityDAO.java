package domostroy.core.adapters.adaptersOutput.cities.dao;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDAO extends JpaRepository<CityProjection, Integer> {

}
