package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.input.offers.CreateOfferRequest;
import domostroy.core.adapters.adaptersInput.dto.input.offers.CreateOfferResponse;
import domostroy.core.adapters.adaptersInput.dto.input.offers.OfferDTO;
import domostroy.core.application.offers.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
@Tag(name = "Объявления", description = "API мобильного приложения для взаимодействия с объявлениями")
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    @Operation(summary = "Создание объявления об аренде" , description = "Создать объявление об аренде")
    public ResponseEntity<CreateOfferResponse> createOffer
            (@RequestPart(name = "metadata") CreateOfferRequest request,
             @RequestPart(name = "file") Collection<MultipartFile> photos,
             @AuthenticationPrincipal UserDetails user) {

        return ok(offerService.createOffer(request, photos, user));
    }

    @GetMapping("/{offerId}")
    @Operation(summary = "Получение данных об объявлении",description = "Получить данные об объявлении")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long offerId) {
        return ok(offerService.getOfferData(offerId));
    }

    /*@PutMapping
    public ResponseEntity<OfferDTO> updateOffer(@RequestBody OfferDTO offer) {
        return ok(offerService.updateOffer(offer));
    }*/

    @DeleteMapping("/{offerId}")
    @Operation(summary = "Удаление объявления", description = "Удалить объявление")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }
}
