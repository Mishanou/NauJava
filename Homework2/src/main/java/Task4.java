import java.net.URI;
import java.net.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Task4 {
    public static void main(String[] args) throws Exception {
        try (var client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/get"))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            var mapper = new ObjectMapper();
            var rootNode = mapper.readTree(response.body());
            var host = rootNode.path("headers").path("Host").asText();

            System.out.println(host);
        }
    }
}
