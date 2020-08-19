package edu.upenn.cis350;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Custom adapter for the ListView used in ActivitiesList
 * Allows multiple pieces of data to be placed in each row of the list
 *
 */
public class ActivitiesAdapter extends BaseAdapter implements OnClickListener{
	
	private Context context;
	private List<ActivityObject> listActivities;
	
	//constructor
	public ActivitiesAdapter(Context context, List<ActivityObject> listActivities){
		this.context = context;
		this.listActivities = listActivities;
	}
	
	public int getCount() {
        return listActivities.size();
    }
	
	public Object getItem(int position) {
        return listActivities.get(position);
    }
	
	public long getItemId(int position) {
        return position;
    }
	
	public View getView(int position, View convertView, ViewGroup viewGroup){
		//gets entry associated with current row in ListView
		ActivityObject entry = listActivities.get(position);
		
		//handles null case
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_item, null);
		}
		// handles Student Name
		TextView tvActivityName = (TextView) convertView.findViewById(R.id.tvActivityName);
		tvActivityName.setText(entry.getName());
        
		EditText textEdit = (EditText) convertView.findViewById(R.id.hidden_edit_view);
		textEdit.setText(entry.getName());
        
        return convertView;
	}

	/** handles 'edit' button for each list item **/
	public void onClick(View v) {
		
    }
	
}