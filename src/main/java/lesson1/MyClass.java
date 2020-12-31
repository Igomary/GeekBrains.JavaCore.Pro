package lesson1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClass {
    public static void main(String[] args) {

        String strArr[] = new String[]{"qw ", "we ", "er ", "rt "};
        Arrays.stream(strArr).forEach(System.out::print);
        System.out.println();
        swap(strArr,1,2);
        Arrays.stream(strArr).forEach(System.out::print);
        System.out.println();
        Integer intArr[] = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(intArr).forEach(System.out::print);
        System.out.println();
        swap(intArr, 2, 4);
        Arrays.stream(intArr).forEach(System.out::print);
        System.out.println();

        List<String> strList = arrToList(strArr);
        System.out.println(strList.getClass());
        List<Integer> intList = arrToList(intArr);
        System.out.println(intList.getClass());

    }
    //1. Метод, который меняет два элемента массива местами
    public static <T> T[] swap(T[] arr, int a, int b) {
        if (a >= 0 && b >= 0 && a < arr.length && b < arr.length) {
            T temp = arr[a];
            arr[a] = arr[b];
            arr[b] = temp;
        }
        return arr;
    }

    //2. Метод, который преобразует массив в ArrayList;
    public static <T> List<T> arrToList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }
}
