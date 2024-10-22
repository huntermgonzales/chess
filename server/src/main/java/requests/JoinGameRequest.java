package requests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, int gameID) {
}
