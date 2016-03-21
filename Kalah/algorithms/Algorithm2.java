package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.ArrayList;

public class Algorithm2 extends Algo{ // Replace TeamName
    private static String teamName = "Algorithm2"; // Replace TeamName
	static boolean freeMove = false;
	static  int totalSeeds = 0;
	static boolean stolen;
	
    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(Algorithm2.class); // Replace TeamName
    }

    public static String algorithm(Kalah game){  
   /**************************************************
    *************  PLACE ALGORITHM HERE  *************
    **************************************************/
        int [][] board = game.getBoard();
        if(totalSeeds == 0){
        	totalSeeds = board[0][1] * board[0].length - 2;
        }
		printBoard(board);
		int move = findBestMove(board);
		System.out.println("You moved: " + move);
		int[][] boardCopy = new int[2][8];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				boardCopy[i][j] = board[i][j];
			}
		}
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
		if (moves.size() == 1)
			return moves.get(0);
		ArrayList<Integer> score = new ArrayList<Integer>();

		for (int move : moves) {
			int[][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			score.add(findTotalHeuristic(boardCopy, move));
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
		for (int i = 1; i < 7; i++) {
			if (board[1][i] > 0)
				return true;
		}
		return false;
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

	public static int findTotalHeuristic(int[][] board, int move) {
		int value = 0;
		int temp = 0;
		System.out.println("Examining move: " + move);
		temp = freeMoveHeuristic(board,move);
		value += temp;
		System.out.println("Free move heuristic: " + temp);
		temp = stealHeuristic(board,move);
		value += temp;
		System.out.println("Steal opponent's heuristic: " + temp);
		temp = preventStealHeuristic(board,move);
		value += temp;
		System.out.println("Prevent Steal heuristic: " + temp);
		temp = over19Heuristic(board,move);
		value += temp;
		if (temp == -1)
			value = 0;
		System.out.println("Over 19 heuristic: " + temp);
		return value;
	}

	public static int freeMoveHeuristic(int[][] board, int move) {
		freeMove = false;
		int[][] newBoard = playMove(copyBoard(board), move);
		int total = 0;
		if (freeMove) {
			ArrayList<Integer> moves = findMoves(newBoard);
			for (int m : moves) {
				total += freeMoveHeuristic(copyBoard(newBoard), m);
			}
			total += 1;
		}
		return total;
	}

	public static int stealHeuristic(int[][] board, int move) {
		int storeBefore = board[1][7];
		int[][] newBoard = playMove(copyBoard(board), move);
		int storeAfter = newBoard[1][7];
		if(storeAfter - storeBefore > 1)
			return storeAfter - storeBefore;
		else 
			return 0;
	}

	public static int findNumOfSteals(int[][] board) {
		board = swapBoard(board);
		ArrayList<Integer> moves = findMoves(board);
		int stealMoves = 0;
		for (int m : moves) {
			int[][] newBoard = playMove(copyBoard(board),m);
			if (stolen)
				stealMoves++;
		}
		return stealMoves;
	}

	/**
	 * Returns the worst steal after the move
	 *
	*/
	public static int preventStealHeuristic(int[][] board, int move) {
		// numStealbefore is just an approximation of the ones that
		// the opponent could steal before
		int[][] newBoard = playMove(copyBoard(board),move);
		newBoard = flipBoard(newBoard);
		// play all free moves to make sure the spots are as open as possible
		for (int i = newBoard[0].length - 2; i > 0; i++) {
			if(newBoard[1][i] == newBoard[0].length - 1 - i){
				newBoard = playMove(board,i);
			} else {
				break;
			}
		}
		ArrayList<Integer> moves = findMoves(newBoard);
		int maxSteal = 0;
		for (int m : moves) {
			int oldStore = newBoard[1][7];
			int[][] board2 = playMove(newBoard,m);
			int newStore = board2[1][7];
			int stolenSeeds = newStore - oldStore - 1;
			if (stolenSeeds > 0 && stolenSeeds > maxSteal) {
				maxSteal = stolenSeeds;
			}
		}
		return - maxSteal;
	}

	public static int over19Heuristic(int[][] board, int move) {
		int[][] newBoard = playMove(copyBoard(board), move);
		int myControlSeeds = 0, opponentControlSeeds = 0;
		newBoard = swapBoard(findWorstScenerio(swapBoard(copyBoard(newBoard))));
		for (int i = 1; i < 7; i++) {
			// approximation
			if (7 - i >= newBoard[1][i])
				myControlSeeds += 7 - i;
			else
				myControlSeeds += newBoard[1][i];
		}
		for (int i = 1; i < 7; i++) {
			if(newBoard[0][i] - i > 0)
				myControlSeeds += newBoard[0][i] - i;
		}
		if (myControlSeeds < 18) {
			return -1;
		}
		return 1;
	}
 	
	public static ArrayList<Integer> findMoves(int[][] board) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			if (board[1][i] > 0)
				moves.add(i);
		}
		return moves;
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
