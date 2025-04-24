package domostroy.core.application.offerCalendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferCalendarService {
    private final OfferCalendarRepository offerCalendarRepository;
}
