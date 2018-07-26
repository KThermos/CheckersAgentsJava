package checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import checkers.GameState.Status;

/***************
 * This is the class that handles the majority of GUI stuff
 * @author Andy
 *
 */
public class GameComponent extends JComponent 
implements MouseListener, MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 155976975390647365L;

	/* Constants for the Color scheme, feel free to change this if you don't like the decor */
	private static final Color BLACK_PIECE = Color.BLACK;
	private static final Color RED_PIECE = new Color(200,50,50);
	private static final Color LEGAL_MOVES = Color.BLUE;
	private static final Color MIDDLE_MOVES = Color.CYAN.darker().darker();
	private static final Color EVEN_SQUARES = new Color(230,230,230);
	private static final Color ODD_SQUARES = new Color(170,120,150);

	/* constants for drawing the board and pieces */
	private static final int SQUARE_SIZE = 80;
	private static final int PIECE_SIZE = 60;
	private static final int LEFT_MARGIN = 10;
	private static final int TOP_MARGIN = 10;

	/* constant that handles the animation speed, smaller = faster */
	private static final int RUN_DELAY = 6;

	/* parent of this component, to pass up status messages */
	private GUI gParent;

	/* CheckerGame */
	private Game gGame;

	/* privates for GUI handling */
	private Piece gCurrentPiece;
	private Point gDragPoint;
	private Point gDragStartPoint;
	private List<Rectangle2D.Double> gLegalMoveList;
	private List<Rectangle2D.Double> gMiddleMoveList;
	private Rectangle2D.Double gCurrentMove;
	private boolean gDrawNumbers = true;

	/* privates for handling animation of AI moves */
	private Piece gAnimatedPiece;
	private Move gAnimatedMove;
	private int gAnimationCounter;
	private Timer gRunTimer;

	/*********
	 * GameComponent constructor
	 * @param mParent this GameComponent's parent CheckersGUI
	 */
	public GameComponent(GUI mParent) {
		gParent = mParent;
		gLegalMoveList = new ArrayList<Rectangle2D.Double>();
		gMiddleMoveList = new ArrayList<Rectangle2D.Double>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Set up the timer
		gRunTimer = new Timer(RUN_DELAY, this); //new TimerTranslator(this, "step"));
	}

	/*********
	 * Starts a new game of checkers with the two given agents
	 * @param mAgent0 the agent to play for player0
	 * @param mAgent1 the agent to play for player1
	 */
	public void startGame(Agent mAgent0, Agent mAgent1) {
		gGame = new Game(mAgent0, mAgent1);
		setCurrentTurn();
		agentMove();
		repaint();
	}

	/**********
	 * Pauses the timer, effectively pausing the animation
	 */
	public void pauseGame() {
		if(gRunTimer.isRunning()) { gRunTimer.stop(); }
		else { gRunTimer.start(); }
	}

	/***********
	 * Calls CheckerGame's undo method
	 */
	public void undo() { 
		gGame.undo();
		setCurrentTurn();
		repaint();
	}

	/************
	 * Sets whether to display square numbers or not
	 * @param mNumbers true if you want square number display on
	 */
	public void setNumberDisplay(boolean mNumbers) {
		gDrawNumbers = mNumbers;
		repaint();
	}

	private void agentMove() {
		if(gGame.getStatus() == Status.BLACK_WINS) {
			gParent.setStatus("Black wins");
		} else if(gGame.getStatus() == Status.RED_WINS) {
			gParent.setStatus("Red wins");
		} else if(gGame.getStatus() == Status.DRAW) {
			gParent.setStatus("Draw!");
		} else {
			GameState tState = gGame.getState().getCurrentAgent().move(gGame.getState());
			if(tState != null) {
				gAnimatedPiece = tState.getLastPieceMoved();
				gAnimatedMove = tState.getLastMove();
				gAnimationCounter = 0;
				gRunTimer.start();
			}
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		drawBoard(g2);	
		drawPieces(g2);
	}

	private void setCurrentTurn() {
		if(gGame.getStatus() == Status.BLACK) {
			gParent.setStatus("Black's Turn");
		} else if(gGame.getStatus() == Status.RED) {
			gParent.setStatus("Red's Turn");			
		}
	}

	private void drawBoard(Graphics2D g2) {
		// draw grid
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				g2.setColor((j+i)%2==0?EVEN_SQUARES:ODD_SQUARES);
				g2.fillRect(j*SQUARE_SIZE + LEFT_MARGIN, i*SQUARE_SIZE + TOP_MARGIN, SQUARE_SIZE, SQUARE_SIZE);
			}
		}

		// draw border
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(LEFT_MARGIN / 2, TOP_MARGIN / 2, LEFT_MARGIN / 2, 8 * SQUARE_SIZE + TOP_MARGIN);
		g2.fillRect(8 * SQUARE_SIZE + LEFT_MARGIN, TOP_MARGIN / 2, LEFT_MARGIN / 2, 8 * SQUARE_SIZE + TOP_MARGIN);
		g2.fillRect(LEFT_MARGIN / 2, TOP_MARGIN / 2, 8 * SQUARE_SIZE + LEFT_MARGIN, TOP_MARGIN / 2);
		g2.fillRect(LEFT_MARGIN / 2, 8 * SQUARE_SIZE + TOP_MARGIN, 8 * SQUARE_SIZE + LEFT_MARGIN, LEFT_MARGIN / 2);

		// draw legal moves - based on currently held piece
		for(Rectangle2D.Double tRect : gMiddleMoveList) {
			g2.setColor(MIDDLE_MOVES);
			g2.fill(tRect);
		}
		for(Rectangle2D.Double tRect : gLegalMoveList) {
			g2.setColor(LEGAL_MOVES.brighter());
			if(tRect == gCurrentMove) { g2.setColor(LEGAL_MOVES.darker()); }
			g2.fill(tRect);
		}

		// draw square numbering
		if(gDrawNumbers) {
			for(int i=0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					int tmp = Board.toNumber(new Point(j,i));
					if(tmp != -1) {
						g2.setColor(EVEN_SQUARES);
						g2.drawString("" + tmp, j*SQUARE_SIZE + LEFT_MARGIN, i*SQUARE_SIZE + TOP_MARGIN + 11);
					}					
				}
			}
		}
	}

	private void drawPieces(Graphics2D g2) {
		// draw pieces
		if(gGame != null) {
			// temps for piece drawing
			int STEPS = 50;
			int counter = gAnimationCounter / STEPS;
			int diff = (SQUARE_SIZE - PIECE_SIZE)/2;


			// pieces on the board
			for(Piece p : gGame.getBoard().getPieces()) {
				int x = p.getLocation().x;
				int y = p.getLocation().y;
				if(p != gCurrentPiece && p != gAnimatedPiece) {
					drawPiece(g2, p, LEFT_MARGIN + x*SQUARE_SIZE + diff, 
							TOP_MARGIN + y*SQUARE_SIZE + diff);
				}
			}

			// piece held by human
			if(gCurrentPiece != null) {
				int x = gCurrentPiece.getLocation().x;
				int y = gCurrentPiece.getLocation().y;
				drawPiece(g2, gCurrentPiece, LEFT_MARGIN + x*SQUARE_SIZE + diff + gDragPoint.x - gDragStartPoint.x,
						TOP_MARGIN + y*SQUARE_SIZE + diff + gDragPoint.y - gDragStartPoint.y);
			}

			// check if animation is over and make move if it is
			if(gAnimatedPiece != null) {
				if(counter >= gAnimatedMove.getPath().size()-1) {
					gGame.makeMove(gAnimatedMove);
					drawPiece(g2, gAnimatedPiece, 
							LEFT_MARGIN + gAnimatedMove.getDestination().x*SQUARE_SIZE + diff,
							TOP_MARGIN + gAnimatedMove.getDestination().y*SQUARE_SIZE + diff);
					gAnimatedPiece = null;
					gAnimatedMove = null;
					gRunTimer.stop();
					setCurrentTurn();
					agentMove();
					repaint();
				} else {
					// animated piece
					Point tFirst = gAnimatedMove.getPath().get(0);
					Point tSecond = gAnimatedMove.getPath().get(1);
					if (counter > 0) {
						// this is a move with multiple steps, need to get the relevant 
						// points in the path
						tFirst = gAnimatedMove.getPath().get(counter);
						tSecond = gAnimatedMove.getPath().get(counter+1);
					}

					// interpolate between points on the path based on the animation counter
					int x = ((STEPS - (gAnimationCounter % STEPS)) * tFirst.x*SQUARE_SIZE + 
							(gAnimationCounter % STEPS) * tSecond.x*SQUARE_SIZE) / STEPS;
					int y = ((STEPS - (gAnimationCounter % STEPS)) * tFirst.y*SQUARE_SIZE + 
							(gAnimationCounter % STEPS) * tSecond.y*SQUARE_SIZE) / STEPS;
					drawPiece(g2, gAnimatedPiece, LEFT_MARGIN + x + diff, TOP_MARGIN + y + diff);
				}
			}
		}
	}

	private void drawPiece(Graphics2D g2, Piece p, int x, int y) {
		int KING_SIZE = 10;
		Color tPieceColor = (p.getColor().equals(Piece.Color.BLACK)?BLACK_PIECE:RED_PIECE);
		g2.setColor(tPieceColor);
		g2.fillOval(x, y, PIECE_SIZE, PIECE_SIZE);
		if(p.isKing()) {
			g2.setColor(Color.WHITE);
			g2.drawOval(x + KING_SIZE/2, y + KING_SIZE/2, PIECE_SIZE - KING_SIZE, 
					PIECE_SIZE - KING_SIZE);
		}
	}

	private void setLegalMoves() {
		if(gGame.getState().getLegalMoves().containsKey(gCurrentPiece)) {
			for(Move tMove : gGame.getState().getLegalMoves().get(gCurrentPiece)) {
				gLegalMoveList.add(new Rectangle2D.Double(LEFT_MARGIN + 
						tMove.getDestination().x*SQUARE_SIZE + 1, 
						TOP_MARGIN + tMove.getDestination().y*SQUARE_SIZE + 1,
						SQUARE_SIZE-2 , SQUARE_SIZE-2));
				for(Point tPoint : tMove.getPath()) {
					gMiddleMoveList.add(new Rectangle2D.Double(LEFT_MARGIN + 
							tPoint.x*SQUARE_SIZE + 1, 
							TOP_MARGIN + tPoint.y*SQUARE_SIZE + 1,
							SQUARE_SIZE-2 , SQUARE_SIZE-2));
				}
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {	/* do nothing */ }
	public void mouseEntered(MouseEvent arg0) { /* do nothing */ }
	public void mouseExited(MouseEvent arg0) { /* do nothing */ }
	public void mouseMoved(MouseEvent arg0) { /* do nothing */ }

	public void mousePressed(MouseEvent mEvent) { 
		if(gGame != null && gGame.getState().getCurrentAgent() instanceof HumanAgent) {
			for(Piece p : gGame.getBoard().getPieces()) {
				int diff = (SQUARE_SIZE - PIECE_SIZE)/2;
				if(new Ellipse2D.Double(LEFT_MARGIN + p.getLocation().x * SQUARE_SIZE + diff, 
						TOP_MARGIN + p.getLocation().y * SQUARE_SIZE + diff, 
						PIECE_SIZE, PIECE_SIZE).contains(mEvent.getPoint())) {
					gDragPoint = mEvent.getPoint();
					gDragStartPoint = mEvent.getPoint();
					gCurrentPiece = p;
					setLegalMoves();
					repaint();
				}
			}
		}
	}

	public void mouseReleased(MouseEvent mEvent) {
		if(gCurrentMove != null && gCurrentPiece != null) {
			int x = (int)((gCurrentMove.x - 1 - LEFT_MARGIN) / SQUARE_SIZE);
			int y = (int)((gCurrentMove.y - 1 - TOP_MARGIN) / SQUARE_SIZE);
			List<Move> tList = gGame.getState().getLegalMoves().get(gCurrentPiece);
			List<Move> tPossibles = new ArrayList<Move>();
			for(Move tMove : tList) {
				if(tMove.getDestination().equals(new Point(x,y))) {
					tPossibles.add(tMove);
				}
			}
			if(tPossibles.size() == 1) {
				gGame.makeMove(tPossibles.get(0));
			} else if(tPossibles.size() > 1) {
				Object[] choices = tPossibles.toArray();
				Move m = (Move)JOptionPane.showInputDialog(this, "Oh, man, that's confusing!\n" +
						"Which move do you actually want to make?", "Choose Move",
						JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
				if(m != null) {
					gGame.makeMove(m);		
				}
			}
			setCurrentTurn();
			agentMove();
			gCurrentMove = null;
		}
		gLegalMoveList.clear();
		gMiddleMoveList.clear();
		gCurrentPiece = null;
		gDragPoint = null;
		gDragStartPoint = null;
		repaint();
	}

	public void mouseDragged(MouseEvent mEvent) {
		if(gDragPoint != null && gCurrentPiece != null) {
			gDragPoint = mEvent.getPoint();
			boolean found = false;
			for(Rectangle2D.Double tRect : gLegalMoveList) {
				if(tRect.contains(gDragPoint)) {
					gCurrentMove = tRect;
					found = true;
				}
			}
			if(!found) { gCurrentMove = null; }
		}
		repaint();
	}

	public void actionPerformed(ActionEvent mEvent) {
		Object tSrc = mEvent.getSource();
		if(tSrc == gRunTimer) {
			gAnimationCounter++;
			repaint();
		}
	}
}
