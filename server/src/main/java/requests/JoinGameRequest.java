package requests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
}
