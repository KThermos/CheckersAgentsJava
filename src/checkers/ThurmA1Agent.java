package checkers;
import java.util.ArrayList;
	import java.util.List;
	import checkers.GameState.Status;
	import checkers.Piece.Color;
	import java.awt.Point;
	
public class ThurmA1Agent implements Agent{
// Author: @thurm018
	
		/**
		 * 
		 * @param mState
		 * @return the heuristic
		 */
	public int getHeuristic(GameState mState){
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
					branch.setHeuristic(branch.getHeuristic()+10);
				}
				else{
					//check to see if there are other pieces of the same color right next to thePiece
					for(Piece otherPiece:myPieces){
						if(otherPiece.getLocation().getX()+1==thePiece.getLocation().getX()||otherPiece.getLocation().getX()-1==thePiece.getLocation().getX()){
							if(otherPiece.getLocation().getX()+1==thePiece.getLocation().getY()||otherPiece.getLocation().getX()-1==thePiece.getLocation().getY()){
								branch.setHeuristic(branch.getHeuristic()+1);
							}
						}
					}
					if(mState.getCurrentPlayer()== Color.BLACK){	
						if(thePiece.getLocation().getY()==0){
							branch.setHeuristic(branch.getHeuristic()+4);
						}
						else{
							branch.setHeuristic(branch.getHeuristic()+3);
						}
					}
					if(mState.getCurrentPlayer() == Color.RED){
							if(thePiece.getLocation().getY()==7){
								branch.setHeuristic(branch.getHeuristic()+4);
							}
							else{
							branch.setHeuristic(branch.getHeuristic()+3);
						}
					}
				}
			}
			for(Piece thePiece:theirPieces){
				branch.setHeuristic(branch.getHeuristic()-4);
				if(thePiece.isKing()){
					branch.setHeuristic(branch.getHeuristic()-9);
				}
			}
			return branch.getHeuristic();
		}
		
		
		
		
		
		/**
		 * parameter: GameState mState
		 * returns the next move
		 */
	public GameState move(GameState mState){
		//same thing
			List<GameTreeNode> grandParents = new GameTreeNode(mState).getChildren();
			for(GameTreeNode grandma: grandParents){
				List<GameTreeNode> parents = grandma.getChildren();
				if(parents.size()!=0){
					for(GameTreeNode mom: parents){
						List<GameTreeNode> kids = mom.getChildren();
						if(kids.size()!=0){
							for(GameTreeNode baby: kids){
								baby.setHeuristic(getHeuristic(baby.getState()));
							}
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
			int max = grandParents.get(0).getHeuristic();
			for(int i =0; i<grandParents.size();i++){
				max = Math.max(max, grandParents.get(i).getHeuristic());
			}
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