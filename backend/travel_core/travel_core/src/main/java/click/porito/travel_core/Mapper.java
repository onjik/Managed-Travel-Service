package click.porito.travel_core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper<S,T> {

    /**
     * Map source object to target object
     * @param source source object, nullable
     * @return transformed object, nullable
     */
    public final T map(S source) {
        if (source == null) {
            return null;
        }
        return mapInternal(source);
    }

    /**
     * Map source object to target object
     * @param source source object, nullable
     * @return transformed object, nullable
     */
    public ArrayList<T> map(List<S> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map(this::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Map source object to target object
     * @param source source object, never null
     * @return transformed object, never null
     * @throws IllegalArgumentException if source is null
     */
    protected abstract T mapInternal(S source);

}
