package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
        int port = Integer.parseInt(Args[0]);
        String addr = (Args.length == 2) ? Args[1] : "";
        Server server = new Server(port, addr);

        if (Args.length == 2)
            server.SendPOST(addr, port);
    }
}
