package checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/************
 * Class to hold information about a checker board
 * @author Andy
 *
 */
public class Board {

	private List<Piece> gPieces;

	/***********
	 * Default constructor
	 */
	public Board() {
		gPieces = new LinkedList<Piece>();
	}

	/***********
	 * Sets up a board for a new game
	 */
	public void init() {
		for(int i = 0; i < 12; i++) {
			gPieces.add(new Piece(new Point(2 * (i % 4) + (1 - (i / 4) % 2), i / 4), 
					GameState.PLAYER_COLORS[0], false));
		}
		for(int i = 20; i < 32; i++) {
			gPieces.add(new Piece(new Point(2 * (i % 4) + (1 - (i / 4) % 2), i / 4) , 
					GameState.PLAYER_COLORS[1], false));
		}
	}
	
/**
 * the next three constructors are only used to create test cases.
 * the fourth one is mostly just a placeholder to help keep things straight
 */
	public void init1() {
		gPieces.add(new Piece(new Point(2,4), GameState.PLAYER_COLORS[0], false));
		gPieces.add(new Piece(new Point(4,4), GameState.PLAYER_COLORS[0], false));
		gPieces.add(new Piece(new Point(4,6), GameState.PLAYER_COLORS[1], false));
	}

	public void init2(){
		gPieces.add(new Piece(new Point(4,4),GameState.PLAYER_COLORS[0], true));
		gPieces.add(new Piece(new Point(2,0), GameState.PLAYER_COLORS[0], false));
		gPieces.add(new Piece(new Point(2,4), GameState.PLAYER_COLORS[1], false));
		gPieces.add(new Piece(new Point(0,6),GameState.PLAYER_COLORS[0], true));
	}
	
	public void init3(){
		gPieces.add(new Piece(new Point(4,4),GameState.PLAYER_COLORS[0], true));
		gPieces.add(new Piece(new Point(2,0), GameState.PLAYER_COLORS[0], false));
		gPieces.add(new Piece(new Point(2,4), GameState.PLAYER_COLORS[1], false));
		gPieces.add(new Piece(new Point(0,6),GameState.PLAYER_COLORS[0], false));
		gPieces.add(new Piece(new Point(0,2),GameState.PLAYER_COLORS[0], true));
		gPieces.add(new Piece(new Point(0,4),GameState.PLAYER_COLORS[0], true));
	}
	
	public void init4(){
		
	}
	/***********
	 * Returns the list of pieces
	 * @return the list of pieces
	 */
	public List<Piece> getPieces() { return gPieces; }

	/***********
	 * Tests to see if mLoc is occupied by a piece
	 * @param mLoc the location to check
	 * @return whether the location is occupied
	 */
	public boolean isOccupied(Point mLoc) {
		for(Piece p : gPieces) {
			if(p.getLocation().equals(mLoc)) {
				return true;
			}
		}
		return false;
	}

	/************
	 * Tests to see if location (x,y) is occupied by a piece
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return whether the location is occupied
	 */
	public boolean isOccupied(int x, int y) {
		return isOccupied(new Point(x,y));
	}

	/************
	 * Makes the given move and returns a new board with the result
	 * @param mMove the move to make
	 * @return the new board
	 */
	public Board fullMove(Move mMove) {
		// move piece
		// remove capture pieces
		Board tNewBoard = move(mMove);
		Piece mPiece = getPiece(mMove.getOrigin());
		// king any pieces in king row.
		// check for kingship
		if((mMove.getDestination().y == 7 && 
				mPiece.getColor().equals(GameState.PLAYER_COLORS[0])) ||
				(mMove.getDestination().y == 0 &&
						mPiece.getColor().equals(GameState.PLAYER_COLORS[1])) &&
						!mPiece.isKing()) {
			tNewBoard.removePiece(mMove.getDestination());	
			tNewBoard.gPieces.add(new Piece(mMove.getDestination(), mPiece.getColor(), true));
		}
		return tNewBoard;
	}

