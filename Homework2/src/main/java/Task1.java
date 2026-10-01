import java.util.Arrays;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args){
        var scanner = new Scanner(System.in);
        int n;

        do {
            System.out.print("Введите количество элементов в массиве: ");
            n = scanner.nextInt();
        } while (n < 0);

        var numbers = new int[n];
        var rnd = new java.util.Random();
        for (var i = 0; i < n; i++)
            numbers[i] = rnd.nextInt(-1000, 1000);

        var positiveSum = 0;

        for (var num : numbers) {
            if (num > 0)
                positiveSum += num;
        }

        System.out.println(Arrays.toString(numbers));
        System.out.println("Сумма положительных элементов массива: " + positiveSum);
    }
}
