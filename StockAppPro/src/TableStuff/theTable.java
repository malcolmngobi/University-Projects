package TableStuff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * This class acts as a communicator to the table model to receive results which 
 * allows a table to be constructed.
 * @author Mally
 *
 */
public class theTable extends JFrame {

	//Declaring variables
	private JPanel pane;
	private JLabel label;
	private JScrollPane scrollpane;
	private JTable table; 
	private String stockSym;
	private int startMonth;
	private int startDay;
	private int startYear;
	private int endMonth;
	private int endDay;
	private int endYear;
	private String interval;
	private boolean isSelected;
	
	
	/* Accepts a Double and returned the double as a String in 2 decimal places */
	DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * Deals with the main bulk of this class acting as a gateway. 
	 * 
	 * @param stockSym - StockSymbol
	 * @param startMonth - BeginMonth
	 * @param startDay - BeginDay 
	 * @param startYear - BeginYear
	 * @param endMonth - EndMonth
	 * @param endDay - EndDay 
	 * @param endYear - EndYear
	 * @param interval - Interval of search
	 * @param isSelected - Whether chronological or not
	 */
	public theTable(String stockSym, int startMonth, int startDay, int startYear, 
			int endMonth, Integer endDay, Integer endYear, String interval , boolean isSelected) {

		
		
		super(stockSym + ": " + startYear + "-" + startMonth + "-" + startDay +
				" to " + endYear + "-" + endMonth + "-" + endDay + 
				" (" + interval + ")"); //Creates a title for the Tables
		
		/* Instantiating Variables of this class with variables from another class */
		this.stockSym = stockSym;
		this.startMonth = startMonth;
		this.startDay = startDay;
		this.startYear = startYear;
		this.endMonth = endMonth;
		this.endDay = endDay;
		this.endYear = endYear;
		this.interval = returnLetterAbriv(interval);
		this.isSelected = isSelected;

		createObjects(); // Calls method
		
		//Adds ScrollPane and gives table some features
		pane.add(scrollpane);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		
		// Attempt at Requirement 7.
		TableColumn column = table.getColumnModel().getColumn(6);
		column.setCellRenderer(new CellTextColour());

		/*
		 * Uses various methods and concatenates the results to form a status bar 
		 */
		label = new JLabel("Average: " + Average() + "  Maximum Drawdown: " + maximumDrawdown());
		
		/*
		 * This keeps track of window changes.
		 */
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			/*
			 * Each time a window is opened it is added to a arrayList of a different class
			 * (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			public void windowOpened(WindowEvent arg0) {
				Windowlisten.addWin(theTable.this); // 
			}
			
			
			@Override
			/*
			 * Each time a window is closed it is removed from a arrayList of a different class
			 * (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			public void windowClosed(WindowEvent arg0) {
				Windowlisten.removeWin(theTable.this);
			}
		});
		
		this.setLayout(new BorderLayout()); //Gives Frame a Layout
		this.add(pane, BorderLayout.CENTER); //Adds pane to Center (Table)
		this.add(label, BorderLayout.SOUTH); // Adds Status bar to South

		this.setPreferredSize(new Dimension(640,480)); //Gives table a size
		this.add(scrollpane);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);// Close table when closed. 
		this.pack(); // Makes table smallest possible
		this.setVisible(true); //Allows table to be visible
	}

	/**
	 * Instantiates variables 
	 */
	private void createObjects() {
		// TODO Auto-generated method stub
		pane = new JPanel(); // Allocate Pane which holds table with ScrollPane
		table = new JTable(new theTableModel(stockSym,startMonth,startDay,startYear,
				endMonth,endDay,endYear,interval, isSelected)); //Puts table model in Constructor
		scrollpane = new JScrollPane(table); //Gives table a ScrollPane
		
	}

	/**
	 * Abbreviates text
	 * @param interval - The interval of the query to be searched
	 * @return - A abbrievation of that interval in a format which Yahoo wants.
	 */
	private String returnLetterAbriv(String interval){

		if(interval.equals("Monthly")){
			return "m";
		}
		else if(interval.equals("Daily")){
			return "d";
		}
		else{
			return "w";
		}

	}

	/**
	 * This method calculates the Average result of the Adj Close.
	 * @return - The Result of the calculation
	 */
	private String Average(){

		double total = 0;
		for(int i = 0; i< table.getRowCount(); i++){

			total += Double.parseDouble((String) table.getValueAt(i, 6));
		}
		double average = total/ (double) table.getRowCount();

		return df.format(average); //Uses the Decimal format I specified above 
	}

	/**
	 * This method calculates the maximum drawdown of the Adj Close.
	 * @return - The Result of the calculation
	 */
	private String maximumDrawdown(){

		double MDD = 0; 
		double peak = -99999;

		double[] DD = new double[table.getRowCount()];

		for(int i = 0; i<table.getRowCount() ; i++){

			int top = (table.getRowCount() - 1) - i; // Ensures that we start from the end of the table
			double x = Double.parseDouble((String) table.getValueAt(top, 6));
			if(x > peak){
				peak = x;
			}
			else{
				DD[i] = peak - x;

				if(DD[i] > MDD){
					MDD = DD[i];
				}
			}

		}
		return df.format(MDD); //Uses the Decimal format I specified above 
	}
}
