package server;

import com.google.gson.Gson;
import exceptions.ResponseException;
import model.AuthData;
import requests.CreateGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(RegisterRequest registerRequest) throws ResponseException {
        var method = "POST";
        var path = "/user";
        return makeRequest(method, path, registerRequest, null, AuthData.class);
    }

    public void clear() throws ResponseException {
        var method = "DELETE";
        var path = "/db";
        makeRequest(method, path, null, null, null);
    }

    public AuthData login(LoginRequest request) throws ResponseException {
        var method = "POST";
        var path = "/session";
        return makeRequest(method, path, request, null, AuthData.class);
    }

    public void logout(String authenticator) throws ResponseException {
        var method = "DELETE";
        var path = "/session";
        makeRequest(method, path, null, authenticator, null);
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authenticator) throws ResponseException {
        var method = "POST";
        var path = "/game";
        return makeRequest(method, path, request, authenticator, CreateGameResponse.class);
    }


    private <T> T makeRequest(String method, String path, Object request, String authenticator, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", authenticator);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status == 200;
    }


}
