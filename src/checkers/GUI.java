package checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/************
 * Class that handles the highest level GUI business for the checkers game
 * @version 0.9
 * @author Andy
 *
 */
public class GUI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1128101043082804815L;

	private GameComponent gGameComponent;
	private JToolBar gToolBar;
	private JButton gStart;
	private JComboBox gPlayer0;
	private JComboBox gPlayer1;
	private JLabel gStatus;
	private JPanel gStatusPanel;
	private JFrame gParent;

	/*********
	 * Add your classes to this array to have them show up in the GUI
	 */
	@SuppressWarnings("unchecked")
	private static Class[] AGENTS = {
		HumanAgent.class,
		RandomAIAgent.class
	};
	
	/***********
	 * Constructor that takes a frame as its parent
	 * @param mParent the frame that contains the panel
	 */
	public GUI(JFrame mParent) {
		gParent = mParent;
		this.setLayout(new BorderLayout());
		this.add(getToolBar(), BorderLayout.NORTH);
		this.add(getGameComponent(), BorderLayout.CENTER);
	}

	/***********
	 * Method implemented so that the panel can listen to its buttons
	 */
	public void actionPerformed(ActionEvent mEvent) {
		String tCmd = mEvent.getActionCommand();
		if(tCmd.equals("start")) {
			newGame();
		} else if(tCmd.equals("undo")) {
			getGameComponent().undo();
		} else if(tCmd.equals("numbers")) {
			JCheckBox tBox = (JCheckBox)mEvent.getSource();
			getGameComponent().setNumberDisplay(tBox.isSelected());
		}
	}

	/**********
	 * Set the panel's status text
	 * @param mText the text
	 */
	public void setStatus(String mText) {
		gStatus.setText(mText);
	}
	
	/**********
	 * Private GUI method
	 * @return the CheckerGameComoponent
	 */
	private GameComponent getGameComponent() {
		if(gGameComponent == null) {
			gGameComponent = new GameComponent(this);
		}
		return gGameComponent;
	}
	
	private JToolBar getToolBar() {
		if(gToolBar == null) {
			gToolBar = new JToolBar();
			gToolBar.setFloatable(false);

			gStart = new JButton("New Game");
			gStart.addActionListener(this);
			gStart.setActionCommand("start");
			gToolBar.add(gStart);

			gToolBar.add(new JToolBar.Separator());

			gToolBar.add(getStatus());
			
			gToolBar.add(new JToolBar.Separator());
			
			JButton tUndo = new JButton("Undo");
			tUndo.addActionListener(this);
			tUndo.setActionCommand("undo");
			gToolBar.add(tUndo);

			gToolBar.add(new JToolBar.Separator());

			JCheckBox tNumbers = new JCheckBox("Board Numbers", true);
			tNumbers.addActionListener(this);
			tNumbers.setActionCommand("numbers");
			gToolBar.add(tNumbers);
		}
		return gToolBar;
	}
	
	private JPanel getStatus() {
		if(gStatusPanel == null) {
			gStatus = new JLabel();
			gStatus.setText("Waiting for game to start...");

			gStatusPanel = new JPanel();
			gStatusPanel.add(gStatus);
			gStatus.setMinimumSize(new Dimension(500, 20));
		}
		return gStatusPanel;
	}


	private void newGame() {
		getGameComponent().pauseGame();
		JDialog tFrame = new JDialog(gParent, "New Game");
		tFrame.setSize(300, 200);
		tFrame.setLocation(this.getLocation().x + 200, this.getLocation().y + 200);
		tFrame.setLayout(new GridLayout(0, 1));
		tFrame.setAlwaysOnTop(true);

		tFrame.add(new JLabel("Select Players"));
		JPanel tComp1 = new JPanel();
		tComp1.setLayout(new BorderLayout());
		tComp1.add(new JLabel(" Black:"), BorderLayout.WEST);
		tComp1.add(getPlayer0(), BorderLayout.EAST);
		tFrame.add(tComp1);
		
		JPanel tComp2 = new JPanel();
		tComp2.setLayout(new BorderLayout());
		tComp2.add(new JLabel(" Red:"), BorderLayout.WEST);
		tComp2.add(getPlayer1(), BorderLayout.EAST);
		tFrame.add(tComp2);
		
		JButton tStart = new JButton("Start");
		tStart.addActionListener(new NewGameListener(tFrame));
		tStart.setActionCommand("start");

		JButton tCancel = new JButton("Cancel");
		tCancel.addActionListener(new NewGameListener(tFrame));
		tCancel.setActionCommand("cancel");

		JPanel tComp3 = new JPanel();
		tComp3.setLayout(new BorderLayout());
		tComp3.add(tStart, BorderLayout.WEST);
		tComp3.add(tCancel, BorderLayout.EAST);
		tFrame.add(tComp3);
		
		tFrame.setVisible(true);
	}
	
	private JComboBox getPlayer0() {
		if(gPlayer0 == null) {
			gPlayer0 = new JComboBox();
			for(Class<Agent> c : AGENTS) {
				try {
					gPlayer0.addItem(c.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return gPlayer0;
	}
	
	private JComboBox getPlayer1() {
		if(gPlayer1 == null) {
			gPlayer1 = new JComboBox();
			for(Class<Agent> c : AGENTS) {
				try {
					gPlayer1.addItem(c.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return gPlayer1;
	}
	
	private class NewGameListener implements ActionListener {
		private JDialog gParent;
		public NewGameListener(JDialog mFrame) { gParent = mFrame; }
		
		public void actionPerformed(ActionEvent arg0) {
			String tCmd = arg0.getActionCommand();
			if(tCmd.equals("start")) {
				getGameComponent().startGame((Agent)gPlayer0.getSelectedItem(), (Agent)gPlayer1.getSelectedItem());				
				gParent.dispose();
			} else if(tCmd.equals("cancel")) {
				getGameComponent().pauseGame();
				gParent.dispose();
			}	
		}
	}
	
	/***********
	 * GUI Main method, runs the GUI version of checkers
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}

	private static void createGUI() {
		JFrame tFrame = new JFrame("Checkers");
		tFrame.add(new GUI(tFrame));
		tFrame.setSize(660, 715);
		tFrame.setResizable(false);
		tFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tFrame.setBackground(Color.WHITE);
		tFrame.setVisible(true);
	}

}

