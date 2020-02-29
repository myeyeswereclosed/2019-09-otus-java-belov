package ru.otus.hw16.server;

import java.io.*;
import java.net.Socket;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    private BufferedReader inputStreamReader;
    private Writer outputStreamWriter;
    private final Socket socket;
    private List<WebSocket.Listener> listeners = new ArrayList<>();

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            inputStreamReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStreamWriter = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public InetAddress getAddress() {
//        return socket.getInetAddress();
//    }
//
//    @Override
//    public void send(Object objectToSend) {
//        if (objectToSend instanceof byte[]) {
//            byte[] data = (byte[]) objectToSend;
//            try {
//                outputStream.write(data);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void addListener(Listener listener) {
//        listeners.add(listener);
//    }

    @Override
    public void run() {
        System.out.println("SOCKET STARTS");
        new Thread(() -> {
            while (true) {
                try {
                    var message = inputStreamReader.readLine();

                    System.out.println("MESSAGE " + message + " RECEIVED FROM " + socket.getRemoteSocketAddress());


//                        for (Listener listener : listeners) {
//                            listener.messageReceived(this, bytes);
//                        }
//                    else {
//                        socket.close();
//                        for (Listener listener : listeners) {
//                            listener.disconnected(this);
//                        }
//                        break;
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
//                    for (Listener listener : listeners) {
//                        listener.disconnected(this);
//                    }
                    break;
                }
            }
        }).start();
    }

//    @Override
//    public void close() {
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

