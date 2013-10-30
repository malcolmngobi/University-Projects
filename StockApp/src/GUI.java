import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * This classes main function is the bulk of my application which deals visually 
 * in presenting the window allowing functionality to each individual features to be 
 * used successfully for a users specific needs. 
 */
public class GUI extends JFrame implements ActionListener { 

	//Declaring variables of J Instances 
	private JLabel output,input;
	protected JLabel outputInfo;
	private JPanel bottomHalf,middleElements,topHalf,belowpane1,belowpane2,belowpane3,belowpane4,labelPanel,infoPanel;
	private JTextArea ourText;
	private JScrollPane scroll;
	//Creating new button arrays used to hold the buttons which are going to be used throughout this class
	private JButton[] buttons1 = new JButton[10];
	private JButton[] buttons2 = new JButton[30];
	//Intermediate arrays used to hold the values of the button labels with would like to provide
	private int[] buttonline1 = {0,1,2,3,4,5,6,7,8,9};
	private String[] buttonline2 = {"Q","W","E","R","T","Y","U","I","O","P","DEL",
			"A","S","D","F","G","H","J","K","L","ENTER","Z","X","C",
			"V","B","N","M",".","Space"};
	//Counter that records the number of spaces entered
	private int countSpace = 1;
	//All is the variable which contains all of the text to be queried.
	private String all;

	//Constructor for class with no parameters 
	public GUI(){
		super("Stock Market APP");

		//Create new objects with object references of J instances
		input = new JLabel("Input");
		output = new JLabel("Output");
		outputInfo = new JLabel();
		ourText = new JTextArea();

		//Sets our textArea so that it is only assessable via the onscreen Keyboard.
		ourText.setEditable(false);


		/*
		 * Creating the buttons and assigning them to a array index value and a ActionListener. 
		 * This is done via a loop which accesses the indexes of two arrays: 
		 * - One of which holds the String/Integer elements individually (Which is used as the Labels of the buttons).
		 * - The other contains an empty array which is waiting to be filled with JButtons using elements provided 
		 * 	 by the first array as the buttons Labels.
		 */
		for(int i=0; i<buttons1.length; i++){buttons1[i] = new JButton(Integer.toString(buttonline1[i]));
		buttons1[i].addActionListener(this);}
		for(int i=0; i<buttons2.length; i++){buttons2[i] = new JButton(buttonline2[i]);
		buttons2[i].addActionListener(this);}

		/* Creating JPanel Objects which are later going to be used in various number of layouts to achieve the 
		   final layout at the end.

		   Layouts used - FlowLayout for a simple left to right design of buttons in this case ordered accordingly
		   				- BorderLayout for a simple ordering of panels to achieve the end design effect.
		 */
		bottomHalf = new JPanel(new BorderLayout());
		middleElements = new JPanel(new BorderLayout());

		topHalf = new JPanel(new BorderLayout());
		labelPanel = new JPanel(new BorderLayout());
		infoPanel = new JPanel(new BorderLayout());

		belowpane1 = new JPanel(new FlowLayout());
		belowpane2 = new JPanel(new FlowLayout());
		belowpane3 = new JPanel(new FlowLayout());
		belowpane4 = new JPanel(new FlowLayout());

		/*
		 * Create a new JScrollPane and parse the element i would like to attach a scrollPane to. 
		 * Setting the VerticalScroll bar to only be visible when there is not enough space to display stock items.
		 * Giving the scrollPane a minimal size and a preferred size.
		 */
		scroll = new JScrollPane(outputInfo);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(250,250));
		scroll.setMinimumSize(new Dimension(250,250));

		//Looping through the number array adding each number(button) to the first belowpane panel 
		for(int i=0; i<buttons1.length; i++){belowpane1.add(buttons1[i]);}

		//Looping through the array of buttons adding them to specific panels in order to achieve the end design effect.
		for(int i=0; i<=10; i++){belowpane2.add(buttons2[i]);}
		for(int i=11; i<=20; i++){belowpane3.add(buttons2[i]);}
		for(int i=21; i<30; i++){belowpane4.add(buttons2[i]);}

		//Adding the panels with the specific buttons to another layout which will be used again.
		middleElements.add(belowpane2, BorderLayout.NORTH);
		middleElements.add(belowpane3, BorderLayout.SOUTH);

		/* Adding all the buttons into a final button panel which allocates the first row of buttons 
		   to the north region of the BorderLayout , the second two rows of buttons which have been allocated 
		   a separate panel to the center region, leaving lastly the last row of buttons to be in the south region.
		 */
		bottomHalf.add(belowpane1, BorderLayout.NORTH);
		bottomHalf.add(middleElements, BorderLayout.CENTER);
		bottomHalf.add(belowpane4, BorderLayout.SOUTH);

