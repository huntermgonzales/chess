package ui;

import client.ChessClient;
import exceptions.ResponseException;

import java.util.Scanner;

public class Repl {
    ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_TEXT_COLOR_WHITE +
                "Welcome to the game of Chess! Please register or login");
        try {
            System.out.print(client.help());
        } catch (ResponseException e) {
            var msg = e.toString();
            System.out.println(msg);
        }

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg + "\n");
            }
        }
        System.out.println();

    }

    private void printPrompt() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + ">>> " + EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
    }

}
