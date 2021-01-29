package lesson6;

import java.util.Arrays;

public class My6App {
    public static void main(String[] args) {

    }

    public static Integer[] comeAfterLast4(Integer[] arr) {

        int res = -1;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 4)
                res = i;
        }
        if (res == -1) throw new RuntimeException();

        return Arrays.stream(arr)
                .skip(res+1)
                .toArray(Integer[]::new);

    }

    public static boolean if1And4AreIn(Integer[] arr) {
        boolean ifOne = false;
        boolean ifFour = false;
        for (Integer i : arr) {
            if (i == 1) ifOne = true;
            if (i == 4) ifFour = true;
        }
        return ifOne & ifFour;
    }



}
