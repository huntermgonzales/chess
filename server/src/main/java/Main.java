import chess.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memoryDAO.MemoryAuthDAO;
import dataaccess.memoryDAO.MemoryGameDAO;
import dataaccess.memoryDAO.MemoryUserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {

        int port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }


        if (args.length >= 2 && args[1].equals("sql")) {
            //TODO: put sql DAOS to each DAO
        }
        Server server = new Server();
        server.run(port);
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
    }
}