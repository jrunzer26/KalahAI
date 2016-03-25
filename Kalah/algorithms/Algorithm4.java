package algorithms;
import org.gamelink.game.Kalah;
import org.gamelink.game.Algo;
import java.util.ArrayList;
import java.text.DecimalFormat;
public class Algorithm4 extends Algo{ // Replace TeamName
    private static String teamName = "Algorithm4"; // Replace TeamName
	static boolean freeMove = false;
	static  int totalSeeds = 0;
	static boolean stolen;
	
    public static String getTeamName(){
        return teamName;
    }

    public static void main(String[] args){
        Kalah game = new Kalah(false);
        game.startGame(Algorithm4.class); // Replace TeamName
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
		int winningMove = -1;
		for (int i = 0; i < score.size(); i++) {
			if (score.get(i)[0] >= max) {
				max = score.get(i)[0];
				index = i;
			}
		}
		for (int i = 0; i < score.size(); i++) {
			if (score.get(i)[1] > 0.6) {
				System.out.println("win :D");
				return moves.get(i);
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

	public static double[] findTotalHeuristic(int[][] board, int move) {
		double value = 0;
		double[] temp;
		System.out.println("\n************ Examining move: " + move + " *******");
		temp = search(copyBoard(board), move, 11, true); //set to 12
		value += temp[0];
		System.out.println(temp[1]);
		System.out.println("Free move heuristic: " + temp);
		System.out.println("**** Total heuristic : " + new DecimalFormat("#0.0000000000").format(value) + " *******");
		return temp;
	}

	public static double[] getHeuristic(int[][] board, boolean myTurn) {
		double value = 0;
		int[][] newBoard;
		double[] a = new double[2];
		//System.out.print(myTurn + " ");
		if (!myTurn)
			newBoard = swapBoard(board);
		else
			newBoard = board;
				//i would loose :(
		if(newBoard[0][0] >= 19){
			//System.out.println("i would loose");
			a[0] = 0;
			a[1] = 0;
			return a;
		}
		boolean theyAreDone = false;
		for (int i = 1; i < 7; i++) {
			if (!(newBoard[0][i] == 0))
				break;
			if(i == 6)
				theyAreDone = true;
		}
		boolean imDone = false;
		for (int i = 1; i < 7; i++) {
			if (!(newBoard[1][i] == 0))
				break;
			if(i == 6)
				imDone = true;
		}
		if (theyAreDone || imDone) {
			//System.out.print(myTurn + " ");
			//System.out.println("found winning solution");
			for (int j = 1; j < 7; j++) {
				value += newBoard[1][j];
				value -= newBoard[0][j];
			}
			value +=  newBoard[1][7];
			value -= newBoard[0][0];
			a[0] = value;
			a[1] = 1;
			//System.out.println(a[1]);
			return a;
		}
		

		
		
 		if(newBoard[1][7] >= 19) {
			value += newBoard[0][0];
		} 
		
		
		for (int i = 1; i < 7; i++) {
			if (board[1][i] <= 7 - i)
				value += newBoard[1][i];
			else
				value += newBoard[1][i] -(7 - i) + 1; 
		}
		for (int i = 1; i < 7; i++) {
			if (newBoard[0][i] - i > 3)
				value += newBoard[0][i] - i;
		}
		
		value += newBoard[1][7] * 2 - newBoard[0][0];
		//value -= estimateTurnsLeft(newBoard);
		a[0] = value;
		a[1] = 0;
		return a;
	}

	public static int estimateTurnsLeft(int[][] board) {
		int turns = 0;
		for(int i = 1; i < 7; i++) {
			if(board[1][i] != 0)
				turns++;
		}
		return turns;
	}
	public static double[] search(int[][]board, int move, int searchDepth, boolean myTurn) {
		int[][] newBoard = playMove(copyBoard(board),move);
		if (searchDepth == 0) {
			//System.out.println("move: " + move + " search depth: " + searchDepth + "myTurn : " + myTurn);
			return getHeuristic(newBoard,myTurn);
		} else {
			ArrayList<Integer> moves = findMoves(newBoard);
			if (moves.size() == 0) 
				//return 0;
				return getHeuristic(newBoard,myTurn);
			ArrayList<double[]> score = new ArrayList<double[]>();
			double win = 1;
			double winpercent = 0;
			double[] a = new double[2];
			if (myTurn) {				
				double average = 0;
				
				for (int m : moves) {
					score.add(search(copyBoard(newBoard), m, searchDepth - 1, freeMove));
				}
				for (double[] s : score) {
					average += s[0];
					//System.out.println(s[1]);
					/*
					if (s[1] != 1)
						win = 0;
					*/
					winpercent += s[1];
				}
				average /= score.size() + 0.0;
				winpercent /= score.size() +0.0;
				a[0] = average;
				a[1] = winpercent;
				return a;
			} else {
				double average = 0;
				if (freeMove)
					myTurn = false;
				else
					myTurn = true;
				for (int m : moves) {
					score.add(search(swapBoard(copyBoard(newBoard)), m, searchDepth - 1, myTurn));
				}
				for (double[] s : score) {
					average += s[0];
					//System.out.println(s[1]);
					/*
					if (s[1] != 1)
						win = 0;
					*/
					winpercent += s[1];
				}
				//average /= score.size() + 0.0;
				average = average / (score.size() + 0.0);
				winpercent /= score.size() +0.0;
				a[0] = average;
				a[1] = winpercent;
				return a;
			}
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
