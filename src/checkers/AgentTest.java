package checkers;

import junit.framework.TestCase;

public class AgentTest extends TestCase{
	
	private ThurmC0Agent me = new ThurmC0Agent();
	private RandomAIAgent you = new RandomAIAgent();
	private Game theGame;
	private Game theGame1;
	private Game theGame2;
	private Game theGame3;
	private Game theGame4;

	public void test1(){
		//System.out.println("Test 1: ");
		theGame = new Game(me,you);
		assertEquals(me.getHeuristic(theGame.getState()),4);
	}
	
	public void test2(){
		//System.out.println("Test 2: ");
		theGame1 = new Game(me,you,1);
		assertEquals(me.getHeuristic(theGame1.getState()),1);
	}
	
	public void test3(){
		//System.out.println("Test 3: ");
		theGame2 = new Game(me,you,2);
		assertEquals(me.getHeuristic(theGame2.getState()),9);
	}
	
	public void test4(){
		//System.out.println("Test 4: ");
		theGame3 = new Game(me,you,3);
		assertEquals(me.getHeuristic(theGame3.getState()),14);
	}
	
	public void test5(){
		//System.out.println("Test 5: ");
		theGame4 = new Game(me,you,4);
		assertEquals(me.getHeuristic(theGame4.getState()),-100000);
	}
}
