import java.util.ArrayList;
import java.util.Scanner;

public class Jason {
	static int [][] board = {{0,3,3,3,3,3,3,0},
		 {0,3,3,3,3,3,3,0}};
	static boolean anotherTurn = false;
	public static void main (String [] args){
		Scanner in = new Scanner(System.in);
		boolean done = false;
		while(!done){
			
			
			System.out.println("Your turn:");
			anotherTurn = true;
			
			while(anotherTurn && !done){
				printBoard(board);
				System.out.println("Go again: ");
				board = playMove(board,in.nextInt());
				done = checkDone(board);
			}
			printBoard(board);
			System.out.println("Swapping board");
			swapBoard();
			System.out.println("Random turn");
			anotherTurn = true;
			while(anotherTurn && !done){
				
				printBoard(board);
				System.out.println("Go again: ");
				board = playMove(board,randomMove());
				done = checkDone(board);
			}
			printBoard(board);
			System.out.println("Swapping board");
			swapBoard();
			
		}
		printBoard(board);
		
		for(int i = 1; i < board[0].length; i++){
			 board[0][0] += board[0][i];
			 board[1][board[0].length - 1] += board[1][i]; 
		}
		
		if(board[0][0] < board[1][board[0].length - 1])
			System.out.println("You win!");
		else if (board[0][0] < board[1][board[0].length - 1])
			System.out.println("Random Wins");
		else
			System.out.println("It's a tie");
		
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
	
	public static boolean checkDone(int [][] board){
		int sum = 0;
		for(int i = 1; i < board[0].length -1; i++){
			sum += board[1][i];
		}
		if(sum == 0)
			return true;
		return false;
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
	
	public static void algorithm(){
		
		
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
	public static int randomMove(){
		ArrayList<Integer> moves = new ArrayList<Integer>();
        
        
        for(int i = 1; i < 6; i++){
            if(board[1][i] > 0)
                moves.add(i);
        }
        int random = (int) (Math.random() * moves.size());
        System.out.println("Random number between 0 and " + moves.size() + " is: " + random);
        
        return moves.get(random);
	}
	
}

