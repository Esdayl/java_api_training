package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] Args) {
        if (Args.length == 0) {
            System.err.println("You must enter a port number");
            return;
        }
        if (Args.length > 2) {
            System.err.println("Too much arguments");
            return;
        }
        final int port = Integer.parseInt(Args[0]);
        final String addr = (Args.length == 2) ? Args[1] : "";
        final UUID id = UUID.randomUUID();
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            server.setExecutor(executor);

            // CallHandler
            final HttpHandler CallHandler = new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    final String body = "Hello";
                    exchange.sendResponseHeaders(200, body.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(body.getBytes());
                    }
                }
            };

            // POSTHandler
            final HttpHandler POSTHandler = new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    try {
                        final String msg = "May the best code win";
                        final String url = "http://localhost:" + port;
                        final JSONObject body = new JSONObject();
                        body.put("id", id);
                        body.put("url", url);
                        body.put("message", msg);
                        final JSONObject json = (JSONObject) new JSONParser().parse(exchange.getResponseBody().toString());
                        final String url_res = json.get("url").toString();

                        exchange.sendResponseHeaders(202, body.toJSONString().length());
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(body.toJSONString().getBytes());
                        }
                    } catch (ParseException e) {
                        final String body = "Bad Request";
                        exchange.sendResponseHeaders(400, body.length());
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(body.getBytes());
                        }
                    }
                }
            };

            server.createContext("/ping", CallHandler);
            server.createContext("/api/game/start", POSTHandler);
            server.start();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
