package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;

public class TEAMNAMEHERE extends Algo{ // Replace TeamName
    private static String teamName = "TEAMNAMEHERE"; // Replace TeamName

    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(TEAMNAMEHERE.class); // Replace TeamName
    }

    public static String algorithm(Kalah game){
 
   /************************************************
    ************  PLACE ALGORITHM HERE  ************
    ************************************************/

    }
}