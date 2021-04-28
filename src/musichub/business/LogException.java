package musichub.business;

import java.lang.Exception;
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

public class LogException extends Exception{
	
	public LogException(String level, String msg) {
		
		//gets the logger that conducts the message to the handler
		Logger logger = Logger.getLogger("logger"); 
		FileHandler filehandler;
		
		try {
			/*FileHandler allows writing runtime errors, warnings and information in the file "errors.txt"
			* Their corresponding time stamp (date and time) are displayed by default since we use a logger.
			*/
			filehandler = new FileHandler("files/errors.txt", true); 
			logger.addHandler(filehandler);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			filehandler.setFormatter(formatter);
			
			//A level is associated to each logger:
			switch (level){
			case "SEVERE":
				logger.log(Level.SEVERE, msg);
				break;
			case "WARNING":
				logger.log(Level.WARNING, msg);
				break;
			case "CONFIG":
				logger.log(Level.CONFIG, msg);
				break;
			case "INFO":
				logger.log(Level.INFO, msg);
				break;
			default:
				logger.log(Level.INFO, msg);
				break;
			}
			
			//Output on the console
			System.out.println("[" + level + "] " + msg);
		}
		catch(SecurityException e){
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
