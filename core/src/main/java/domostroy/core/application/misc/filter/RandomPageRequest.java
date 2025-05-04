package domostroy.core.application.misc.filter;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;

@Getter
public class RandomPageRequest extends PageRequest {
    private final String seed;
    private final Instant snapshot;

    protected RandomPageRequest(int page, int size, Sort sort, String seed, Instant snapshot) {
        super(page, size, sort);
        this.seed = seed;
        this.snapshot = snapshot;
    }

    public static RandomPageRequest of(int page, int size, Sort sort, String seed, Instant snapshot) {
        return new RandomPageRequest(page, size, sort, seed, snapshot);
    }
}
