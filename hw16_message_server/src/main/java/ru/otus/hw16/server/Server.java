package ru.otus.hw16.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String [] args){

        ExecutorService executor = Executors.newFixedThreadPool(5);

        ServerSocket server;

        try{
            server = new ServerSocket(5555);

            System.out.println ("Server started!");

            while(true) {
                try {
                    Socket clientSocket = server.accept();

                    if (clientSocket.isConnected()) {
                        System.out.println("Client connected");

                        executor.execute(new ClientHandler(clientSocket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException el){
            el.printStackTrace();
        }
    }
}
