package domostroy.core.adapters.adaptersOutput.offers.dao;

import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferDAO extends JpaRepository<OfferProjection, Long> {
}
