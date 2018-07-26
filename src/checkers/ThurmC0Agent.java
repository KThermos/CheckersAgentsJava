 package checkers;
 import java.util.List;
 import java.util.ArrayList;

import checkers.GameState.Status;
import checkers.Piece.Color;
public class ThurmC0Agent implements Agent{
// Author: @thurm018
//ThurmC0 Agent is a very basic agent that tries to avoid losing by basing decisions solely on the results of the next step and a weighted randomization process. 
	
	/**
	 * @param gState
	 * @return Color: Which team the agent is on
	 */
	private Color findMyColor(GameState gState){
		if(gState.getCurrentAgent() instanceof ThurmC0Agent){
			return gState.getCurrentPlayer();
		}
		else{
			if(gState.getCurrentPlayer().equals(Color.RED)){
				return Color.BLACK;
			}
			else{
				return Color.RED;
			}
		}
	}
	
	/**
	 * 
	 * @param mState
	 * @return the heuristic value of the state
	 */

	public int getHeuristic(GameState mState){
		Color myColor = findMyColor(mState);
		GameTreeNode branch = new GameTreeNode(mState);
		//check who wins and whether it is the agent or not
		if(mState.getStatus()==Status.BLACK_WINS && mState.getCurrentPlayer()==Color.BLACK){
			branch.setHeuristic(100000);
		}
		if(mState.getStatus()==Status.RED_WINS && mState.getCurrentPlayer() == Color.RED){
			branch.setHeuristic(100000);
		}
		if(mState.getStatus()==Status.BLACK_WINS && mState.getCurrentPlayer() == Color.RED){
			branch.setHeuristic(-100000);
		}
		if(mState.getStatus()==Status.RED_WINS && mState.getCurrentPlayer() == Color.BLACK){
			branch.setHeuristic(-100000);
		}
		//creates one list of the agent's pieces and one for the other guy's pieces
		ArrayList<Piece> myPieces = new ArrayList<Piece>();
		ArrayList<Piece> theirPieces = new ArrayList<Piece>();
		for(Piece aPiece: mState.getBoard().getPieces()){
			if(aPiece.getColor()== myColor){
				myPieces.add(aPiece);
			}
			else{
				theirPieces.add(aPiece);
			}
		}
		for(Piece thePiece:myPieces){
			if(thePiece.isKing()){
				branch.setHeuristic(branch.getHeuristic()+4);
			}
			else{
				//check the color in order to determine where home row is
				if(myColor== Color.BLACK){	
					if(thePiece.getLocation().getY()==0){
						branch.setHeuristic(branch.getHeuristic()+2);
						//System.out.println("+2");
					}
					else{
						branch.setHeuristic(branch.getHeuristic()+1);
						//System.out.println("+1");
					}
				}
				if(myColor == Color.RED){
						if(thePiece.getLocation().getY()==7){
							branch.setHeuristic(branch.getHeuristic()+2);
							//System.out.println("+2");
						}
						else{
						branch.setHeuristic(branch.getHeuristic()+1);
						//	System.out.println("+1");
					}
				}
			}
		}
		//adds negative values for opposite color pieces
		for(Piece thePiece:theirPieces){
			branch.setHeuristic(branch.getHeuristic()-1);
			if(thePiece.isKing()){
				branch.setHeuristic(branch.getHeuristic()-4);
			}
		}
		return branch.getHeuristic();
	}

	/**
	 * 
	 * @param mState
	 * @return a supposedly good next move
	 */
	public GameState move(GameState mState){
		List<GameState> moves = mState.getSuccessorStates(); //list of all successor states
		ArrayList<Integer> numbers = new ArrayList<Integer>(); //list of heuristic values associated with the states
		//weights values (highest -> more weight) and chooses at random
		for(GameState move: moves){
			numbers.add(getHeuristic(move));
		}
		int min = numbers.get(0);
		int sum = 0;
		for(int i =0; i<numbers.size();i++){
			min= Math.min(min,numbers.get(i));
		}
		for(int r= 0; r<numbers.size();r++){ 
			numbers.set(r,numbers.get(r)-min+1);
			sum = sum + numbers.get(r); 
		}
		double p = Math.random()*sum;
		int total = 0;
		int iter = 0;
		while(total<p){
			total = total + numbers.get(iter);
			iter++;
		}
		return moves.get(iter-1);
	}

}
