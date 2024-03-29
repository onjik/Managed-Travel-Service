package click.porito.common.util;

import org.springframework.core.NestedRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Mapper<S,T> {

    /**
     * Map source object to scope object
     * @param source source object, nullable
     * @return transformed object, nullable
     */
    public final T map(S source) {
        if (source == null) {
            return null;
        }
        try {
            return mapInternal(source);
        } catch (Exception e){
            throw new MapperException("Error occurred while mapping", e);
        }
    }

    /**
     * Map source object to scope object
     * @param source source object, nullable
     * @return transformed object, never null, but may be empty
     */
    public ArrayList<T> map(List<S> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        return source.stream()
                .map(this::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Map source object to scope object
     * @param source source object, never null
     * @return transformed object, never null
     * @throws IllegalArgumentException if source is null
     */
    protected abstract T mapInternal(S source);

    public static class MapperException extends NestedRuntimeException {

        public MapperException(String msg) {
            super(msg);
        }

        public MapperException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

}
