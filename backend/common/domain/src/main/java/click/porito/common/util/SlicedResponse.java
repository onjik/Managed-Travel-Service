package click.porito.common.util;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SlicedResponse<T> {
    private final List<T> content = new ArrayList<>();
    private final PageableRequest pageableRequest;
    private final boolean hasNext;

    public SlicedResponse(List<T> content, PageableRequest pageableRequest, boolean hasNext) {
        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageableRequest, "PageableRequest must not be null");
        this.content.addAll(content);
        this.pageableRequest = pageableRequest;
        this.hasNext = hasNext;
    }

    public int getPageNumber() {
        return pageableRequest.getPageNumber();
    }

    public int getPageSize() {
        return pageableRequest.getPageSize();
    }

    public long getNumberOfElements() {
        return content.size();
    }

    public boolean hasPrevious() {
        return pageableRequest.hasPrevious();
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean isFirst() {
        return !hasPrevious();
    }

    public boolean isLast() {
        return !hasNext;
    }

    public Optional<PageableRequest> nextPageable() {
        if (!hasNext()) {
            return Optional.empty();
        }
        return Optional.of(pageableRequest.next());
    }

    public Optional<PageableRequest> previousPageable() {
        if (!hasPrevious()) {
            return Optional.empty();
        }
        return Optional.of(pageableRequest.previous());
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public PageableRequest getPageableRequest() {
        return pageableRequest;
    }

    public boolean getHasNext() {
        return hasNext;
    }

}
