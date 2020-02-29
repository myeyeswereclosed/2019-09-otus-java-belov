package ru.otus.hw16.frontend_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Client {
    // TODO inject
    private int port = 5555;

    private String host = "localhost";

    private Socket socket;

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private DataOutputStream outToTCP;
    private BufferedReader inFromTCP;

    private PriorityBlockingQueue<String> incomingMessages = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<String> outcomingMessages = new PriorityBlockingQueue<>();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Thread sendDataToTCP = new Thread(){
        public void run(){
            String sentence;

            log.info("Starting Backend -> TCP communication thread");

            while(true) {
                try {
                    sentence = incomingMessages.take();
                    outToTCP.writeBytes(sentence + '\n');
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private Thread getDataFromTCP = new Thread(){
        public void run(){
            log.info("Starting TCP -> Backend communication thread");

            while(true) {
                String response;
                try {
                    response = inFromTCP.readLine();
                    if (response == null)
                        break;
                    outcomingMessages.put(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void start() {
        if (!isStarted.get()) {
            try {
                this.socket = new Socket(host, port);

                isStarted.set(true);

                outToTCP = new DataOutputStream(socket.getOutputStream());
                inFromTCP = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                getDataFromTCP.start();
                sendDataToTCP.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Send messages to Socket.
    public void sendMessage(String message) {
        incomingMessages.put(message);
    }
    //Take Message from Socket
    public String takeMessage() throws InterruptedException {
        return outcomingMessages.take();
    }
}
