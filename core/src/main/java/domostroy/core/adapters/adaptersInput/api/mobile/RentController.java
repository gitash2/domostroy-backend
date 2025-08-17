package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.aggregates.rentRequest.RentRequestStatus;
import domostroy.core.adapters.adaptersInput.dto.input.mobile.rent.CreateRentRequestDTO;
import domostroy.core.adapters.adaptersInput.dto.output.rentRequest.RentInfoDTO;
import domostroy.core.adapters.adaptersInput.dto.output.rentRequest.RentRequestDTO;
import domostroy.core.application.rentRequest.RentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/rent")
@RequiredArgsConstructor
public class RentController {
    private final RentRequestService rentService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@RequestBody CreateRentRequestDTO dto, @AuthenticationPrincipal UserDetails user) {
        rentService.createRentRequest(dto, user);
    }

    @PatchMapping("/{requestId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void changeRequestStatus(@PathVariable("requestId") Long requestId,
                                    @RequestParam("status") RentRequestStatus status) {
        rentService.changeRequestStatus(requestId, status);
    }

    @GetMapping("/incomingRequests")
    public ResponseEntity<Page<RentRequestDTO>> getIncomingRequests(
            @AuthenticationPrincipal UserDetails user,
            @PageableDefault(
                    size      = 10,
                    sort      = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return ok(rentService.getIncomingRequests(user, pageable));
    }

    @GetMapping("/outgoingRequests")
    public ResponseEntity<Page<RentRequestDTO>> getOutgoingRequests(
            @AuthenticationPrincipal UserDetails user,
            @PageableDefault(
                    size      = 10,
                    sort      = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ok(rentService.getOutgoingRequests(user, pageable));
    }

    @GetMapping("/incomingRequest/{requestId}")
    public RentInfoDTO incomingInfo(@PathVariable Long requestId) {
        return rentService.getRequestInfo(requestId, true);
    }

    @GetMapping("/outgoingRequest/{requestId}")
    public RentInfoDTO outgoingInfo(@PathVariable Long requestId) {
        return rentService.getRequestInfo(requestId, false);
    }

    @DeleteMapping("/{rentRequestId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@AuthenticationPrincipal UserDetails user, @PathVariable Long rentRequestId) {
        rentService.deleteRequest(user, rentRequestId);
    }
}
