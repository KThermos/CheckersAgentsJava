package checkers;

/*********
 * Class to handle moves for a Human player
 * @author Andy
 *
 */
public class HumanAgent implements Agent {

	/*******
	 * Only return null; we want this method to punt and let the GUI handle the move
	 */
	public GameState move(GameState state) {
		return null;
	}

	
	public String toString() { return "Human"; }
}
