/**
* Jason Runzer
* 100520993
*/

package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.ArrayList;
import java.text.DecimalFormat;
public class Runzer_Jason extends Algo{ // Replace TeamName
    private static String teamName = "Runzer_Jason"; // Replace TeamName
	static boolean freeMove = false;
	static  int totalSeeds = 0;
	static boolean stolen;
	
    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(Runzer_Jason.class); // Replace TeamName
    }

    public static String algorithm(Kalah game){  
   /**************************************************
    *************  PLACE ALGORITHM HERE  *************
    **************************************************/
        int [][] board = game.getBoard();
        if(totalSeeds == 0){
        	totalSeeds = 2 * board[0][1] * (board[0].length - 2);
        }
	printBoard(board);
	int move = findBestMove(board);
	System.out.println("You moved: " + move);
	int[][] boardCopy = copyBoard(board);
	System.out.println("Board before:");
	printBoard(board);
	System.out.println("Playing move: " + move);
	printBoard(playMove(boardCopy,move));
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
	 	
	public static ArrayList<Integer> findMoves(int[][] board) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			if (board[1][i] > 0)
				moves.add(i);
		}
		return moves;
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

		boolean playLast = true;
		int startSeeds = board[0][1];
		for (int i = 1; i < board[0].length - 1; i++) {
			if((board[0][i] != startSeeds) || (board[1][i] != startSeeds))
				playLast = false;	
		}
		if(playLast)
			return moves.get(moves.size() - 1);
		if (moves.size() == 1)
			return moves.get(0);
		ArrayList<Integer> score = new ArrayList<Integer>();
		for (int move : moves) {
			int[][] boardCopy = copyBoard(board);
			System.out.println("\n************ Examining move: " + move + " *******");
			int total = findHeuristic(boardCopy, move, true, 1);
			System.out.println("Total heuristic: " + total);
			score.add(total);
		}
		int max = score.get(0);
		int index = 0;
		for (int i = 0; i < score.size(); i++) {
			if (score.get(i) >= max) {
				max = score.get(i);
				index = i;
			}
		}
		return moves.get(index);
	}
	
	public static boolean checkDone(int[][] board) {
		for (int i = 1; i < board[0].length - 1; i++) {
			if (board[1][i] != 0 || board[0][i] != 0)
				return false;
		}
		return true;
	}
	
	
	public static int [][] copyBoard (int [][] board){
		int[][] boardCopy = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				boardCopy[i][j] = board[i][j];
			}
		}
		return boardCopy;
	}

	public static void printArray(double[] array) {
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i] + " ");
		System.out.println();
	}

	public static int howCloseToWin(int[][] board) {
		int value = board[1][board[0].length - 1] - (totalSeeds / 2 + 1);
		return value;
	}

	public static int howFarAhead(int[][] board) {
		return (board[1][board[0].length -1] - board[0][0]);
	}

	public static int findHeuristic(int[][] board, int move, boolean myTurn, int depth) {
		freeMove = false;
		int[][] newBoard = playMove(copyBoard(board), move);
		int total = 0;
		int max = -200;
		if (checkDone(newBoard)) {
			if (!myTurn)
				newBoard = swapBoard(newBoard);
			max = 0;
			for(int i = 0; i < newBoard[0].length; i++) {
				max += newBoard[1][i];
				max -= newBoard[0][i];
			}
			max -= newBoard[0][0];
		} else if (freeMove) {
			ArrayList<Integer> moves = findMoves(newBoard);
			for (int m : moves) {
				int value = 1 + findHeuristic(copyBoard(newBoard), m, myTurn, depth);
				if (value > max)
					max = value;
			}
		} else {
			int doneTotal = 0;
			doneTotal += stealHeuristic(newBoard, board);
			doneTotal += howFarAhead(newBoard);
			if(howCloseToWin(newBoard) >= 0)
				doneTotal += 5 * howCloseToWin(newBoard);
			depth--;
			if (depth >= 0) {
				newBoard = swapBoard(newBoard);
				ArrayList<Integer> moves = findMoves(newBoard);
				int theirMax = -100;
				for (int m : moves) {
					int value = findHeuristic(copyBoard(newBoard), m, !myTurn, depth);
					if (value > theirMax)
						theirMax = value;
				}
				doneTotal -= theirMax;
			}
			if (doneTotal > max)
				max = doneTotal;
		}
		return max;
	}

	public static int stealHeuristic(int[][] board, int[][] oldBoard) {
		if (board[1][board[0].length - 1] - oldBoard[1][board[0].length -1] > 1)
			return board[1][board[0].length - 1] - oldBoard[1][board[0].length - 1];
		else
			return 0;
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
		stolen = false;
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
				stolen = true;
				board[1][board[0].length - 1] += board[1][currentIndex - 1];
				board[1][currentIndex - 1] = 0;
				board[1][board[0].length - 1] += board[0][currentIndex - 1];
				board[0][currentIndex - 1] = 0;
			} 
		}
		return board;
	}

}

