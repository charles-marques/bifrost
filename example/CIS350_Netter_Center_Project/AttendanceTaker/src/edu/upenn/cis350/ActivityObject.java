package edu.upenn.cis350;

import java.util.Date;

public class ActivityObject implements Comparable {

	    private String id;
	    private String name;
	    private String className;
	    private Date date;
	    private int count;

	    public ActivityObject(){
	    	this.name = "";
	    }
	    // constructor
	    public ActivityObject(String id, String name, String className, Date date, int count){
	        this.id = id;
	        this.name = name;
	        this.className = className;
	        this.date = date;
	        this.count = count;
	    }
	    
	    public ActivityObject(String name){
	       
	        this.name = name;
	    }

	    // getting ID
	    public String getID(){
	        return this.id;
	    }
	 
	    // setting id
	    public void setID(String id){
	        this.id = id;
	    }
	 
	    // getting name
	    public String getName(){
	        return this.name;
	    }
	 
	    // setting name
	    public void setName(String name){
	        this.name = name;
	    }
	    
	    // set name of activity as it is in the Parse database
	    public String getClassName(){
	    	return this.className;
	    }
	    
	    // get name of activity as it is in the Parse database
	    public void setClassName(String className){
	    	this.className = className;
	    }
	 
	    // getting userId
	    public Date getDate(){
	        return this.date;
	    }
	 
	    // setting userId
	    public void setDate(Date d){
	        this.date = d;
	    }
	
	    public int getCount() {
	    	return this.count;
	    }
	    
		public int compareTo(Object other) {
			if (this.getCount() > ((ActivityObject) other).getCount())
				return -1;
			else if (this.getCount() < ((ActivityObject) other).getCount())
				return 1;
			return 0;
		}
}
