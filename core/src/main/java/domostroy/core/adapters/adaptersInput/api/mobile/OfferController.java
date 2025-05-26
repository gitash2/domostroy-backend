package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.*;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.FavouriteOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.preview.MyOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.output.calendar.CalendarOutput;
import domostroy.core.adapters.adaptersInput.dto.output.offers.OfferOutput;
import domostroy.core.application.misc.filter.SearchDTO;
import domostroy.core.application.offers.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Tag(name = "Объявления", description = "API мобильного приложения для работы с объявлениями аренды")
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    @Operation(
            summary = "Создать объявление аренды",
            description = "Создание нового объявления аренды"
    )
    public ResponseEntity<CreateOfferResponse> createOffer(
            @RequestPart(name = "metadata") CreateOfferRequest request,
            @RequestPart(name = "file") Collection<MultipartFile> photos,
            @AuthenticationPrincipal UserDetails user) {
        return ok(offerService.createOffer(request, photos, user));
    }

    @PostMapping("/search")
    @Operation(
            summary = "Поиск объявлений аренды",
            description = "Поиск объявлений по заданным параметрам фильтрации"
    )
    public ResponseEntity<OfferOutput> searchOffers(@AuthenticationPrincipal @Nullable UserDetails user, @RequestBody SearchDTO dto) {
        return ok(offerService.search(user, dto));
    }

    @GetMapping("/{offerId}")
    @Operation(
            summary = "Получить детали объявления",
            description = "Получить полную информацию об объявлении по его идентификатору "
    )
    public ResponseEntity<OfferDTO> getOffer(@Nullable @AuthenticationPrincipal UserDetails user, @PathVariable Long offerId) {
        return ok(offerService.getOfferData(user, offerId));
    }

    @DeleteMapping("/{offerId}")
    @Operation(
            summary = "Удалить объявление",
            description = "Удалить существующее объявление по идентификатору "
    )
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myOffers")
    @Operation(
            summary = "Список своих объявлений",
            description = "Получить список объявлений, созданных текущим пользователем."
    )
    public ResponseEntity<Page<MyOfferDTO>> getMyOffers(@PageableDefault(
            page = 0,
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) Pageable pageable, @AuthenticationPrincipal UserDetails user) {
        return ok(offerService.getMyOffers(user.getUsername(), pageable));
    }

    @GetMapping("/favourite")
    @Operation(
            summary = "Список избранных объявлений",
            description = "Получить список объявлений, добавленных текущим пользователем в избранное."
    )
    public ResponseEntity<Page<FavouriteOfferDTO>> getFavouriteOffers(@PageableDefault(
            page = 0,
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) Pageable pageable, @AuthenticationPrincipal UserDetails user) {
        return ok(offerService.getFavouriteOffers(pageable, user.getUsername()));
    }

    @PostMapping("/favourite/{offerId}")
    @Operation(
            summary = "Добавить в избранное",
            description = "Добавить объявление с указанным идентификатором в список избранного текущего пользователя."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addOfferToFavourites(
            @PathVariable Long offerId,
            @RequestParam("isFavourite") boolean isFavourite,
            @AuthenticationPrincipal UserDetails user) {
        offerService.addOfferToFavourites(offerId, user.getUsername(), isFavourite);
    }

    @GetMapping("/calendar/{offerId}")
    public ResponseEntity<CalendarOutput> getCalendar(@PathVariable Long offerId) {
        return ok(offerService.getCalendar(offerId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateOffer(@RequestPart(name = "metadata") UpdateOfferDTO dto,
                            @RequestPart(name = "file") @Nullable Collection<MultipartFile> photos,
                            @AuthenticationPrincipal UserDetails user) {
        offerService.update(dto, photos, user);
    }

    @PostMapping("/calendar")
    @ResponseStatus(HttpStatus.OK)
    public void updateAvailableDates(@RequestBody UpdateAvailableDatesDTO dto) {
        offerService.updateAvailableDates(dto);
    }
}
