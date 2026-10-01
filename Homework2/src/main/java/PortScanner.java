import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortScanner implements Task {
    private static final int CONNECT_TIMEOUT_MS = 200;

    private final String host;
    private final int startPort;
    private final int endPort;
    private volatile boolean isRunning = false;

    public PortScanner(String host, int startPort, int endPort) {
        if (host == null || host.isBlank())
            throw new IllegalArgumentException("host must not be blank");
        if (startPort < 1 || endPort > 65535 || startPort > endPort)
            throw new IllegalArgumentException("invalid port range");

        this.host = host;
        this.startPort = startPort;
        this.endPort = endPort;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;

        for (var port = startPort; port <= endPort && isRunning; port++) {
            try (var socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
                System.out.println("Порт " + port + " открыт!");
            } catch (IOException e) {
                // порт закрыт или недоступен
            }
        }
        isRunning = false;
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            System.out.println("Сканирование остановлено.");
        }
    }
}