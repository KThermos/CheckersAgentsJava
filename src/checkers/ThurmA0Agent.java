package checkers;
import java.util.ArrayList;
	import java.util.List;
	import checkers.GameState.Status;
import checkers.Piece.Color;
	
public class ThurmA0Agent implements Agent{
	// Author: @thurm018
	
	/**
	 * 
	 * @param mState a potential gameState
	 * @return the heuristic value
	 */
	public int getHeuristic(GameState mState){
		//same thing
		GameTreeNode branch = new GameTreeNode(mState);
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
		ArrayList<Piece> myPieces = new ArrayList<Piece>();
		ArrayList<Piece> theirPieces = new ArrayList<Piece>();
		for(Piece aPiece: mState.getBoard().getPieces()){
			if(aPiece.getColor()== mState.getCurrentPlayer()){
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
				if(mState.getCurrentPlayer()== Color.BLACK){	
					if(thePiece.getLocation().getY()==0){
						branch.setHeuristic(branch.getHeuristic()+2);
					}
					else{
						branch.setHeuristic(branch.getHeuristic()+1);
					}
				}
				if(mState.getCurrentPlayer() == Color.RED){
						if(thePiece.getLocation().getY()==7){
							branch.setHeuristic(branch.getHeuristic()+2);
						}
						else{
						branch.setHeuristic(branch.getHeuristic()+1);
					}
				}
			}
		}
		for(Piece thePiece:theirPieces){
			branch.setHeuristic(branch.getHeuristic()-1);
			if(thePiece.isKing()){
				branch.setHeuristic(branch.getHeuristic()-4);
			}
		}
		return branch.getHeuristic();
	}

/**
 * @param GameState mState the current state of the game
 * @return GameState the next move
 * 
 */

	public GameState move(GameState mState){
		//find child nodes of the current state
		List<GameTreeNode> grandParents = new GameTreeNode(mState).getChildren();
		for(GameTreeNode grandma: grandParents){
			//find child nodes of the child nodes
			List<GameTreeNode> parents = grandma.getChildren();
			if(parents.size()!=0){
				for(GameTreeNode mom: parents){
					//find child nodes of the child nodes of the child nodes (current player's potential moves after the next ply)
					List<GameTreeNode> kids = mom.getChildren();
					if(kids.size()!=0){
						//find the heuristic values for each state
						for(GameTreeNode baby: kids){
							baby.setHeuristic(getHeuristic(baby.getState()));
						}
						//finds the highest heuristic score for each "mom"
						int max = kids.get(0).getHeuristic();
						for(int i = 0;i<kids.size();i++){
							max = Math.max(max,kids.get(i).getHeuristic());
						}
						mom.setHeuristic(max*-1);
					}
					else{
						mom.setHeuristic(getHeuristic(mom.getState()));
					}
				}
				//find minimum for the other player's next move
				int min = parents.get(0).getHeuristic();
				for(int i = 0;i<parents.size();i++){
					min = Math.min(min,parents.get(i).getHeuristic());
				}
				grandma.setHeuristic(min);
			}
			else{
				grandma.setHeuristic(getHeuristic(grandma.getState()));
			}
		}
		//find the maximum of the minimums of the maximums
		int max = grandParents.get(0).getHeuristic();
		for(int i =0; i<grandParents.size();i++){
			max = Math.max(max, grandParents.get(i).getHeuristic());
		}
		//constructs a list of the possible moves with the max score
		ArrayList<GameTreeNode> oneLastList = new ArrayList<GameTreeNode>();
		for(GameTreeNode word:grandParents){
			if(word.getHeuristic()==max){
				oneLastList.add(word);
			}
		}
		double whatDoINeedToDoToMakeThisStupidDoubleAnInt = Math.floor(Math.random()*oneLastList.size());
		int i;
		for(i=0; i<whatDoINeedToDoToMakeThisStupidDoubleAnInt;i++){
		}
		return(oneLastList.get(i).getState());
	}
}
