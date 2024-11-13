package client;

import exceptions.ResponseException;
import model.AuthData;
import requests.LoginRequest;
import requests.RegisterRequest;
import server.ServerFacade;
import ui.UserStatus;

import java.util.Arrays;

public class ChessClient {
    private final String url;
    private final ServerFacade server;
    private UserStatus status = UserStatus.SIGNED_OUT;
    private String authToken = null;


    public ChessClient(String serverUrl) {
        url = serverUrl;
        server = new ServerFacade(url);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_OUT) {
            throw new ResponseException(400, "You are already signed in");
        }
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            AuthData authData = server.register(new RegisterRequest(username, password, email));
            authToken = authData.authToken();
            status = UserStatus.SIGNED_IN;
            return "Successfully registered\n";
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (status != UserStatus.SIGNED_OUT) {
            throw new ResponseException(400, "You are already signed in");
        }
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            AuthData authData = server.login(new LoginRequest(username, password));
            authToken = authData.authToken();
            status = UserStatus.SIGNED_IN;
            return "Successfully logged in\n";
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String logout() throws ResponseException {
        if (status != UserStatus.SIGNED_OUT && authToken != null) {
            server.logout(authToken);
            authToken = null;
            status = UserStatus.SIGNED_OUT;
            return "Successfully logged out\n";
        }
        throw new ResponseException(400, "Error: you were not logged in");
    }

    public String help() throws ResponseException {
        if (status == UserStatus.SIGNED_OUT) {
            return """
                    To see this menu: "h", "help"
                    To register as a new user: "register" <username> <password> <email>
                    To login as an existing user: "login" <username> <password>
                    To exit the program: "q", "quit"
                    """;
        } else if (status == UserStatus.SIGNED_IN) {
            return """
                    To see this menu: "h", "help"
                    To logout: "logout"
                    To create a game: "create" <game name>
                    To list all created games: "list"
                    To play a game: "play" <game ID> <Black|White>
                    To observe a game: "observe" <game ID>
                    """;
        }
        throw new ResponseException(400, "Something went wrong");
    }



}
