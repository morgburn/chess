package client;

import ui.ChessClient;

public class ClientMain {
    public static void main(String[] args) {
        try {
            new ChessClient(8080).run();
        } catch (Exception e) {
            System.out.print("Unable to start client.");
        }
    }
}
