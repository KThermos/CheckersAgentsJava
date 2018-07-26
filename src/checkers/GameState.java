package checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/************
 * Class to handle a checkers game's state
 * @author Andy
 *
 */
public class GameState {

	/*******
	 * An enum to hold the games current status: BLACK = black's turn, RED = red's turn, etc.
	 * @author Andy
	 *
	 */
	public enum Status { BLACK, RED, BLACK_WINS, RED_WINS, DRAW };

	/*******
	 * Public constants that hold the colors of the players... better to do with enums
	 */
	public static final Piece.Color[] PLAYER_COLORS = { Piece.Color.BLACK, Piece.Color.RED };

	/* Private constants for agent objects and whose turn it is */
	private Agent[] gPlayers;
	private int gCurrentPlayer;

	/* Board, legal move map, and some bookkeeping objects */
	private Board gBoard;
	private Map<Piece, List<Move>> gLegalMoveMap;
	private List<GameState> gSuccessorStates;
	private Move gLastMove;
	private Piece gLastPiece;

	/* Counter for checking for a draw */
	private int gDrawCounter;

	/*********
	 * Constructor for a game state given two agents
	 * @param mAgent0 the agent to play black
	 * @param mAgent1 the agent to play red
	 */
	public GameState(Agent mAgent0, Agent mAgent1) {
		gBoard = new Board();
		gBoard.init();
		gCurrentPlayer = 0;
		gDrawCounter = 0;
		gPlayers = new Agent[2];
		gPlayers[0] = mAgent0;
		gPlayers[1] = mAgent1;
	}
	
	/**
	 * 
	 * constructs a GameState with the board specified for the test case
	 */
	public GameState(Agent mAgent0, Agent mAgent1,int test){
		gBoard = new Board();
		if(test ==1){
			gBoard.init1();
		}
		if(test ==2){
			gBoard.init2();
		}
		if(test == 3){
			gBoard.init3();
		}
		if(test == 4){
			gBoard.init4();
		}
		gCurrentPlayer = 0;
		gDrawCounter = 0;
		gPlayers = new Agent[2];
		gPlayers[0] = mAgent0;
		gPlayers[1] = mAgent1;
	}
	/**********
	 * Gets the last piece that was moved
	 * @return the piece
	 */
	public Piece getLastPieceMoved() { return gLastPiece; }

	/**********
	 * Gets the last move that was made
	 * @return the move
	 */
	public Move getLastMove() { return gLastMove; }

	/**********
	 * Gets the current checker board
	 * @return the board
	 */
	public Board getBoard() { return gBoard; }

	/**********
	 * Returns the agent whose turn it is
	 * @return the agent
	 */
	public Agent getCurrentAgent() { return gPlayers[gCurrentPlayer]; }

	/**********
	 * Returns the color of the current player
	 * @return the color
	 */
	public Piece.Color getCurrentPlayer() { return PLAYER_COLORS[gCurrentPlayer]; }

	/**********
	 * Returns a list of all successor states of this state if none available, someone
	 * must have won, or it is a draw.
	 * @return a list of states
	 */
	public List<GameState> getSuccessorStates() {
		if(gSuccessorStates == null) {
			gSuccessorStates = new ArrayList<GameState>();
			for(Piece p : getLegalMoves().keySet()) {
				for(Move m : getLegalMoves().get(p)) {
					gSuccessorStates.add(move(m));
				}
			}
		}
		gLegalMoveMap = null;
		return gSuccessorStates;
	}

	/*********
	 * Returns the status of the current game state: Draw, Black or Red win, Black or Red's turn
	 * @return the status
	 */
	public Status getStatus() {
		if(isDraw()) { return Status.DRAW; }
		else if(PLAYER_COLORS[0].equals(getWinner())) { return Status.BLACK_WINS; }
		else if(PLAYER_COLORS[1].equals(getWinner())) { return Status.RED_WINS; }
		else { return gCurrentPlayer==0?Status.BLACK:Status.RED; }
	}

	private boolean isDraw() { return (gDrawCounter / 2) >= 50; }

	private Piece.Color getWinner() {
		int count = 0;
		for(List<Move> tList : getLegalMoves().values()) {
			count += tList.size();
		}
		if(count == 0) { return PLAYER_COLORS[1-gCurrentPlayer]; }
		else return null;
	}

	/*********
	 * Gets a map from pieces to lists of moves of all legal moves
	 * @return the map
	 */
	public Map<Piece, List<Move>> getLegalMoves() {
		if(gLegalMoveMap != null) { return gLegalMoveMap; }
		gLegalMoveMap = new HashMap<Piece, List<Move>>();
		boolean tCap = false;
		// first, check if any piece can make a capture move
		for(Piece p : gBoard.getPieces()) {
			if(p.getColor().equals(PLAYER_COLORS[gCurrentPlayer]) && gBoard.canCapture(p)) {
				tCap = true;
			}
		}
		if(tCap) {
			// there is a capture move available, so only capture moves...
			for(Piece p: gBoard.getPieces()) {
				if(p.getColor().equals(PLAYER_COLORS[gCurrentPlayer])) {
					if(!gLegalMoveMap.containsKey(p)) { gLegalMoveMap.put(p, new ArrayList<Move>()); }
					gLegalMoveMap.get(p).addAll(gBoard.getLegalCaptureMoves(p));
				}
			}
		} else {
			// there are no captures available, so only ordinary moves...
			for(Piece p: gBoard.getPieces()) {
				if(p.getColor().equals(PLAYER_COLORS[gCurrentPlayer])) {
					if(!gLegalMoveMap.containsKey(p)) { gLegalMoveMap.put(p, new ArrayList<Move>()); }
					gLegalMoveMap.get(p).addAll(gBoard.getLegalOrdinaryMoves(p));
				}
			}
		}
		return gLegalMoveMap;
	}

	/*********
	 * Returns the resulting game state after moving mPiece along mMove
	 * @param mMove the move to make
	 * @return the resulting game state
	 */
	public GameState move(Move mMove) {
		Piece tPiece = gBoard.getPiece(mMove.getOrigin());
		GameState rState = new GameState(gPlayers[0], gPlayers[1]);
		rState.gBoard = gBoard.fullMove(mMove);
		rState.gCurrentPlayer = 1 - this.gCurrentPlayer;
		rState.gLastMove = mMove;
		rState.gLastPiece = tPiece;
		if(tPiece.isKing() && rState.getBoard().getPieces().size() == this.getBoard().getPieces().size()) {
			rState.gDrawCounter = this.gDrawCounter + 1;
		}
		return rState;
	}
}

