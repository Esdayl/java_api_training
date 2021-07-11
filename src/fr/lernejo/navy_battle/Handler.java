package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class Handler implements HttpHandler {
    final UUID id;
    final String url;

    public Handler(UUID id, String url) {
        this.id = id;
        this.url = url;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod()))
        {
            try {
                JSONObject json = new JSONObject();
                json.put("id", id);
                json.put("url", url);
                json.put("message", "May the best code win");
                String body = json.toJSONString();
                Object object;
                object = new JSONParser().parse(new InputStreamReader(exchange.getRequestBody(), "utf-8"));
                final JSONObject obj = (JSONObject) object;

                exchange.getResponseHeaders().set("Accept", "application/json");
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(202, body.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            } catch (ParseException e) {
                final String body = "Bad Request";
                exchange.sendResponseHeaders(400, body.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
                e.printStackTrace();
            }
        }
        else {
            final String body = "Not Found";
            exchange.sendResponseHeaders(404, body.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body.getBytes());
            }
        }
    }
}