		//Adds the output and input labels in a Borderlayout to a panel. 
		labelPanel.add(output, BorderLayout.NORTH);
		labelPanel.add(input, BorderLayout.SOUTH);


		//Adds the JLabel and JTextField instances in a Borderlayout to a panel.
		infoPanel.add(scroll, BorderLayout.NORTH);
		infoPanel.add(ourText, BorderLayout.SOUTH);

		/* Adds the output, input , JTextArea , and JLabel to another panel which makes the topHalf of the 
		   application. 
		 */ 
		topHalf.add(labelPanel, BorderLayout.WEST);
		topHalf.add(infoPanel, BorderLayout.CENTER);


		/*
		 *  Focusing on the current window(GetContentPane) in whole *this* and setting a general layout for everything
		 *  Where we finally allocate a final general layout where the topHalf is allocated to the Center of the 
		 *  Application and the bottomHalf is allocated to the south of the application.
		 */
		this.setLayout(new BorderLayout());
		this.add(topHalf, BorderLayout.CENTER);
		this.add(bottomHalf, BorderLayout.SOUTH);	
	
		/* Setting the size of the application , allowing the app to close when prompted by the user and 
		 * and allowing the user to see the app. 
		 */

		this.setSize(570,480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	/*
	 * Action method which adds functionality to buttons when they are pressed
	 * @param ev An action event activated when item an with a actionListener is initiated(non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ev) {

		String textAreaString = ourText.getText();
		int textAreaLength = ourText.getText().length();
		
		//For buttons 0 - 9 
		for(int i=0; i<10; i++){
			//If the String returned by the button pressed equals to an element in the array carry out this action.
			if(ev.getActionCommand() == buttons1[i].getText()){

				/*If our counting variable does not exceeds or equals the integer 8 then carry out the if . 
				  If : - Simply gets the Text currently on the TextArea and concatenates it with the index of the button's Text.
				  If not then jump to the else
				 */ 
				if(textAreaLength - textAreaString.lastIndexOf(" ") < 9){
					String currentText = textAreaString;
					ourText.setText(currentText + buttons1[i].getText());
				}

				//Display a message dialog showing an error.
				else{
					JOptionPane.showMessageDialog(ourText, "Has exceeded limit" , "Error" , JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
		}

		//For buttons A - Z , Del and Enter(in nested if statements)
		for(int i=0; i<30; i++){
			if(ev.getActionCommand() == buttons2[i].getText()){

				//If the String returned by the button pressed equals to an element in the array carry out this action.
				if(ev.getActionCommand() == "DEL")
				{

					/*If the input text not empty then a character can get deleted and the counter which checks whether the character limit 
				  has been exceeded is decremented
					 */

					if(textAreaString.length() > 0){
						ourText.setText(textAreaString.substring(0, textAreaLength - 1));
					}

					//Else a error message is displayed
					else{
						JOptionPane.showMessageDialog(ourText, "Field is empty, Please enter text" , "Error" , JOptionPane.ERROR_MESSAGE);
					}
					break;
				}

				/*
				 *  For Enter Button which queries the stock symbols and returns the result to a JLabel. 
				 *  The specific implementation of how it is queried is shown on my URLClass.
				 */
				else if(ev.getActionCommand() == "ENTER"){
					/* 
					 * Gets all the text and replaces the spaces with +'s then makes a new object with the URLClass
					 * with :
					 * @param all 
					 * @param this 
					 * @param countSpace
					 * After that the text is set to empty and all counters are reset. 
					 */
					all = textAreaString.replaceAll(" ", "+");
					new URLClass(all, this , countSpace );
					ourText.setText("");
					countSpace = 1;
					break;
				}

				/*
				 * When the Space button is pressed the Text is concatenated with a space and the 
				 * space counter increases by one. 
				 */
				else if(ev.getActionCommand() == "Space"){
					ourText.setText(textAreaString + " ");
					countSpace++; 
				}
				
				/*If the button pressed is not enter or delete, then the current text in the TextArea is 
			Concatenated with the string character label of the button pressed
				 */
				else{
					/*
					 *
					 * The String character label is only added If the total length of text minus the last instance of a space is contains less than 
					 * 9 characters(8 Characters or less in other words) 
					 * then carry out the if. If not then jump to the else
					 */
					
					if(textAreaLength - textAreaString.lastIndexOf(" ") < 9){
						String currentText = textAreaString;
						ourText.setText(currentText + buttons2[i].getText());
					}

					//Else a error message is displayed
					else{
						JOptionPane.showMessageDialog(ourText, "Has exceeded limit" , "Error" , JOptionPane.ERROR_MESSAGE);
					}
					break;

				}	
			}
		}
	}
}

