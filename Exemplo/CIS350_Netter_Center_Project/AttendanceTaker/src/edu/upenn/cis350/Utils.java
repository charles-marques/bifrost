package edu.upenn.cis350;

import java.util.Calendar;
import java.util.List;


/**
 * Utility/helper methods that are used throughout
 * the classes of the application.
 */
public class Utils {

	//Format MM/DD/YYYY
	public static String getTodaysDate() {
	    final Calendar c = Calendar.getInstance();
	    return(new StringBuilder()
	            .append(c.get(Calendar.MONTH) + 1).append("_")
	            .append(c.get(Calendar.DAY_OF_MONTH)).append("_")
	            .append(c.get(Calendar.YEAR))).toString();
	}

	//Format HH:MM:SS
	public static String getCurrentTime() {
	    final Calendar c = Calendar.getInstance();
	    int hour = c.get(Calendar.HOUR_OF_DAY);
	    int minute = c.get(Calendar.MINUTE);
	    String ampm = "";
	    if(hour==0){
	    	hour = 12;
	    	ampm = "AM";
	    }
	    else if(hour<12){
	    	ampm = "AM";
	    }
	    else if(hour==12){
	    	ampm = "PM";
	    }
	    else{
	    	hour = hour-12;
	    	ampm = "PM";
	    }
	    StringBuilder s = new StringBuilder().append(hour).append(":")
	    		.append(minute).append(" ").append(ampm);
        return s.toString();
	}
	
	public static boolean containsName(List<StudentObject> studentList, String name){
		for(StudentObject student: studentList){
			if(student.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public static StudentObject getStudentFromList(List<StudentObject> studentList, String name){
		for(StudentObject student: studentList){
			if(student.getName().equals(name)){
				return student;
			}
		}
		return null;
	}
}
