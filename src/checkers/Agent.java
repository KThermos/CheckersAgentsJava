package checkers;

/**********
 * Interface to handle game-playing agents
 * @author Andy
 *
 */
public interface Agent {
	
	/***********
	 * Ask the agent to make a move
	 * @param mState the current state
	 * @return the new state to move to
	 */
	public GameState move(GameState mState);
}
