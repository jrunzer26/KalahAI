package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.ArrayList;
import java.text.DecimalFormat;
public class Algorithm5 extends Algo{ // Replace TeamName
    private static String teamName = "Algorithm5"; // Replace TeamName
	static boolean freeMove = false;
	static  int totalSeeds = 0;
	static boolean stolen;
	static int counter = 0;
	
    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(Algorithm5.class); // Replace TeamName
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
		if (moves.size() == 1)
			return moves.get(0);
		ArrayList<double[]> score = new ArrayList<double[]>();

		for (int move : moves) {
			int[][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			score.add(findTotalHeuristic(boardCopy, move));
		}
		double max = score.get(0)[0];
		int index = 0;
		int maxWinIndex = 0;
		double maxWin = 0;
		int winningMove = -1;
		for (int i = 0; i < score.size(); i++) {
			if (score.get(i)[1] > maxWin) {
				maxWin = score.get(i)[1];
				maxWinIndex = i;
			}
			if (score.get(i)[0] >= max) {
				max = score.get(i)[0];
				index = i;
			}
		}

		if( maxWin >= 0.8)
			return moves.get(maxWinIndex);
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

	public static double[] findTotalHeuristic(int[][] board, int move) {
		double value = 0;
		double[] temp;
		System.out.println("\n************ Examining move: " + move + " *******");
		temp = search(copyBoard(board), move, 10, true); //set to 12
		value += temp[0];
		System.out.println(temp[1]);
		System.out.println("Free move heuristic: " + temp);
		System.out.println("**** Total heuristic : " + new DecimalFormat("#0.0000000000").format(value) + " *******");
		return temp;
	}
	public static void printArray(double[] array) {
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i] + " ");
		System.out.println();
	}

	public static double howCloseToWin(int[][] board) {
		int value = board[1][7] - 19;
		return value;
	}

	public static double howCloseToLoss(int[][] board) {
		int value = 19 - board[0][0];
		return value;
	}
	public static double howFarAhead(int[][] board) {
		return (board[1][7] - board[0][0]);
	}

	

	public static double[] getHeuristic(int[][] board, boolean myTurn, boolean done) {
		double value = 0;
		int[][] newBoard;
		double[] a = new double[2];
		boolean print = false;
		//System.out.print(myTurn + " ");
		if (!myTurn)
			newBoard = swapBoard(copyBoard(board));
		else
			newBoard = board;
		if (done) {
			for (int j = 1; j < 7; j++) {
				value += newBoard[1][j];
				value -= newBoard[0][j];
			}
			value +=  newBoard[1][7];
			value -= newBoard[0][0];
			a[0] = 2 * value;
			if(value > 0)
				a[1] = 1;
			else
				a[1] = 0;
			return a;
		}
		if( board[1][7] < 19) {
			a[0] += 0.5 * howFarAhead(newBoard);
			a[0] +=  0.75 * howCloseToWin(newBoard);
			a[0] += 0.5 * howCloseToLoss(newBoard);
		}
		else {
			a[0] = 20;
			a[1] = 1;
		}
		return a;
	}

	public static double[] search(int[][]board, int move, int searchDepth, boolean myTurn) {
		int[][] newBoard = playMove(copyBoard(board),move);
		if (searchDepth == 0) {
			return getHeuristic(newBoard, myTurn, false);
		} else {
			ArrayList<Integer> moves;

			ArrayList<double[]> score = new ArrayList<double[]>();
			double win = 1;
			double winpercent = 0;
			double average = 0;
			double[] a = new double[2];
			if (myTurn) {
				if(!freeMove) {
					newBoard = swapBoard(newBoard);
				}
				moves = findMoves(newBoard);
				if (moves.size() == 0) 
					return getHeuristic(newBoard, freeMove, true);			
				for (int m : moves) {
					if( (freeMove && newBoard[0][0] < 19 ) || !freeMove && newBoard[1][7] < 19)
						score.add(search(copyBoard(newBoard), m, searchDepth - 1, freeMove));
					else {
						score.add(getHeuristic(newBoard, freeMove, false));
					}
				}
			} else {
				if (freeMove)
					myTurn = false;
				else {
					myTurn = true;
					newBoard = swapBoard(newBoard);
				}
				moves = findMoves(newBoard);
				if (moves.size() == 0) 
					return getHeuristic(newBoard, myTurn, true);
				for (int m : moves) {
					if( (!myTurn && newBoard[1][7] < 19) || (myTurn && newBoard[0][0] < 19))
						score.add(search(copyBoard(newBoard), m, searchDepth - 1, myTurn));
					else {
						score.add(getHeuristic(newBoard, freeMove, false));
					}
				}
			}
			for (double[] s : score) {
				average += s[0];
				winpercent += s[1];
			}
			average /= score.size() + 0.0;
			winpercent /= score.size() + 0.0;
			a[0] = average;
			a[1] = winpercent;
			return a;
		}
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









