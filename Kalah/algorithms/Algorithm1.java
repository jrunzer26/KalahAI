package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.Scanner;
import java.util.ArrayList;

public class Algorithm1 extends Algo{ // Replace TeamName
    private static String teamName = "Algorithm1"; // Replace TeamName

    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(Algorithm1.class); // Replace TeamName
    }

    public static String algorithm(Kalah game){  
   
   /**************************************************
    *************  PLACE ALGORITHM HERE  *************
    **************************************************/
        int [][] board = game.getBoard();
        ArrayList<String> moves = new ArrayList<String>();
        
        
        for(int i = 1; i < 7; i++){
            if(board[1][i] > 0)
                moves.add(Integer.toString(i));
        }
        int random = (int) (Math.random() * moves.size());
        System.out.println("Random number between 0 and " + moves.size() + " is: " + random);
        
        return moves.get(random);
    }
}
