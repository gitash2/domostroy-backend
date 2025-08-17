package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.users.domain.RoleModel;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.AdminOfferOutput;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.offers.admin.BanOfferDTO;
import domostroy.core.adapters.adaptersInput.dto.output.users.AdminUserDTO;
import domostroy.core.adapters.adaptersOutput.users.projections.User;
import domostroy.core.application.misc.filter.SearchDTO;
import domostroy.core.application.offers.OfferService;
import domostroy.core.application.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final OfferService offerService;
    private final UserService userService;

    @DeleteMapping("offers/{offerId}")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Удалить любое объявление",
            description = "Удалить существующее объявление по идентификатору "
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteOffer(@PathVariable Long offerId) {
        offerService.deleteOffer(offerId);
    }

    @PostMapping("offers/search")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Поиск объявлений аренды",
            description = "Поиск объявлений по заданным параметрам фильтрации"
    )
    public ResponseEntity<AdminOfferOutput> searchOffers(@RequestBody SearchDTO dto, @AuthenticationPrincipal UserDetails user) {
        return ok(offerService.searchAdmin(dto, user));
    }

    @GetMapping("users/search")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(summary = "Поиск пользователей", description = "Поиск по email, имени или фамилии с пагинацией")
    public ResponseEntity<Page<AdminUserDTO>> searchUsers(
            @RequestParam String query,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )Pageable pageable, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.searchUsers(query, pageable, user));
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по ID"
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/offers/ban")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Забанить объявление",
            description = "Блокирует объявление"
    )
    @ResponseStatus(HttpStatus.OK)
    public void banOffer(@RequestBody BanOfferDTO dto) {
        offerService.banOffer(dto);
    }

    @PostMapping("/offers/{offerId}/unban")
    @PreAuthorize(RoleModel.Allowed.ADMIN)
    @Operation(
            summary = "Разблокировать объявление",
            description = "Разблокирует ранее забаненное объявление"
    )
    @ResponseStatus(HttpStatus.OK)
    public void unbanOffer(@PathVariable Long offerId) {
        offerService.unbanOffer(offerId);
    }
}
