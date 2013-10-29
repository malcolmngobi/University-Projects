package TableStuff;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * This class deals with WindowEvents and how to deal with a duplicate query
 * @author Mally
 *
 */
public class Windowlisten {

	/* An ArrayList which contains all the current frames opened*/
	private static ArrayList<JFrame> winFrame = new ArrayList<JFrame>();
	
	/**
	 *  When a window is opened its added to the ArrayList
	 *  @param - fram The current JFrame (Table) opened
	 *   */
	public static void addWin(JFrame fram){
		winFrame.add(fram);
	}
	
	/**
	 *  Checking whether the Table to be Opened is already opened if so then we 
	 *  move this Jframe to the front of the arraylist. 
	 * 
	 *  @param - title:- the title of the JFrame window(table)
	 *  @return - true:- if window is found
	 *  @return -  false:- if window is not found
	 * */
	public static boolean checkWin(String title){
		for(int i = 0; i< winFrame.size(); i++){
			if(winFrame.get(i).getTitle().equals(title)){
				winFrame.get(i).toFront();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * When a window is closed its removed to the ArrayList
	 * @param fram - The current JFrame (Table) closed
	 * 
	 */
	public static void removeWin(JFrame fram){
		winFrame.remove(fram);
	}
}
