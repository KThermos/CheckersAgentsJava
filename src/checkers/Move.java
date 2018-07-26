package checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/************
 * Class to handle one particular move, which is internally represented 
 * as a list of points
 * @author Andy
 *
 */
public class Move {

	private ArrayList<Point> gList;
	
	/********
	 * Gets the first point in the path
	 * @return the point
	 */
	public Point getOrigin() { return gList.get(0); }
	
	/********
	 * Gets the final point in the path
	 * @return the point
	 */
	public Point getDestination() { return gList.get(gList.size()-1); }
	
	/********
	 * Default constructor
	 */
	public Move() {
		gList = new ArrayList<Point>();
	}
	
	/*********
	 * Copy constructor
	 * @param mMove the move to copy
	 */
	public Move(Move mMove) {
		gList = new ArrayList<Point>(mMove.getPath());
	}
	
	/*********
	 * Constructor that creates a move with a start point and a dest point
	 * @param mStart the origin of the move
	 * @param mDest the destination of the move
	 */
	public Move(Point mStart, Point mDest) {
		gList = new ArrayList<Point>();
		gList.add(mStart);
		gList.add(mDest);
	}
	
	/**********
	 * Adds p to this move's path
	 * @param p the point to add
	 */
	public void add(Point p) { gList.add(p); }
	
	/**********
	 * Adds all of m's path except for its origin point to this move's path
	 * @param m the move to add
	 */
	public void add(Move m) { gList.addAll(m.getPath().subList(1, m.getPath().size())); }
	
	/**********
	 * Gets the path of this move as a list of points
	 * @return the list
	 */
	public List<Point> getPath() { return gList; }
	
	public String toString() {
		String rString = "" + Board.toNumber(gList.get(0));
		for(int i = 1; i < gList.size(); i++) {
			rString += " -> " + Board.toNumber(gList.get(i));
		}
		return rString;
	}
}
