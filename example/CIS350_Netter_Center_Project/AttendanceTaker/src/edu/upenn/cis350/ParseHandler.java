package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/***
 * Handles interaction with online Parse database.
 * In general, methods convert to ActivityObject or StudentObject
 *
 */
public class ParseHandler {

	/**
	 * given a name, adds an activity to Parse datastore
	 * @param name
	 */
	public static void addActivity(String name){
        ParseObject activity = new ParseObject("Activity");
        ParseQuery query = new ParseQuery("Activity");
        int id = 0;
        try {
			id = query.count()+1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        StringTokenizer tokenizer = new StringTokenizer(name);
        String className = tokenizer.nextToken();
        activity.put("ClassName", className);
        activity.put("DisplayName", name);
        activity.put("ID", id);
        activity.put("count", 0);
        activity.saveInBackground();
        
        ParseObject newActivity = new ParseObject(className);
        newActivity.put("schoolYear", "2011-2012");
        newActivity.put("program", name);
        newActivity.saveInBackground();
	}
	
	public static void addStudentToActivity(Double studentID, String activity) {
		ParseQuery query = new ParseQuery("Student");
		query.whereEqualTo("ID", studentID);
		try{
			ParseObject p = query.getFirst();
			Log.v("add", "ID: " + studentID + " activity: " + activity);
			p.put(activity, 1);
			p.saveInBackground();
		} catch(ParseException e){
			e.printStackTrace();
		}
	}
	
	public static void removeStudentFromActivity(Double studentID, String activity) {
		ParseQuery query = new ParseQuery("Student");
		query.whereEqualTo("ID", studentID);
		try{
			ParseObject p = query.getFirst();
			Log.v("add", "ID: " + studentID + " activity: " + activity);
			p.put(activity, 0);
			p.saveInBackground();
		} catch(ParseException e){
			e.printStackTrace();
		}
	}
	
	public static ActivityObject getActivityByName(String name){
		ActivityObject activity = new ActivityObject();
		ParseQuery query = new ParseQuery("Activity");
		query.whereEqualTo("ClassName", name);
		
		try {
			ParseObject a = query.getFirst();	
			activity = new ActivityObject(a.getObjectId(),a.getString("DisplayName"),
					a.getString("ClassName"), a.getUpdatedAt(), a.getInt("count"));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return activity;
	}
	
	public static List<ActivityObject> getAllActivities(){
		ParseQuery query = new ParseQuery("Activity");
		List<ParseObject> queryList = new ArrayList<ParseObject>();
		List<ActivityObject> activityList = new ArrayList<ActivityObject>();
		try{
			queryList = query.find();
			for(ParseObject p : queryList){
				activityList.add(new ActivityObject(p.getObjectId(), p.getString("DisplayName"),
						p.getString("ClassName"), p.getUpdatedAt(), p.getInt("count")));
			}
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		Collections.sort(activityList);
		return activityList;
	}
	
	public static void increaseActivityCount(String activity){
		ParseQuery query = new ParseQuery("Activity");
		query.whereEqualTo("ClassName", activity);
		try {
			ParseObject a = query.getFirst();
			int count = a.getInt("count");
			count++;
			Log.v("count", count+"");
			a.put("count", count);
			a.saveInBackground();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void addStudent(StudentObject s){
		ParseObject student = new ParseObject("Student");
		student.put("Name", s.getName());
        student.saveInBackground();
	}
	
	public static List<StudentObject> getAllStudents(){
		ParseQuery query = new ParseQuery("Student");
		List<ParseObject> queryList = new ArrayList<ParseObject>();
		List<StudentObject> studentList = new ArrayList<StudentObject>();
		try{
			queryList = query.find();
			Log.v("hello", queryList.size()+"");
			for(ParseObject student : queryList){
				studentList.add(new StudentObject(student.getNumber("ID").toString(), student.getNumber("Parse_1112GradeLevel").intValue(),
						student.getString("Name"),student.getString("phoneNumber"),student.getString("Address"), student.getString("School"), student.getString("Site"),"Program?",
						student.getString("emergencyContactName"), student.getString("emergencyContactRelationship")));
			}
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		return studentList;
	}
	
	public static List<StudentObject> getStudentsForActivity(String activity) {
		ParseQuery query = new ParseQuery("Student");
		query.whereEqualTo(activity, 1);
		List<ParseObject> queryList = new ArrayList<ParseObject>();
		List<StudentObject> studentList = new ArrayList<StudentObject>();
		
		try{
			queryList = query.find();
			Log.v("hello", queryList.size()+"");
			for(ParseObject student : queryList){
				studentList.add(new StudentObject(student.getNumber("ID").toString(), student.getNumber("Parse_1112GradeLevel").intValue(),
						student.getString("Name"),student.getString("phoneNumber"),student.getString("Address"), student.getString("School"), student.getString("Site"),"Program?",
						student.getString("emergencyContactName"), student.getString("emergencyContactRelationship")));
			}
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		return studentList;
		
	}
	
	public static void renameActivity (String className, String newName) {
		ParseQuery q = new ParseQuery("Activity");
    	q.whereEqualTo("ClassName", className);
		try {
			ParseObject p = q.getFirst();
			p.put("DisplayName", newName);
			p.saveInBackground();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	public static void submitActivityAttendance(ActivityObject a, List<StudentObject> studentList){
		String date = Utils.getTodaysDate();
		String time = Utils.getCurrentTime();

		ParseQuery query = new ParseQuery(a.getClassName());
		//ParseQuery query = new ParseQuery("SubmitTest");
		try {
			List<ParseObject> rows = query.find();
			for(ParseObject row: rows){
				String name = row.getString("Name");
				if(Utils.containsName(studentList, name)){
					StudentObject stud = Utils.getStudentFromList(studentList, name);
					if(stud!=null){
						String studentStatus = stud.getStatus();
						String studentComments = stud.getComments();
						//if student is absent, don't log anything
						if(!studentStatus.equals("Absent")){
							String columnName = studentStatus + "_" + date;
							if(!row.containsKey(columnName)){
								row.put(columnName, time);	
								row.put("comments", studentComments);
								row.saveInBackground();
								Log.v("Attendance", "Attendance taken for " + stud.getName() + " " + columnName + " " + time);
							}
						}
					}
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void signUpUser(String username, /*String email,*/ String password, Context context){
		CharSequence text = "";
		ParseQuery query = ParseQuery.getUserQuery();	
		String stored_username;
		List<ParseObject> userlist = new ArrayList<ParseObject>();
		int flag = 1;
		try {
			userlist = query.find();
			int a = query.count();
			System.out.println(a);	
			for(ParseObject users: userlist){		
				stored_username = users.getString("username");
				Log.v("I GET HERE2", users.getString("username"));
				if (stored_username.equals(username)) {	
				text = "Username already exists please try a different name";
				int duration = Toast.LENGTH_SHORT;
		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    	flag = 0;
				}	
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		
		if (flag == 1) {
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		// user.setEmail(email);
		try {
			user.signUp();
			text = "Registration successful!";
		} catch (ParseException e) {
    		text = "Registration failed. Please try again.";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
		}
	}
}
	public static ParseUser checkLogin(String username, String password){
		try {
			ParseUser user = ParseUser.logIn(username, password);
			if(user!=null){
				return user;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void editStudent(String name, String nameEdit, String phoneEdit, 
									String addressEdit, String idEdit, String ecnEdit, 
									String ecrEdit, String siteEdit, String gradeEdit, 
									String schoolEdit){
		ParseQuery query = new ParseQuery("Student");
    	query.whereEqualTo("Name", name);
    	try {
    		ParseObject a = query.getFirst();
    		if(!nameEdit.equals("")){
    			a.put("Name", nameEdit);
    		}
			a.put("phoneNumber", phoneEdit);
			a.put("emergencyContactRelationship", ecrEdit);
			a.put("emergencyContactName", ecnEdit);
			a.put("Address", addressEdit);
			a.put("ID", Double.parseDouble(idEdit));
			a.put("School", schoolEdit);
			a.put("Site", siteEdit);
			a.put("Address", addressEdit);
			a.put("Parse_1112GradeLevel", Integer.parseInt(gradeEdit));
			a.saveInBackground();
			Log.v("asdf", "edit successful " + addressEdit);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
