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

        switch (searchCriteria.operation()) {
            case "cn": // CONTAINS
                return cb.like(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        "%" + lowerString + "%"
                );

            case "nc": // DOES_NOT_CONTAIN
                return cb.notLike(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        "%" + lowerString + "%"
                );

            case "bw": // BEGINS_WITH
                return cb.like(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        lowerString + "%"
                );

            case "bn": // DOES_NOT_BEGIN_WITH
                return cb.notLike(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        lowerString + "%"
                );

            case "ew": // ENDS_WITH
                return cb.like(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        "%" + lowerString
                );

            case "en": // DOES_NOT_END_WITH
                return cb.notLike(
                        cb.lower(root.get(searchCriteria.filterKey())),
                        "%" + lowerString
                );

            case "eq": { // EQUAL
                Path<Object> path = root.get(searchCriteria.filterKey());
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

            case "ne": { // NOT_EQUAL
                Path<Object> path = root.get(searchCriteria.filterKey());
                return cb.notEqual(
                        path,
                        convertValue(path.getJavaType(), searchCriteria.value())
                );
            }

            case "nu": // IS_NULL
                return cb.isNull(root.get(searchCriteria.filterKey()));

            case "nn": // NOT_NULL
                return cb.isNotNull(root.get(searchCriteria.filterKey()));

            case "gt": // GREATER_THAN
                return compare(cb, root, CompareOperator.GREATER_THAN);

            case "ge": // GREATER_THAN_EQUAL
                return compare(cb, root, CompareOperator.GREATER_THAN_OR_EQUAL);

            case "lt": // LESS_THAN
                return compare(cb, root, CompareOperator.LESS_THAN);

            case "le": // LESS_THAN_EQUAL
                return compare(cb, root, CompareOperator.LESS_THAN_OR_EQUAL);

            default:
                throw new IllegalArgumentException("Operation not supported yet: " + searchCriteria.operation());
        }
    }

    private Predicate compare(CriteriaBuilder cb, Root<T> root, CompareOperator operator) {
        Path<Comparable> path = root.get(searchCriteria.filterKey());
        Comparable value = (Comparable) convertValue(
                path.getJavaType(),
                searchCriteria.value()
        );

        return switch (operator) {
            case GREATER_THAN -> cb.greaterThan(path, value);
            case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(path, value);
            case LESS_THAN -> cb.lessThan(path, value);
            case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
        };
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