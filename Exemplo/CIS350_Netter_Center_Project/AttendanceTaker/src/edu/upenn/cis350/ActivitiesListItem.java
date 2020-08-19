package edu.upenn.cis350;

/**
 * represents a row in the ActivitiesList
 *
 */
public class ActivitiesListItem {
	private String name;
	
	public ActivitiesListItem(String name){
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
