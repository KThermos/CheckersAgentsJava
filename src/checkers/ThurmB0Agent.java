package checkers;

import java.util.ArrayList;
import java.util.List;
import checkers.GameState.Status;
import checkers.Piece.Color;

public class ThurmB0Agent implements Agent {
//This agent analyzes one move in advance to develop a heuristic assessment of each move.
// Author: @thurm018
	
	/**
	 * 
	 * @param mState
	 * @return the heuristic value of the state 
	 * 
	 */
	public int getHeuristic(GameState mState){
		//same thing as C0 Agent
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
	 * parameter: GameState mState
	 * returns the next move
	 * 
	 */
	public GameState move(GameState mState){
		List<GameState> moves = mState.getSuccessorStates();
		List<GameTreeNode> nodes = new ArrayList<GameTreeNode>();
		for(GameState move: moves){
			nodes.add(new GameTreeNode(move));
		}
		//check out the heuristics for moves after the next
		for(GameTreeNode mommy: nodes){
			List<GameTreeNode> yanni = mommy.getChildren();
			if(yanni.size()!=0){
				for(GameTreeNode baby: yanni){
					baby.setHeuristic(getHeuristic(baby.getState()));
				}
				//find the minimum (which the other player is supposedly most likely to choose)
				int min = yanni.get(0).getHeuristic();
				for(int i = 0;i<yanni.size();i++){
					min = Math.min(min,yanni.get(i).getHeuristic());
				}
				mommy.setHeuristic(min);
			}
			//if there aren't any children, the move is a win for the other player.
			else{
				mommy.setHeuristic(getHeuristic(mommy.getState()));
			}
		}
		//find the next move that will give Agent the best worst-case scenario
		int max = nodes.get(0).getHeuristic();
		for(int i =0; i<nodes.size();i++){
			max = Math.max(max, nodes.get(i).getHeuristic());
		}
		ArrayList<GameTreeNode> oneLastList = new ArrayList<GameTreeNode>();
		for(GameTreeNode word:nodes){
			if(word.getHeuristic()==max){
				oneLastList.add(word);
			}
		}
		//if there are more than one move with max heuristic, choose randomly from them.
		double whatDoINeedToDoToMakeThisStupidDoubleAnInt = Math.floor(Math.random()*oneLastList.size());
		int i;
		for(i=0; i<whatDoINeedToDoToMakeThisStupidDoubleAnInt;i++){
		}
		return(oneLastList.get(i).getState());


	}

	
}
