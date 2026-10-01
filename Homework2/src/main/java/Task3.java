import java.util.ArrayList;

public class Task3 {
    public static void main(String[] args) {
        var employees = new ArrayList<Employee>();
        employees.add(new Employee("Джимми", 28, "Маркетинг", 9999999.0));
        employees.add(new Employee("Карл", 27, "Коммерческий", 12345.0));
        employees.add(new Employee("Нолан", 26, "IT", 123456.0));
        employees.add(new Employee("Чендлер", 29, "Производственный", 100500.0));
        employees.add(new Employee("Денис", 67, "Юридический", 10000.0));

        var salaryFilter = 100000.0;
        System.out.println("Имена сотрудников с зарплатой выше " + salaryFilter + ":");
        employees.stream()
                .filter(emp -> emp.getSalary() > salaryFilter)
                .map(Employee::getFullName)
                .forEach(System.out::println);
    }
}
