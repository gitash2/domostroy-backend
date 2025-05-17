package domostroy.core.adapters.adaptersOutput.offerCalendar.dao;

import domostroy.core.adapters.adaptersOutput.offerCalendar.projections.OfferCalendarProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OfferCalendarDAO extends JpaRepository<OfferCalendarProjection, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        WITH
          del_dates AS (
            DELETE FROM rent_request_dates
             WHERE offer_date_id IN (
               SELECT id FROM offer_calendar WHERE offer_id = :offerId
             )
          ),
          del_fav AS (
            DELETE FROM favourites
             WHERE offer_id = :offerId
          )
        DELETE FROM offer_calendar
         WHERE offer_id = :offerId
        """,
            nativeQuery = true)
    void deleteAllByOfferId(Long offerId);

    @Query("""
            
                    select case
                    when (select count(o1)
                          from OfferCalendarProjection o1
                          where o1.offerId = :offerId
                          and o1.date in :dates
                          ) <> :#{#dates.size()} then true
                    when exists (
                    select o2
                    from OfferCalendarProjection o2
                    where o2.offerId = :offerId
                    and o2.isBooked  = true
                    and o2.date in :dates
                    ) then true
                        else false
                    end
            """)
    boolean areDatesBooked(Set<LocalDate> dates, Long offerId);

    @Query("""
                select o
                from OfferCalendarProjection o
                where o.offerId = :offerId
                and o.date in :dates
            """)
    Set<OfferCalendarProjection> getOfferDates(Long offerId, Set<LocalDate> dates);


    List<OfferCalendarProjection> findOfferDatesByOfferId(Long offerId);
}
