import java.util.ArrayList;
import java.util.Scanner;

public class Jason {
	static int[][] board = { { 0, 3, 3, 3, 3, 3, 3, 0 }, { 0, 3, 3, 3, 3, 3, 3, 0 } };
	static boolean anotherTurn = false;
	static boolean freeMove = false;

	public static void main(String[] args) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		boolean done = false;
		while (!done) {

			printBoard(board);
			System.out.println("Your turn:");
			anotherTurn = true;
			done = checkDone(board);
			while (anotherTurn && !done) {
				board = playMove(board, algorithm(board));
				Thread.sleep(0000);
				done = checkDone(board);
				if (anotherTurn) {
					System.out.println("You get another turn!");
					printBoard(board);
					System.out.println("Go again: ");
				}
			}
			printBoard(board);
			System.out.println("Swapping board");
			board = swapBoard(board);
			done = checkDone(board);
			anotherTurn = true;
			while (anotherTurn && !done) {
				printBoard(board);
				System.out.println("Random turn: ");
				board = playMove(board, randomMove());
				done = checkDone(board);
				if (anotherTurn) {
					printBoard(board);
					System.out.println("You get another turn!");
					System.out.println("Go again: ");
				}
			}
			printBoard(board);
			System.out.println("Swapping board");
			board = swapBoard(board);

		}
		System.out.println("The game is done!");
		printBoard(board);

		for (int i = 1; i < board[0].length-1; i++) {
			board[0][0] += board[0][i];
			board[1][board[0].length - 1] += board[1][i];
		}
		System.out.println("Me: " + board[1][board[0].length - 1]);
		System.out.println("Random: " + board[0][0]);
		if (board[0][0] < board[1][board[0].length - 1])
			System.out.println("You win!");
		else if (board[0][0] > board[1][board[0].length - 1])
			System.out.println("Random Wins");
		else
			System.out.println("It's a tie");

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

	public static boolean checkDone(int[][] board) {
		int sum = 0;
		for (int i = 1; i < board[0].length - 1; i++) {
			sum += board[1][i];
		}
		if (sum == 0)
			return true;
		return false;
	}

	/**
	 * Helper method to swap the opponent and the current players board.
	 */
	public static int [][] swapBoard(int [][] board) {
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
	public static int algorithm(int [][] board) {
		ArrayList<Integer> moves = findMoves(board);
		ArrayList<Integer> score = new ArrayList<Integer>();
		
		for (int move : moves){
			int [][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			score.add(heuristic(boardCopy, move));
		}
		int max = 0;
		int index = 0;
		for (int i = 0; i < score.size(); i++) {
			if(score.get(i) > max){
				max = score.get(i);
				index = i;
			}
		}
		return moves.get(index);
	}
	public static boolean isPlayableMove(int [][] board) {
		for(int i = 0; i < 7; i++) {
			if(board[1][i] > 0)
				return true;
		}
		return false;
	}

	public static int [][] findWorstScenerio(int [][] board){
		for(int i = board[0].length - 2; i > 0; i++){
			if(!freeMove){
				break;
			}
		}
		int max = 0;
		int move = 0;
		ArrayList<Integer> moves = findMoves(board);
		for(int k = 0; k < moves.size(); k++){
			int [][] boardCopy = new int[2][8];
			for (int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					boardCopy[i][j] = board[i][j];
				}
			}
			boardCopy = playMove(boardCopy,moves.get(k));
			if(boardCopy[1][boardCopy.length -1] > max){
				max = boardCopy[1][boardCopy.length -1];
				move = moves.get(k);
			}
		}
		if(moves.size() > 0)
			board = playMove(board,move);
		return board;
	}
	public static int heuristic(int [][] board, int move){
		int [][] newBoard = playMove(board,move);
		int value = 0;
		if(freeMove && !checkDone(newBoard)){
			freeMove = false;
			System.out.println("Move: " + move);
			move = algorithm(newBoard);
			System.out.println("Sub-move: " + move);
			return 10 + heuristic(newBoard, move);
		} else {
			board = swapBoard(findWorstScenerio(swapBoard(board)));
			for(int i = 1; i < 7; i++) {
				value += board[1][i];
			}
			value += 4 * newBoard[1][7];
		}
		System.out.println("move: " + move);
		System.out.println("heuristic: " + value + "\n");
		return value;
	}
	
	public static ArrayList<Integer> findMoves (int [][] board){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++){
            if(board[1][i] > 0)
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
		anotherTurn = false;
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
					if (seeds == 0){
						anotherTurn = true;
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

	public static int randomMove() {
		ArrayList<Integer> moves = new ArrayList<Integer>();

		for (int i = 1; i < 7; i++) {
			if (board[1][i] > 0)
				moves.add(i);
		}
		int random = (int) (Math.random() * moves.size());
		System.out.println("Random number between 0 and " + moves.size() + " is: " + random);

		return moves.get(random);
	}

}
