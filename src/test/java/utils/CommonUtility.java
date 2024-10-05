package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonUtility {
	
	public static String getDate(String reqdate) {
		String rDate = null;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/MMMM/u", Locale.ENGLISH);
		LocalDateTime now = LocalDateTime.now();
		if(reqdate.equalsIgnoreCase("Today")) {
			System.out.println("**************"+dateFormatter.format(now));
			rDate = dateFormatter.format(now);
		}else if(reqdate.equalsIgnoreCase("Tomorrow")) {
			
			System.out.println("**************"+dateFormatter.format(now.plusDays(1)));
			rDate = dateFormatter.format(now.plusDays(1));
		}
		return rDate;
	}

}
