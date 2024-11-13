package ui;

import client.ChessClient;

public class Repl {
    ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_WHITE +
                "Welcome to the game of Chess! Please register or login");

    }

}
