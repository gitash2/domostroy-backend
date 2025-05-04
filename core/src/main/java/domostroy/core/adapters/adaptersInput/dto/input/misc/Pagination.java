package domostroy.core.adapters.adaptersInput.dto.input.misc;

public record Pagination(int page, int size) {
    public Pagination() {
        this(0, 30);
    }

    public boolean isValid() {
        return page >= 0 && size > 0;
    }
}