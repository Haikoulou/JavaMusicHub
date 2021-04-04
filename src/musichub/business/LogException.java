package musichub.business;

import java.io.IOException;
import java.util.logging.*;

/**
 * 
 * @author Julie 
 *
 */

/**
 * @param args
 */

/* Implementation of the error logging system*/

public class LogException{
	
	public static void main(String[] args) {
		
		//gets the logger that conducts the message to the handler
		Logger logger = Logger.getLogger("logger"); 
		
		//A level is associated to each logger:
		logger.log(Level.SEVERE, "Severe error");
		logger.log(Level.WARNING, "Warning message");
		logger.log(Level.CONFIG, "Configuration error");
		logger.log(Level.INFO, "Information message");
		
		
		/*FileHandler allows writing runtime errors, warnings and information in the file "errors.txt"
		* Their corresponding time stamp (date and time) are displayed by default since we use a logger.
		*/
		try {
			FileHandler filehandler = new FileHandler("errors.txt"); 
			logger.addHandler(filehandler);
		}
		catch(SecurityException e){
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
