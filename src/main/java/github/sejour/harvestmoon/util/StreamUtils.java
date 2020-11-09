package github.sejour.harvestmoon.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamUtils {
    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return Optional
                .ofNullable(collection)
                .stream()
                .flatMap(Collection::stream);
    }
}
