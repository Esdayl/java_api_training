package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public Server(int port, String addr) {

        final String url = "http://localhost:" + port;
        final UUID id = UUID.randomUUID();
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            server.setExecutor(executor);

            Handler handler = new Handler(id, url);
            CallHandler callHandler = new CallHandler();

            server.createContext("/ping", callHandler);
            server.createContext("/api/game/start", handler);
            server.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendPOST(String addr, int port) {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(addr + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"1\", \"url\":\"http://localhost:"
                + port + "\", \"message\":\"hello\"}")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
