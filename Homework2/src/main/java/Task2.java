import java.util.ArrayList;
import java.util.Scanner;

public class Task2 {
    public static void main(String[] args){
        var scanner = new Scanner(System.in);
        int n;

        do {
            System.out.print("Введите количество элементов в массиве: ");
            n = scanner.nextInt();
        } while (n < 0);

        var numbers = new ArrayList<Double>();
        var rnd = new java.util.Random();
        for (var i = 0; i < n; i++)
            numbers.add(rnd.nextDouble(-1000, 1000));

        System.out.println(numbers);

        for (var i = 1; i < numbers.size(); i++) {
            var key = numbers.get(i);
            var j = i - 1;

            while (j >= 0 && numbers.get(j) > key) {
                numbers.set(j + 1, numbers.get(j));
                j--;
            }

            numbers.set(j + 1, key);
        }

        System.out.println(numbers);
    }
}
