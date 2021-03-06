package io.github.codeutilities.util.socket;

import io.github.codeutilities.util.socket.client.*;
import io.github.codeutilities.util.socket.client.type.*;
import org.glassfish.tyrus.server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public class SocketHandler {
    
    private final ServerSocket server = new ServerSocket(31372);
    public static final Map<String, SocketItem> ITEM_REGISTRY = new HashMap<>();
    public static final List<Client> clients = new ArrayList<>();
    
    public SocketHandler() throws IOException {
        new Thread(() -> {
            System.out.println("Opened socket listener");
            while (true) {
                try {
                    SocketClient client = new SocketClient(server.accept());
                    clients.add(client);
                    System.out.println("Client connected on local server: " + client.getSocket());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Server server = new Server("localhost", 31371, "/codeutilities", ItemWebEndpoint.class);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void init() {
        register(new NbtItem(), new TemplateItem(), new RawTemplateItem());
        
       try {
           new SocketHandler();
       } catch (IOException e) {
           System.out.println("Failed to load socket handler!");
       }
    }
    
    private static void register(SocketItem... items) {
        for (SocketItem item : items) {
            ITEM_REGISTRY.put(item.getIdentifier(), item);
        }
    }
    
}
