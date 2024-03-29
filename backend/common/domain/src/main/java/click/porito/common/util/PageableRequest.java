package click.porito.common.util;

import lombok.Getter;

@Getter
public class PageableRequest {
    private final int pageNumber;
    private final int pageSize;

    public PageableRequest(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public long getOffset() {
        return (long) pageNumber * (long) pageSize;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    public PageableRequest previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public PageableRequest next() {
        return new PageableRequest(pageNumber + 1, pageSize);
    }

    public PageableRequest previous() {
        return hasPrevious() ? new PageableRequest(pageNumber - 1, pageSize) : this;
    }

    public PageableRequest first() {
        return new PageableRequest(0, pageSize);
    }

}
