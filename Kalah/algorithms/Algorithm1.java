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
	/**
	 * Helper method to print the current state of the board
	 * @param board the board to print
	 */
	public static void printBoard (int [][] board){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				System.out.printf("%4d",board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Helper method to swap the opponent and the current players board.
	 */
	public static void swapBoard (){
		int [] temp = new int [board[0].length];
		for(int i = 0; i < temp.length; i++){
			temp[i] = board[0][i];
		}
		for(int i = 0; i < board[0].length; i++){
			board[0][i] = board[1][board[0].length - 1 - i];
			
		}
		for(int i = 0; i < board[0].length; i++){
			board[1][i] = temp[board[0].length-i-1];
		}
		
	}
	/**
	 * Plays a move and returns the new board
	 * @param board the board to play the move on
	 * @param move the index of the move to play
	 * @return the modified board
	 */
	public static int[][] playMove(int [][] board, int move){
		int seeds = board[1][move];
		board[1][move] = 0;
		int currentIndex = move + 1;
		anotherTurn = false;
		boolean right = true; //determines if we are still distributing the seeds in your houses
		while(seeds > 0){
			seeds--;
			if(right){ //add to our side
				board[1][currentIndex] +=1;
				if((currentIndex + 1) % board[0].length != 0)
					currentIndex++;
				else{
					right = false;
					if(seeds == 0)
						anotherTurn = true;
					currentIndex = board[0].length -2;
				}
			}
			else{ //add to the opponents side
				board[0][currentIndex] +=1;
				if(currentIndex > 1)
					currentIndex--;
				else{
					right = true;
					currentIndex = 1;
				}
			}
				
		}
		if(right){ //steal the seeds from the opponent
			if(board[1][currentIndex - 1] == 1 && board[0][currentIndex -1] > 0){
				board[1][board[0].length -1] += board[1][currentIndex - 1];
				board[1][currentIndex -1] = 0;
				board[1][board[0].length -1] += board[0][currentIndex - 1];
				board[0][currentIndex -1] = 0;
			}
		}
		return board;
	}
}
