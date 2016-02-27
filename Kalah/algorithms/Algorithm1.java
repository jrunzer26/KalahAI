package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.ArrayList;

public class Algorithm1 extends Algo{ // Replace TeamName
    private static String teamName = "Algorithm1"; // Replace TeamName
	static boolean freeMove = false;
	
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
		printBoard(board);
		int move = findBestMove(board);
		System.out.println("You moved: " + move);
        return String.valueOf(move);
    }
    
	/**
	 * Helper method to print the current state of the board
	 * 
	 * @param board
	 *            the board to print
	 */
	public static void printBoard(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				System.out.printf("%4d", board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Helper method to swap the opponent and the current players board.
	 */
	public static int[][] swapBoard(int[][] board) {
		int[] temp = new int[board[0].length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = board[0][i];
		}
		for (int i = 0; i < board[0].length; i++) {
			board[0][i] = board[1][board[0].length - 1 - i];

		}
		for (int i = 0; i < board[0].length; i++) {
			board[1][i] = temp[board[0].length - i - 1];
		}
		return board;
	}
	
	/**
	 * An amazing Kalah algorithm :D
	 */
	public static int findBestMove(int[][] board) {
		ArrayList<Integer> moves = findMoves(board);
		ArrayList<Integer> score = new ArrayList<Integer>();

		for (int move : moves) {
			int[][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			score.add(heuristic(boardCopy, move));
		}
		int max = 0;
		int index = 0;
		for (int i = 0; i < score.size(); i++) {
			if (score.get(i) > max) {
				max = score.get(i);
				index = i;
			}
		}
		return moves.get(index);
	}
	
	public static boolean checkDone(int[][] board) {
		for (int i = 0; i < 7; i++) {
			if (board[1][i] > 0)
				return true;
		}
		return false;
	}
	
	public static int[][] findWorstScenerio(int[][] board) {
		// play all free moves to make sure the spots are as open as possible
		for (int i = board[0].length - 2; i > 0; i++) {
			if(board[1][i] == board[0].length - 1 - i){
				board = playMove(board,i);
			} else {
				break;
			}
		}
		int max = 0;
		int move = 0;
		ArrayList<Integer> moves = findMoves(board);
		for (int k = 0; k < moves.size(); k++) {
			int[][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			boardCopy = playMove(boardCopy, moves.get(k));
			if (boardCopy[1][boardCopy.length - 1] > max) {
				max = boardCopy[1][boardCopy.length - 1];
				move = moves.get(k);
			}
		}
		if (moves.size() > 0)
			board = playMove(board, move);
		return board;
	}
	
	public static int heuristic(int[][] board, int move) {
		int[][] newBoard = playMove(board, move);
		int value = 0;
		if (freeMove && !checkDone(newBoard)) {
			freeMove = false;
			System.out.println("Move: " + move);
			move = findBestMove(newBoard);
			System.out.println("Sub-move: " + move);
			return 10 + heuristic(newBoard, move);
		} else {
			board = swapBoard(findWorstScenerio(swapBoard(board)));
			for (int i = 1; i < 7; i++) {
				value += board[1][i];
			}
			value += 2 * newBoard[1][7];
		}
		System.out.println("move: " + move);
		System.out.println("heuristic: " + value + "\n");
		return value;
	}
	
	public static ArrayList<Integer> findMoves(int[][] board) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			if (board[1][i] > 0)
				moves.add(i);
		}
		return moves;
	}
	
	/**
	 * Plays a move and returns the new board
	 * 
	 * @param board
	 *            the board to play the move on
	 * @param move
	 *            the index of the move to play
	 * @return the modified board
	 */
	public static int[][] playMove(int[][] board, int move) {
		int seeds = board[1][move];
		board[1][move] = 0;
		int currentIndex = move + 1;
		freeMove = false;

		boolean right = true; // determines if we are still distributing the
								// seeds in your houses
		while (seeds > 0) {
			seeds--;
			if (right) { // add to our side
				board[1][currentIndex] += 1;
				if ((currentIndex + 1) % board[0].length != 0)
					currentIndex++;
				else {
					right = false;
					if (seeds == 0) {
						freeMove = true;
					}
					currentIndex = board[0].length - 2;
				}
			} else { // add to the opponents side
				board[0][currentIndex] += 1;
				if (currentIndex > 1)
					currentIndex--;
				else {
					right = true;
					currentIndex = 1;
				}
			}

		}
		if (right) { // steal the seeds from the opponent
			if (board[1][currentIndex - 1] == 1 && board[0][currentIndex - 1] > 0) {
				board[1][board[0].length - 1] += board[1][currentIndex - 1];
				board[1][currentIndex - 1] = 0;
				board[1][board[0].length - 1] += board[0][currentIndex - 1];
				board[0][currentIndex - 1] = 0;
			}
		}
		return board;
	}
}
