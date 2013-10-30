import java.text.DecimalFormat;

import javax.swing.JLabel;

/*
 * This classes function is to deal with getting all the data from a Google query 
 * and presenting and outputting back to the GUI Class.
 */

public class URLClass {


	private String priceFormula;
	private char firstChar;
	private String[] companyInfos;
	private String[] companyArray;

	/*
	 * The bulk of this class with deals with all actions and processing
	 * 
	 * @param stockQuery - This is the stock symbols we would like to process.
	 * @param window - Main GUI window.
	 * @param countSpace - Counter variable telling us how many spaces have currently been entered.
	 */

	public URLClass(String stockQuery, GUI window, int countSpace){

		/*
		 * This accesses another file(class) given to us which reads URL's . 
		 * And i have given the stock names to be input by the user as a parameter which should be concatenated with the URL 
		 * to give a successful query bringing back the correct information which can allow me to output specific information.
		 */
		String sData = URLReader.readURL ("http://finance.yahoo.com/d/quotes.csv?s=" + stockQuery + "&f=sl1pdj1xn");

		//Creates a new instance of the DecimaFormat with the allowing the format to be set to 2 decimal places via the variable df.
		DecimalFormat df = new DecimalFormat("#.##");

		if(sData != null){
			// do something with the data

			/* For the string sData the "" are replaced with empty strings . 
			 * The sData string is then split in accordance to each line.
			 */
			sData = sData.replaceAll("\"", "");
			companyInfos = sData.split("\n");

			//String variable which denotes the start of a output presentation.
			String output = "<html><body>";

			/* For loop which loops through the indexes of the split sData string .
				   And each index contains the data for each company which has been selected by the user.
				   Which is then split again in an array of strings allocating each index with a separate unique stock related item
				   This is where the currentPrice and previousPrice is worked out and presented in the forms of priceChange and percentileChange's . 
				   Which is all concatenated and rounded to a string which presents all the price information.

				   Which is then concatenated to the start of the output presentation which denotes the start of a HTML. 

				   Where this process is looped going through each of the company names which have been given to the class as a parameter.
			 */
			for (int i = 0; i < companyInfos.length; i++) {

				//Try this code if no exception has occurred 
				try{
					String company = companyInfos[i];
					companyArray = company.split(",", 7);
					double currentPrice = Double.parseDouble(companyArray[1]);
					double previousPrice = Double.parseDouble(companyArray[2]);
					double priceSubt = currentPrice - previousPrice;
					double priceChange = (priceSubt *100)/100;
					double percentileChange = ((priceSubt/previousPrice)*100);
					priceFormula = df.format(priceChange) + "(" + df.format(percentileChange) + "%)" + "\n";

					output += "<br>" + "<b>" +companyArray[6] +"</b>" +"<br>" + "Price:   " + companyArray[1] + Currency() + "<br>" + "PriceChange:   " 
							  + checkPrice() + "<br>" + "Dividend: "+ companyArray[3] + "<br>" + "Market Cap:   " + companyArray[4] + "<br>" + "Stock Exchange:   " 
							  + companyArray[5] +"<br>";
				}

				//Catch the exception which has occurred and display message 
				catch(NumberFormatException e){
					output +=  "<html><br>" + "<b>" + "<font color=red>" + "\tInvalid input symbol" + "</font>" + "<b>" + "<br>";
				}
			}

			//HTML tag is closed up and set as text to the output Screen (JLabel).
			output += "</body></html>";
			window.outputInfo.setText(output);


		}
		else{
			System.out.println("Data has not been receieved");
		}
	}


	/*
	 * Checks whether the priceChange is negative or positive then reflects then colour codes the output
	 * @return Returns Red if a decline in profit(PriceChange) and Returns Green if growth in profit(PriceChange). 
	 */
	private String checkPrice(){
		firstChar = priceFormula.charAt(0);
		String colourCode;
		if(firstChar == '-' ){
			colourCode = "<font color=red>"+ priceFormula + "</font>";
		}
		else{
			colourCode = "<font color=green>"+ priceFormula + "</font> ";
		}
		return colourCode;
	}

	/*Checks whether the Stock Exchange element in the array is equal to a specific string . If so then 
	 *currency is set besides the price
	 *
	 *@return Returns the String currency 
	 */
	private String Currency(){
		String currency;

		if(companyArray[5].equals("NasdaqNM") || companyArray[5].equals("NYSE")){
			currency = "$";
		}
		else if(companyArray[5].equals("Brussels") || companyArray[5].equals("Paris") ){
			currency = "EUR";
		}
		else if(companyArray[5].equals("HKSE")){
			currency = "HK$";
		}
		else if(companyArray[5].equals("London")){
			currency = "p(ence)";
		}
		else if(companyArray[5].equals("SES")){
			currency = "S$";
		}
		else if(companyArray[5].equals("NCM")){
			currency = "A$";
		}
		else{
			currency = "";
		}

		return currency;

	}

}

