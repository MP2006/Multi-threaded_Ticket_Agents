package tranppha_CSCI201_Assignment2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util{
	public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.sss");
		Date date = new Date();
		String time = "[" + dateFormat.format(date) + "]"; //2016/11/16 12:08:43
		return time;
	}
}
