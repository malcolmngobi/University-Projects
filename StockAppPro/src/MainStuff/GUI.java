package MainStuff;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import TableStuff.theTable;
import TableStuff.Windowlisten;

/**
 * This Class deals with the Main GUI window which displays the selection tools needed to make 
 * a search query for a company in a specific time scale 
 * @author Mally
 *
 */
public class GUI extends JFrame {

	/* Declaring variables */
	private JLabel stockSymLabel,beginLabel,endLabel,intervalLabel;
	private JTextField stockText;
	private JComboBox intervalList,days,months,years,daysEnd,monthsEnd,yearEnd;
	private JButton lookupQ;
	private JPanel one, two,three, empty,empty1, empty2;
	private String[] monthsArr = {"January","February","March","April","May","June","July",
			"August","September","October","November","December"};
	private String[] integerListArr = {"Monthly","Weekly","Daily"};
	private JCheckBox chrono;
	
	private Date date1;
	private Date date2;
	private Calendar begin;
	private Calendar end;
	
	/**
	 * Class Constructor which is the main base for this class which deals with
	 * GUI layout mainly but links to other methods which do other jobs for this class.
	 */
	public GUI(){
		super("Stock Market App Pro!!");
		createInstances(); //Calls the following method
		
		/* Instantiating JPanels which will be used as empty space 
		 * which allows us to formalize a specific design to the layout */
		empty = new JPanel();
		empty1 = new JPanel();
		empty2 = new JPanel();
		
		/* The main Grid Layout(this will contain 3 columns and 4 rows of Grid objects)
		 *  which will be in the center of the Frame */ 
		GridLayout grid1 = new GridLayout(3,4);
		grid1.setVgap(4); //Setting the Vertical Gap between grid objects
		grid1.setHgap(3); //Setting the Horizontal Gap between grid objects
		
		/* Main panel where most of the Combo boxes, Labels, Check Box and button 
		 *  is added to with a custom gridLayout variable parsed into the JPanel's 
		 *  constructor. Allowing the JPanel to be set with this custom layout */ 
		one = new JPanel(grid1);
		
		one.add(beginLabel);
		one.add(months);
		one.add(days);
		one.add(years);
		one.add(endLabel);
		one.add(monthsEnd);
		one.add(daysEnd);
		one.add(yearEnd);
		
		one.add(intervalLabel);
		one.add(intervalList);
		one.add(chrono);
		
		/* Instantiating NorthBound JPanel of the Main Frame */
		two = new JPanel(new BorderLayout());
		
		/* Adding Items to this Panel */
		two.add(stockSymLabel, BorderLayout.WEST);
		two.add(stockText, BorderLayout.CENTER);

		/* Instantiating SouthBound JPanel of the Main Frame */
		three = new JPanel(new GridLayout(1,4));
		
		/* Adding Items to this Panel */
		three.add(empty);
		three.add(lookupQ);
		three.add(empty1);
		three.add(empty2);
		
		/* Setting the JFrames Layout and Adding the North,South and Center Bound Panels to it */
		this.setLayout(new BorderLayout());
		this.add(one, BorderLayout.CENTER);
		this.add(two, BorderLayout.NORTH);
		this.add(three, BorderLayout.SOUTH);
		
		/* Allows the Frame to be the smallest size possible */
		this.pack();
		
		/* Terminates program on close of this frame */
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		/* Allows frame to be visible as opposed to be invisible */
		this.setVisible(true);
	}

	
	/**
	 * This method returns nothing, but is the main method which deals with instantiating the separate 
	 * objects needed to be viewed on our MainFrame and adding functionality to these Items.
	 */
	@SuppressWarnings("deprecation")
	private void createInstances() {
		
		/* Instantiating various objects and parsing text into their constructors 
		 * which allows assigning of specific things */
		
		stockSymLabel = new JLabel("Stock Symbol     " + "\t");
		beginLabel = new JLabel("Begin:" + "\t");
		endLabel = new JLabel("End:" + "\t");
		intervalLabel = new JLabel("Interval" + "\t" );
		
		
		intervalList = new JComboBox(integerListArr); //Creates a comboBox containing the contents of the array parsed
		lookupQ = new JButton("Lookup");
		chrono = new JCheckBox("Chronological?");

		/* Adding Functionality to the button in the Main GUI Frame */
		lookupQ.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				/* Gets text which is queried*/
				String regExTxt = stockText.getText();
				
				/*  Using the patterns class and the matches method in order to implement a form of validation
				 * 	which ensures that the text entered should be Upper case letters or digits or contains a dot and this should occur
				 *  the minimum times once and max 8. Returns a boolean if this condition is met or not*/
				
				boolean check = Pattern.matches("([A-Z]|\\d|\\.){1,8}", regExTxt);
				
				/* Instantiating a date object and parsing into the constructor a year, month and date.
				 * This is to be used later in the code for a comparision. */
				date1 = new Date( (int) yearEnd.getSelectedItem(),
						monthsEnd.getSelectedIndex(), (int) daysEnd.getSelectedItem()); //Used for endDate
				
				date2 = new Date( (int) years.getSelectedItem(),
						months.getSelectedIndex(), (int) days.getSelectedItem()); //Used for start date
				
				/* The interval the user would like to see their stock query in */
				String interval = (String) intervalList.getSelectedItem();
				
				/* Concatenates various fields to produce a title for the Table to be created */
				String title = stockText.getText() + ": " + (int) years.getSelectedItem() + "-" + months.getSelectedIndex() + "-" + (int) days.getSelectedItem() +
						" to " + (int) yearEnd.getSelectedItem() + "-" +  monthsEnd.getSelectedIndex()  + "-" +  (int) daysEnd.getSelectedItem() + " (" + interval + ")";
				
				
				if(Windowlisten.checkWin(title) == false){ //If the following query has already been made.. 
					
				if(date2.after(date1)){ //If the begin date selected is after the end date
					
					JOptionPane.showMessageDialog(GUI.this, "Please enter a start date before the end date" , "Error" , JOptionPane.ERROR_MESSAGE);
				}
				else if(!check){ // If the boolean returns false
					
					JOptionPane.showMessageDialog(stockText, "Text must be maximum 8 characters in length(uppercase,digits and period)!" , "Error" , JOptionPane.ERROR_MESSAGE);
					stockText.setText("");
				}
				else{
				
				boolean isSelected = chrono.isSelected(); //Declaring and instantiating the check box boolean on our GUI so when pressed true else false
				
				/* Creating a new theTable object */
				new theTable(stockText.getText(), months.getSelectedIndex(), (int) days.getSelectedItem(), (int) years.getSelectedItem(), 
						monthsEnd.getSelectedIndex(),(int) daysEnd.getSelectedItem(), (int) yearEnd.getSelectedItem(), interval, isSelected );
				}
				}
				
			}
		});
		
		/* Instantiating TextField */
		stockText = new JTextField();
		
		
		
		months = new JComboBox(monthsArr);
		
		/** 
		 * Creates and adds an anonymous inner class for an ActionListener. 
		 * Its function is to do remove all items in the Days combo box, while it then checks the 
		 * method checkDays, to query how many days to add to the relevant combo box.
		 */
		months.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				days.removeAllItems(); //Removes Items from combo box
				checkDays(months, days); //Calls the following method 
				
			}
		});
		
		/* Instantiating ComboBoxes */
		years = new JComboBox();
		days = new JComboBox();
		
		
		monthsEnd = new JComboBox(monthsArr); // Instantiating ComboBox with a array of months parsed as a selection
		
		monthsEnd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				daysEnd.removeAllItems(); //Removes Items from combo box
				checkDays(monthsEnd, daysEnd); //Calls the following method 
				
			}
		});
		
		/* Instantiating ComboBoxes */
		yearEnd = new JComboBox();
		daysEnd = new JComboBox();
		
		/*
		 * Adding years to ComboBox via a loop which has a start index of the first year we would like to add 
		 * which then is added to the combo box and one by one the years are added to the combo box via an 
		 * incremented i variable
		 */
		
		for(int i = 1970; i<=2012; i++){
			years.addItem(i);
			yearEnd.addItem(i);
		}
		
		/* Here I create a new calendar which uses the computer as a base to find out the current date 
		 *  time etc. With this Calendar instance I then ask to return specific items of the calendar; 
		 *  month, day and year 
		 *  */
		Calendar currentCal = Calendar.getInstance();
		
		monthsEnd.setSelectedItem(returnString(currentCal.get(Calendar.MONTH)));// Using a method to return the String month rather than a number
		daysEnd.setSelectedItem(currentCal.get(Calendar.DAY_OF_MONTH)); // Sets the combo box to the current month
		yearEnd.setSelectedItem(currentCal.get(Calendar.YEAR)); // Sets the combo box to the current year
		
		/* Default date set when user opens application for the start date of the query */
		months.setSelectedItem("January"); //Sets combo box to January
		days.setSelectedItem(1); // Sets combo box to 1 
		years.setSelectedItem(2000); // Sets combo box to 200 
		
		
		
	}
	
	/**
	 * This method checks the month selected, then inputs the amount of days in the month separately
	 * into the combo box allocated for days.  
	 * 
	 * @param mon - Month selected via the combo box parsed through 
	 * @param day - Day selected via the relevant combo box parsed through 
	 */
	private void checkDays(JComboBox mon, JComboBox day){
		if(mon.getSelectedItem().equals("January") || mon.getSelectedItem().equals("March") ||
				mon.getSelectedItem().equals("May") || mon.getSelectedItem().equals("July") || 
				mon.getSelectedItem().equals("August") || mon.getSelectedItem().equals("October") ||
				mon.getSelectedItem().equals("December"))
		for(int i = 1; i<=31; i++){
			day.addItem(i);
		}
		else if(mon.getSelectedItem().equals("April") || mon.getSelectedItem().equals("June") ||
				mon.getSelectedItem().equals("September") || mon.getSelectedItem().equals("November")){
			for(int i = 1; i<=30; i++){
				day.addItem(i);
			}
		}
		else{
			for(int i = 1; i<=28; i++){
				day.addItem(i);
			}
		}
	}
	
	/**
	 * This method converts the integer value of a month to the String
	 * known value. eg val = 1 then method returns String January
	 *  
	 * @param val - The integer value of the current month
	 * @return The String value of the current month
	 */
	private String returnString(int val){
		String curMonth = null;
		switch(val){
		case 0: curMonth = "January";
				break;
		case 1: curMonth = "February";
			    break;
		case 2: curMonth = "March";
				break;
		case 3: curMonth = "April";
				break;
		case 4: curMonth = "May";
				break;
		case 5: curMonth = "June";
			    break;
		case 6: curMonth = "July";
				break;
		case 7: curMonth = "August";
				break;
		case 8: curMonth = "September";
				break;
		case 9: curMonth = "October";
				break;
		case 10: curMonth = "November";
				break; 
		case 11: curMonth = "December";
				break;
		default: break;
		}
		return curMonth;
		
	}
	
}
