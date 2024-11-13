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
                default -> throw new ResponseException(400, "invalid code");
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3 && status == UserStatus.SIGNED_OUT) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            AuthData authData = server.register(new RegisterRequest(username, password, email));
            authToken = authData.authToken();
            return "Successfully registered\n";
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2 && status == UserStatus.SIGNED_OUT) {
            String username = params[0];
            String password = params[1];
            AuthData authData = server.login(new LoginRequest(username, password));
            authToken = authData.authToken();
            return "Successfully logged in\n";
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }



}
