package domostroy.core.adapters.adaptersOutput.cities.dao;

import domostroy.core.adapters.adaptersOutput.cities.projections.CityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityDAO extends JpaRepository<CityProjection, Integer> {

    @Query("""
        select c
        from CityProjection c
        order by c.id asc
        limit 5
    """)
    List<CityProjection> findPopularCities();


    @Query("""
        select c
        from CityProjection c
        where lower(c.name)
        like lower(concat('%', :city, '%'))
    """)
    List<CityProjection> findByMatch(String city);

    @Query("""
        select c
        from CityProjection c
        where c.id in (:ids)
    """)
    List<CityProjection> findAllByIds(List<Integer> ids);

}
