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
        // 1) Фильтры из других Specification'ов ставим выше или раньше
        //    (вернуть всегда true, чтобы не мешало)
        Predicate alwaysTrue = cb.conjunction();

        // 2) Склеиваем id + seed + snapshot в одну строку
        Expression<String> idStr = root.get("id").as(String.class);
        Expression<String> withSeed = cb.concat(idStr, cb.literal(seed));
        Expression<String> full     = cb.concat(withSeed, cb.literal(snapshot.toString()));

        // 3) Вызываем md5 только от одного параметра — full
        Expression<String> md5expr = cb.function(
                "md5",
                String.class,
                full
        );

        // 4) substr(md5expr,1,16)
        Expression<String> prefix16 = cb.function(
                "substr",
                String.class,
                md5expr,
                cb.literal(1),
                cb.literal(16)
        );

        // 5) склеиваем 'x' + prefix16
        Expression<String> hexLiteral = cb.concat(cb.literal("x"), prefix16);

        // 6) кастим к битовой строке и bigint
        //    К сожалению JPA не умеет напрямую в ::bit(), ::bigint,
        //    поэтому тут тоже можно через функцию или /*numberTemplate*/
        //    Например, оставим просто как строку, а приведём через native:
        //    Ниже — упрощённый пример, но лучше — вар. 2.

        // 7) Вставляем order by
        query.orderBy(
                cb.asc(prefix16),        // временно — смотрим первые 16 символов (hex)
                cb.asc(root.get("id"))
        );

        return alwaysTrue;
    }


}

