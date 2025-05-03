package domostroy.core.application.misc.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterSpecificationBuilder<T> {
    private List<SearchCriteria> params = new ArrayList<>();

    public FilterSpecificationBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public FilterSpecificationBuilder<T> with(SearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public FilterSpecificationBuilder<T> withCriteriaFrom(List<SearchCriteria> searchCriteriaList) {
        params.addAll(searchCriteriaList);
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            throw new IllegalStateException("No data to build Specification");
        }

        Specification<T> result = null;

        try {
            for (int i = 0; i < params.size(); i++) {
                SearchCriteria param = params.get(i);
                if (i == 0) {
                    result = new FilterSpecification<>(param);
                } else {
                    result = Specification.where(result)
                            .and(new FilterSpecification<>(param));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return Objects.requireNonNull(result, "Error while building Specification");
    }
}
