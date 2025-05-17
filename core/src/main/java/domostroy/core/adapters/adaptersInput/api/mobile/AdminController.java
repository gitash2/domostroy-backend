package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.aggregates.users.domain.RoleModel;
import domostroy.core.application.offers.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final OfferService offerService;

    @DeleteMapping("/{offerId}")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Удалить любое объявление",
            description = "Удалить существующее объявление по идентификатору "
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId);
    }


}
