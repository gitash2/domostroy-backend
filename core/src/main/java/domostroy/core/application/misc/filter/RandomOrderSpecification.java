package domostroy.core.application.misc.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class RandomOrderSpecification<T> implements Specification<T> {
    private final String seed;
    private final Instant snapshot;

    public RandomOrderSpecification(String seed, Instant snapshot) {
        this.seed = seed;
        this.snapshot = snapshot;
    }

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        Predicate alwaysTrue = cb.conjunction();

        Expression<String> idStr = root.get("id").as(String.class);
        Expression<String> withSeed = cb.concat(idStr, cb.literal(seed));
        Expression<String> full     = cb.concat(withSeed, cb.literal(snapshot.toString()));

        Expression<String> md5expr = cb.function(
                "md5",
                String.class,
                full
        );

        Expression<String> prefix16 = cb.function(
                "substr",
                String.class,
                md5expr,
                cb.literal(1),
                cb.literal(16)
        );

        Expression<String> hexLiteral = cb.concat(cb.literal("x"), prefix16);


        query.orderBy(
                cb.asc(prefix16),
                cb.asc(root.get("id"))
        );

        return alwaysTrue;
    }


}

