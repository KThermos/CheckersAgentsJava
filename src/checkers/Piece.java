package checkers;

import java.awt.Point;

/*********
 * Class to hold information about a particular piece
 * Piece is an immutable class
 * @author Andy
 *
 */
public class Piece {

	/*********
	 * A simple enum to hold piece colors
	 * @author Andy
	 *
	 */
	public enum Color { BLACK, RED };
	
	/* instance variables that hold info about the piece */
	private final Point gSquare;
	private final Color gColor;
	private final boolean gKing;
	
	/*********
	 * Constructor that build a piece from location, color and king status
	 * @param mLoc the piece's location
	 * @param mColor the piece's color
	 * @param mKing whether the piece is a king or not
	 */
	public Piece(Point mLoc, Color mColor, boolean mKing) {
		gSquare = mLoc;
		gColor = mColor;
		gKing = mKing;
	}
	
	/*********
	 * Copy constructor that creates a new piece based on mPiece
	 * @param mPiece the piece to copy
	 */
	public Piece(Piece mPiece) {
		gSquare = mPiece.gSquare;
		gColor = mPiece.gColor;
		gKing = mPiece.gKing;
	}
	
	/*********
	 * Getter for the piece's location
	 * @return the location
	 */
	public Point getLocation() { return gSquare; }
	
	/*********
	 * Getter for the piece's color
	 * @return the color
	 */
	public Color getColor() { return gColor; }
	
	/*********
	 * Returns whether the piece is a king or not
	 * @return true if the piece is a king
	 */
	public boolean isKing() { return gKing; }
	
	public String toString() { return "(" + gSquare + ")"; } 
	
	public boolean equals(Object mObj) { 
		if(!(mObj instanceof Piece)) { return false; }
		Piece p = (Piece) mObj;
		return gSquare.equals(p.getLocation()) && gColor.equals(p.getColor()) &&
			(gKing == p.isKing());
	}
}
