package domostroy.core.application.misc.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

public class FilterSpecification<T> implements Specification<T> {

    private final SearchCriteria searchCriteria;

    public FilterSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String lowerString = searchCriteria.value() != null ?
                searchCriteria.value().toString().toLowerCase(Locale.getDefault()) :
                "";

        Path<?> path = getPath(root, searchCriteria.filterKey());



        switch (searchCriteria.operation()) {
            case "cn": // CONTAINS
                return cb.like(cb.lower(path.as(String.class)), "%" + lowerString + "%");

            case "nc": // DOES_NOT_CONTAIN
                return cb.notLike(cb.lower(path.as(String.class)), "%" + lowerString + "%");

            case "bw": // BEGINS_WITH
                return cb.like(cb.lower(path.as(String.class)), lowerString + "%");

            case "bn": // DOES_NOT_BEGIN_WITH
                return cb.notLike(cb.lower(path.as(String.class)), lowerString + "%");

            case "ew": // ENDS_WITH
                return cb.like(cb.lower(path.as(String.class)), "%" + lowerString);

            case "en": // DOES_NOT_END_WITH
                return cb.notLike(cb.lower(path.as(String.class)), "%" + lowerString);

            case "eq": {
                Object value = searchCriteria.value();
                if (value == null) {
                    return cb.isNull(path);
                }
                if (value instanceof Collection) {
                    CriteriaBuilder.In<Object> inClause = cb.in(path);
                    for (Object item : (Collection<?>) value) {
                        inClause.value(convertValue(path.getJavaType(), item));
                    }
                    return inClause;
                }
                return cb.equal(path, convertValue(path.getJavaType(), value));
            }

            case "ne":
                return cb.notEqual(path, convertValue(path.getJavaType(), searchCriteria.value()));

            case "nu":
                return cb.isNull(path);

            case "nn":
                return cb.isNotNull(path);

            case "gt":
                return compare(cb, (Path<Comparable>) path, CompareOperator.GREATER_THAN);

            case "ge":
                return compare(cb, (Path<Comparable>) path, CompareOperator.GREATER_THAN_OR_EQUAL);

            case "lt":
                return compare(cb, (Path<Comparable>) path, CompareOperator.LESS_THAN);

            case "le":
                return compare(cb, (Path<Comparable>) path, CompareOperator.LESS_THAN_OR_EQUAL);


            default:
                throw new IllegalArgumentException("Operation not supported yet: " + searchCriteria.operation());
        }
    }

    private Predicate compare(CriteriaBuilder cb, Path<Comparable> path, CompareOperator operator) {
        Comparable value = (Comparable) convertValue(path.getJavaType(), searchCriteria.value());

        return switch (operator) {
            case GREATER_THAN -> cb.greaterThan(path, value);
            case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(path, value);
            case LESS_THAN -> cb.lessThan(path, value);
            case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
        };
    }

    private Path<?> getPath(Root<T> root, String key) {
        if (!key.contains(".")) {
            return root.get(key);
        }
        String[] parts = key.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return path;
    }


    private Object convertValue(Class<?> targetType, Object value) {
        if (value == null) return null;

        String stringValue = value.toString();
        try {
            if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(stringValue);
            }
            if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(stringValue);
            }
            if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(stringValue);
            }
            if (targetType == OffsetDateTime.class) {
                return OffsetDateTime.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
            }
            if (targetType == LocalDateTime.class) {
                return LocalDateTime.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
            }
            if (targetType == LocalDate.class) {
                return LocalDate.parse(stringValue, DateTimeFormatter.ISO_DATE);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value conversion for type " + targetType.getSimpleName(), e);
        }
        return value;
    }

    private enum CompareOperator {
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL
    }
}