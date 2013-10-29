package TableStuff;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;
import URLstuff.URLReader;

/**
 * This class constructs the rows and columns of a table.(And headings)
 * @author Mally
 *
 */
public class theTableModel extends AbstractTableModel {
	
	private String[] mainInfo;
	private Object[][] rowData;
	private Object[][] chronoArr;
	String[] columnNames;
	
	/**
	 * Deals with the main bulk of this class acting as a gateway. 
	 * 
	 * @param symbol - StockSymbol
	 * @param startMonth - BeginMonth
	 * @param startDay - BeginDay
	 * @param startYear - BeginYear
	 * @param endMonth - EndMonth
	 * @param endDay - EndDay
	 * @param endYear - EndYear
	 * @param interval - Interval of search
	 * @param isSelected - Whether chronological or not
	 */
	public theTableModel(String symbol, int startMonth, int startDay, int startYear, 
			int endMonth, int endDay, int endYear, String interval, boolean isSelected){
		
		/* Input a contatenated url with variables to the URLReader class and store the result in a string */
		String sData = URLReader.readURL("http://ichart.yahoo.com/table.csv?s=" + symbol + "&a=" + startDay + "&b=" + startMonth + 
				"&c=" + startYear + "&d=" + endDay + "&e=" + endMonth + "&f=" + endYear + "&g=" + interval);
		
		
		/* Splitting the Yahoo search results into a single array containing individual rows of specific data*/
		mainInfo = sData.split("\n");
		
		/*
		 * The 2D array which will contain the sorted results to be input into the table
		 */
		rowData = new Object[mainInfo.length-1][7];
		
		/* If order selected by the user via a check box is not chronological then..*/
		
		if(!isSelected){
		/*We loop through the Google results returned then split them in such a way each of these
		 * split string objects(each specific result) go into each of the columns specified thus filling the rowData Array */
			
	    for(int j =0; j<mainInfo.length-1; j++){
	    	
	    	rowData[j]=mainInfo[j+1].split(","); // I do not include the first index as this contains the column headers
	    	
	    }
		}
		else{
			chronoArr = new Object[mainInfo.length-1][7];
			
			/*We loop through the Google results returned then split them in such a way each of these
			 * split string objects(each specific result) go into each of the columns specified thus filling the chronoArr Array */
			for(int j =0; j<mainInfo.length-1; j++){
			    	
			    	chronoArr[j]=mainInfo[j+1].split(","); // I do not include the first index as this contains the column headers
			    	
			    }
			
			int countRow = chronoArr.length; //Separate variable containing the amount of rows of the table
			
			/*
			 * This for loop then fills the rowData array in the reverse order of the chronoArr starting from the bottom
			 * upwards
			 */
			for(int j =0; j<mainInfo.length-1; j++){
				countRow--;
		    	for(int i = 0; i<7; i++){
		    	rowData[j][i] = chronoArr[countRow][i];
		    	}
		    }
			
		}
	    
	    columnNames = mainInfo[0].split(","); // The first row of the google return results added to the single array
	    
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		
		return columnNames.length;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		
		return rowData.length;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column) {
		
		return rowData[row][column];
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int col){
		
		return columnNames[col];
	}

}