	private Board move(Move mMove) {
		Piece mPiece = getPiece(mMove.getOrigin());
		Board tNewBoard = new Board();
		for(Piece p : gPieces) {
			if(mPiece != p) {
				tNewBoard.gPieces.add(new Piece(p));
			} else {
				tNewBoard.gPieces.add(new Piece(mMove.getDestination(), p.getColor(), p.isKing()));
			}
		}
		if(!isLegalOrdinaryMove(mPiece, mMove.getDestination())) {
			Point p1 = mPiece.getLocation();
			Point p2 = null;
			for(int i = 0; i < mMove.getPath().size(); i++) {
				p2 = mMove.getPath().get(i);
				tNewBoard.removePiece((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
				p1 = p2;
			}
		}
		return tNewBoard;
	}

	private void removePiece(Point mPoint) {
		for(Piece p : gPieces) {
			if(p.getLocation().equals(mPoint)) {
				gPieces.remove(p);
				return;
			}
		}
	}

	private void removePiece(int x, int y) {
		removePiece(new Point(x,y));
	}

	/*********
	 * Gets the piece at (x,y) or null if there is no piece there
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the piece at (x,y) or null if there is none
	 */
	public Piece getPiece(int x, int y) {
		return getPiece(new Point(x, y));
	}

	/*********
	 * Gets the piece at mPoint or null if there is no piece there
	 * @param mPoint the point to check
	 * @return the piece at mPoint or null if there is none
	 */
	public Piece getPiece(Point mPoint) {
		for(Piece p : gPieces) {
			if(p.getLocation().equals(mPoint)) return p;
		}
		return null;
	}

	/*********
	 * Checks whether moving piece mPiece to mPoint is a legal capture move
	 * This method ignores further captures
	 * @param mPiece the piece to check
	 * @param mPoint the point to move it to
	 * @return true is it is a legal capture move
	 */
	public boolean isLegalCapture(Piece mPiece, Point mPoint) {
		// first check simple things: mPoint must be legal and not occupied
		if(mPoint.x < 0 || mPoint.x > 7 || mPoint.y < 0 || mPoint.y > 7 || 
				isOccupied(mPoint)) { 
			return false;
		} else {
			// next, check that the target spot is +/- 2 on x and y
			Point tLoc = mPiece.getLocation();
			if(Math.abs(tLoc.x - mPoint.x) != 2) {
				return false;
			}
			if(Math.abs(tLoc.y - mPoint.y) != 2) {
				return false;
			}
			// next, check color-specific rules (black moves down, red moves up)
			if(!mPiece.isKing() && mPiece.getColor().equals(GameState.PLAYER_COLORS[0]) &&
					mPoint.y < tLoc.y) {
				return false;
			}
			if(!mPiece.isKing() && mPiece.getColor().equals(GameState.PLAYER_COLORS[1]) && 
					mPoint.y > tLoc.y) {
				return false;
			}
			// make sure that there is a piece to be captured and it is an opponent's piece
			Piece tCap = getPiece((mPoint.x + tLoc.x) / 2, (mPoint.y + tLoc.y) / 2);
			if(tCap == null || tCap.getColor().equals(mPiece.getColor())) {
				return false;
			}
			return true;
		}
	}

	/**********
	 * Checks whether moving mPiece to mPoint is a legal ordinary move
	 * @param mPiece the piece to check
	 * @param mPoint the point to move it to
	 * @return true if it is a legal ordinary move
	 */
	public boolean isLegalOrdinaryMove(Piece mPiece, Point mPoint) {
		if(mPoint.x >= 0 && mPoint.y >= 0 && mPoint.x < 9 && mPoint.y < 9 && !isOccupied(mPoint)) {
			if(Math.abs(mPoint.x - mPiece.getLocation().x) == 1) {
				if(mPiece.getColor().equals(GameState.PLAYER_COLORS[0]) || mPiece.isKing()) {
					if(mPoint.y - mPiece.getLocation().y == 1) { return true; }
				}
				if(mPiece.getColor().equals(GameState.PLAYER_COLORS[1]) || mPiece.isKing()) {
					if(mPoint.y - mPiece.getLocation().y == -1) { return true; }
				}
			}
		}
		return false;
	}
	
	/************
	 * Checks whether mPiece can make any capturing move 
	 * @param mPiece the piece to check
	 * @return true if mPiece can make a capturing move
	 */
	public boolean canCapture(Piece mPiece) {
		for(int i = mPiece.getLocation().x - 2; i <= mPiece.getLocation().x + 2; i+=4) {
			for(int j = mPiece.getLocation().y - 2; j <= mPiece.getLocation().y + 2; j+=4) {
				if(isLegalCapture(mPiece, new Point(i,j))) {
					return true;
				}
			}
		}
		return false;
	}

	/************
	 * Gets a list of all legal ordinary moves of mPiece
	 * @param mPiece the piece to check moves for
	 * @return a list of all legal moves
	 */
	public List<Move> getLegalOrdinaryMoves(Piece mPiece) {
		List<Move> rPoints = new ArrayList<Move>(4);
		Point tPoint = mPiece.getLocation();
		if(mPiece.getColor().equals(GameState.PLAYER_COLORS[0]) || mPiece.isKing()) {
			// moves down...
			if(tPoint.y < 7) {
				for(int i = tPoint.x-1>=0?tPoint.x-1:tPoint.x+1; i <= (tPoint.x+1<8?tPoint.x+1:tPoint.x-1); i+=2) {
					if(!isOccupied(new Point(i,tPoint.y+1))) {
						rPoints.add(new Move(mPiece.getLocation(), new Point(i, tPoint.y+1)));
					}
				}
			}
		} 
		if(mPiece.getColor().equals(GameState.PLAYER_COLORS[1]) || mPiece.isKing()){
			// moves up
			if(tPoint.y > 0) {
				for(int i = tPoint.x-1>=0?tPoint.x-1:tPoint.x+1; i <= (tPoint.x+1<8?tPoint.x+1:tPoint.x-1); i+=2) {
					if(!isOccupied(new Point(i,tPoint.y-1))) {
						rPoints.add(new Move(mPiece.getLocation(), new Point(i, tPoint.y-1)));
					}
				}
			}			
		}
		return rPoints;	
	}

	/************
	 * Gets a list of all legal capturing moves of mPiece
	 * @param mPiece the piece to check moves for
	 * @return a list of all legal moves
	 */
	public List<Move> getLegalCaptureMoves(Piece mPiece) {
		List<Move> rMoves = new ArrayList<Move>(4);
		Point tPoint = mPiece.getLocation();
		// Check for black pieces and all kings
		if(mPiece.getColor().equals(GameState.PLAYER_COLORS[0]) || mPiece.isKing()) {
			for(int i = tPoint.x - 2; i <= tPoint.x + 2; i += 4) {
				int j = tPoint.y + 2;
				Point tPoint2 = new Point(i,j);
				if(isLegalCapture(mPiece, tPoint2)) {
					Move m = new Move(mPiece.getLocation(), tPoint2);
					Board tBoard = move(new Move(mPiece.getLocation(), tPoint2));
					if(tBoard.canCapture(tBoard.getPiece(tPoint2))) {
						// need to continue the capture...
						List<Move> tMoves = new ArrayList<Move>();
						for(Move tMove : tBoard.getLegalCaptureMoves(tBoard.getPiece(tPoint2))) {
							Move tMove2 = new Move(m);
							tMove2.add(tMove);
							tMoves.add(tMove2);
						}
						rMoves.addAll(tMoves);
					} else {
						rMoves.add(m);
					}
				}
			}
		}
		// Check for red piece and all kings
		if(mPiece.getColor().equals(GameState.PLAYER_COLORS[1]) || mPiece.isKing()) {
			// captures up...
			for(int i = tPoint.x - 2; i <= tPoint.x + 2; i += 4) {
				int j = tPoint.y - 2;
				Point tPoint2 = new Point(i,j);
				if(isLegalCapture(mPiece, tPoint2)) {
					Move m = new Move(mPiece.getLocation(), tPoint2);
					Board tBoard = move(new Move(mPiece.getLocation(), tPoint2));
					if(tBoard.canCapture(tBoard.getPiece(tPoint2))) {
						// need to continue the capture...
						List<Move> tMoves = new ArrayList<Move>();
						for(Move tMove : tBoard.getLegalCaptureMoves(tBoard.getPiece(tPoint2))) {
							Move tMove2 = new Move(m);
							tMove2.add(tMove);
							tMoves.add(tMove2);
						}
						rMoves.addAll(tMoves);
					} else {
						rMoves.add(m);
					}
				}
			}

		}
		return rMoves;
	}
	
	/***********
	 * Converts from a point (x,y) 0-based to standard checkers board numbering 1-32
	 * @param mPoint the point to convert
	 * @return the number of point, or -1 if mPoint is not a valid board location
	 */
	public static int toNumber(Point mPoint) {
		if(mPoint.x < 0 || mPoint.x > 7 || mPoint.y < 0 || mPoint.x > 7) {
			// out of bounds
			return -1;
		} else if((mPoint.x + mPoint.y) % 2 == 0) {
			// if x+y is even, this is a light square
			return -1;
		} else {
			return (mPoint.x / 2 + 1) + mPoint.y*4;
		}
	}
}
