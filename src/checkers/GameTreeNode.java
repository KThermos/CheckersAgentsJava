package checkers;

import java.util.ArrayList;
import java.util.List;

/************
 * Class that converts a gamestate and its successors into a tree structure
 * @author Andy
 *
 */
public class GameTreeNode {

	private final GameState gState;
	private final GameTreeNode gParent;
	private final int gDepth;
	private int gHeuristicVal;
	private List<GameTreeNode> gChildren;

	/********
	 * Getter for the heuristic value of this node
	 * @return the heuristic value
	 */
	public int getHeuristic() { return gHeuristicVal; }

	/*******
	 * Setter for the heuristic value of this node
	 * @param h the node's new heuristic value
	 */
	public void setHeuristic(int h) { gHeuristicVal = h; }

	/*********
	 * Constructs a new GameTreeNode from the given GameState
	 * @param mState the GameState
	 */
	public GameTreeNode(GameState mState) {
		gDepth = 0;
		gHeuristicVal = 0;
		gState = mState;
		gParent = null;
	}

	private GameTreeNode(GameState mState, GameTreeNode mParent, int mDepth) {
		gState = mState;
		gParent = null;
		gDepth = mDepth;
		gHeuristicVal = 0;
	}

	/*********
	 * Get the state stored by this GameTreeNode
	 * @return the state
	 */
	public GameState getState() { return gState; }
	
	/**********
	 * Get this node's depth in the current tree
	 * @return the depth
	 */
	public int getDepth() { return gDepth; }

	/********
	 * Oops! This was from an older version, don't use it!
	 * @deprecated
	 * @return null
	 */
	public GameTreeNode getParent() { return gParent; }

	/**********
	 * Get this node's children, as GameTreeNode's wrapping the state's successor states
	 * @return the list of children
	 */
	public List<GameTreeNode> getChildren() {
		if(gChildren == null) {
			gChildren = new ArrayList<GameTreeNode>();
			for(GameState s : gState.getSuccessorStates()) {
				gChildren.add(new GameTreeNode(s, this, gDepth+1));
			}
		}
		return gChildren;
	}
}
