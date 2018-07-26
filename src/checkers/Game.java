package checkers;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import checkers.GameState.Status;

/***********
 * A class to handle a checker game
 * @author Andy
 *
 */
public class Game {

	private GameState gState;
	private Stack<GameState> gUndoStack;
	private List<Move> gMoveList;

	/**********
	 * Constructor that creates a new game with the given agents
	 * @param mAgent0 the agent to play as black
	 * @param mAgent1 the agent to play as red
	 */
	public Game(Agent mAgent0, Agent mAgent1) {
		gState = new GameState(mAgent0, mAgent1);
		gUndoStack = new Stack<GameState>();
		gMoveList = new ArrayList<Move>();
	}
	/**
	 * 
	 * constructor for test versions of the game setup.
	 */
	public Game(Agent mAgent0, Agent mAgent1,int test){
		gState = new GameState(mAgent0, mAgent1,test);
		gUndoStack = new Stack<GameState>();
		gMoveList = new ArrayList<Move>();
	
	}
	/***********
	 * Runs a game to finish, assumes two AI agents
	 */
	public void play() {
		if(gState.getStatus() == Status.RED || gState.getStatus() == Status.BLACK) {
			GameState tState = gState.getCurrentAgent().move(gState);
			if(tState != null) {
				if(!gState.getSuccessorStates().contains(tState)) {
					throw new RuntimeException("Illegal Move!!");
				} else {
					gState = tState;
					gMoveList.add(gState.getLastMove());
					play();
				}
			}
		}
	}

	/************
	 * Returns the status of the game
	 * @return the status
	 */
	public Status getStatus() { return gState.getStatus(); }

	/************
	 * Getter for the current GameState
	 * @return the current GameState
	 */
	public GameState getState() { return gState; }

	/************
	 * Forces the game to move mPiece along mMove
	 * @param mMove the move to move the piece along
	 */
	public void makeMove(Move mMove) {
		if(gState.getCurrentAgent() instanceof HumanAgent)
			gUndoStack.push(gState);
		gMoveList.add(mMove);
		gState = gState.move(mMove);
	}

	/************
	 * Undoes the most recent move
	 */
	public void undo() { 
		try {
			gState = gUndoStack.pop();
			gMoveList.remove(gMoveList.size()-1);
		} catch(EmptyStackException e) {
			/* do nothing */
		}
	}

	/************
	 * Getter for the current board
	 * @return the current board
	 */
	public Board getBoard() { return gState.getBoard(); }

	/***********
	 * Game's main method: runs some test games. This method can be modified 
	 * as much as you want
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		/* change these constants to test your agents */
		Agent[] tAgents = { new ThurmC0Agent(), new ThurmA1Agent() };
		int[] tWins = new int[3];
		int tNumGames = 100;
		for(int i = 0; i < tNumGames / 2; i++) {
			Game tGame = new Game(tAgents[0], tAgents[1]);
			tGame.play();
			if(tGame.getStatus() == Status.BLACK_WINS) {
				tWins[0]++;
			} else if(tGame.getStatus() == Status.RED_WINS) {
				tWins[1]++;
			} else if(tGame.getStatus() == Status.DRAW) {
				tWins[2]++;
			}
			
			tGame = new Game(tAgents[1], tAgents[0]);
			tGame.play();
			if(tGame.getStatus() == Status.RED_WINS) {
				tWins[0]++;
			} else if(tGame.getStatus() == Status.BLACK_WINS) {
				tWins[1]++;
			} else if(tGame.getStatus() == Status.DRAW) {
				tWins[2]++;
			}
		}
		
		System.out.printf("%s: %d  %s: %d  Draw: %d\n", tAgents[0], tWins[0], tAgents[1], tWins[1], tWins[2]);
	}
}
