package checkers;

import java.util.List;

/***************
 * A class to handle a simple random agent
 * @author Andy
 *
 */
public class RandomAIAgent implements Agent {

	/********
	 * Randomly selects a state from the successor state list
	 */
	public GameState move(GameState state) {
		List<GameState> tList = state.getSuccessorStates();
		return tList.get((int)(tList.size() * Math.random()));
	}

	public String toString() { return "AI (Random)"; }
}
