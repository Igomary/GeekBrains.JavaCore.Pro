package lesson6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class My6AppTest {
    private final My6App app = new My6App();

    @ParameterizedTest
    @MethodSource("SuccessData")
    void comeAfterLast4(Integer[] arr, Integer[] expectedRes) {
        Integer[] result = app.comeAfterLast4(arr);

        Assertions.assertArrayEquals(expectedRes, result);
    }

    private static Stream<Arguments> SuccessData() {
        List<Arguments> arguments = new ArrayList<>(){{
            add(Arguments.arguments(new Integer[]{1, 2, 4, 4, 2, 3, 4, 1, 7}, new Integer[]{1, 7}));
            add(Arguments.arguments(new Integer[]{1, 2, 4, 4, 2, 3, 4, 1, 4}, new Integer[]{}));
            add(Arguments.arguments(new Integer[]{4, 2, 67, 0, 2, 3, 0, 1, 7}, new Integer[]{2, 67, 0, 2, 3, 0, 1, 7}));
            add(Arguments.arguments(new Integer[]{1, 2, 9, 8, 2, 3, 10, 4, 7}, new Integer[]{7}));
        }};

        return arguments.stream();
    }

    @Test
    void comeAfterLast4Exception() {
        Integer[] array = {2, 1, 2, 5, 1, 0};
        Assertions.assertThrows(RuntimeException.class, () -> app.comeAfterLast4(array));
        Assertions.assertThrows(RuntimeException.class, () -> app.comeAfterLast4(new Integer[]{}));
    }

    @ParameterizedTest
    @MethodSource("SuccessData2")
    void if1And4AreIn(Integer[] arr, boolean expectedRes) {
        boolean result = app.if1And4AreIn(arr);

        Assertions.assertEquals(expectedRes, result);
    }

    private static Stream<Arguments> SuccessData2() {
        List<Arguments> arguments = new ArrayList<>(){{
            add(Arguments.arguments(new Integer[]{1,1,1,1,4,1,1,1,4}, true));
            add(Arguments.arguments(new Integer[]{4,4,4,4,4,4,4}, false));
            add(Arguments.arguments(new Integer[]{1,1,1,1,1}, false));
            add(Arguments.arguments(new Integer[]{}, false));
        }};

        return arguments.stream();
    }

}
