public class Task5 {
    public static void main(String[] args) throws InterruptedException  {
        var scanner = new PortScanner("127.0.0.1", 1, 10000);

        var thread = new Thread(scanner::start, "scanner-thread");
        thread.start();

        Thread.sleep(5000);
        scanner.stop();
        thread.join();
        System.out.println("Демонстрация завершена.");
    }
}
