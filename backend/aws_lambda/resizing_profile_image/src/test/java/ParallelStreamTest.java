import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamTest {

    @Test
    void test() {
        List<Integer> list = IntStream.range(0, 30).boxed().collect(Collectors.toList());
        List<String> collected = list.parallelStream()
                .map(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread() + " " + i);
                    if (i % 2 == 0) {
                        return null;
                    }
                    return i + " processed";
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println(collected);
        System.out.println("Done");
    }

}
