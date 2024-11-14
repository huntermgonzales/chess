package model;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, chess.ChessGame game) {

    @Override
    public String toString() {
        return  "gameName='" + gameName + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", whiteUsername='" + whiteUsername + '\'';
    }
}
