package com.parentlink.riotapitest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {
	
	public final static String CHAMPION_NAME = "com.parentlink.riotapitest.RIOT";
	
	private SharedPreferences champNameEntered;
	
	private ListView champListView;
	
	private EditText champNameEditText;
	
	private DallinIsAwesome dallin;
	
	private Button enterChampNameButton;
	public Button deleteAllButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		champNameEntered = getSharedPreferences("champList",MODE_PRIVATE);
		
		champListView= (ListView) findViewById(R.id.champListView);
		
		champNameEditText= (EditText) findViewById(R.id.champNameEditText);
		
		enterChampNameButton= (Button) findViewById(R.id.enterChampNameButton);
		deleteAllButton= (Button) findViewById(R.id.deleteAllButton);
		
		enterChampNameButton.setOnClickListener(enterChampNameButtonListener);
		deleteAllButton.setOnClickListener(deleteChampListButtonListener);
		
		dallin = new DallinIsAwesome();
		
		champListView.setAdapter(dallin);
		
		dallin.narwhal.addAll(champNameEntered.getAll().keySet());
	}
	
	private class DallinIsAwesome extends BaseAdapter{

			public List<String> narwhal;
			
			public DallinIsAwesome() {
				narwhal = new ArrayList<String>();
			}
			
			@Override
			public int getCount() {
				
				return narwhal.size();
				
			}

			@Override
			public Object getItem(int arg0) {
				
				return narwhal.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return getItem(arg0).hashCode();
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				
				if( arg1 == null)
				{
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					arg1 = inflater.inflate(R.layout.champ_info_row, null);
					
					Button dallinConfusesMe = (Button) arg1.findViewById(R.id.individualChampDeleteButton);
					
					dallinConfusesMe.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							
							View row = (View) arg0.getParent();
							
							int index= (Integer)row.getTag();
							
							String champName= narwhal.remove(index);
							
							champNameEntered.edit().remove(champName).commit();
							
							notifyDataSetChanged();
						}
						
					});
				}
				
				
				
				arg1.setTag(arg0);
				
				TextView newChampTextView = (TextView) arg1.findViewById(R.id.championNameTextView);
				
				newChampTextView.setText((String)getItem(arg0));
				
				Button champInfoButton = (Button) arg1.findViewById(R.id.champInfoButton);
				
				champInfoButton.setOnClickListener(getChampActivityListener);
				
				Button champWebButton = (Button) arg1.findViewById(R.id.champWebButton);
				
				champWebButton.setOnClickListener(getChampInfoFromWebsiteListener);
				return arg1;
			}
			
		}

	private void saveChampName(String newChamp){
		
		String isTheChampNew = champNameEntered.getString(newChamp, null);
		
		SharedPreferences.Editor preferencesEditor = champNameEntered.edit();
		
		preferencesEditor.putString(newChamp, newChamp);
		preferencesEditor.apply();
		
		if(isTheChampNew == null){
			
			dallin.narwhal.add(newChamp);
			Collections.sort(dallin.narwhal);
			//updating the list
			dallin.notifyDataSetChanged();
		}
	}
	
	public OnClickListener enterChampNameButtonListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
			if(champNameEditText.getText().length() > 0)
			{
				saveChampName(champNameEditText.getText().toString());
				
				champNameEditText.setText("");
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				
				imm.hideSoftInputFromWindow(champNameEditText.getWindowToken(), 0);
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				
				builder.setTitle(R.string.invalid_champion_name);
				
				builder.setPositiveButton(R.string.ok, null);
				
				builder.setMessage(R.string.missing_champion_name);
				
				AlertDialog theAlertDialog = builder.create();
				
				theAlertDialog.show();
			}
			
		}
		
	};
	
	public OnClickListener deleteChampListButtonListener = new OnClickListener(){
		
		

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deleteAllChamps();
			
			SharedPreferences.Editor preferencesEditor = champNameEntered.edit();
			preferencesEditor.clear();
			preferencesEditor.apply();
		}
	};
	
	private void deleteAllChamps(){
		
		dallin.narwhal.clear();
		dallin.notifyDataSetChanged();
	}
	
	public OnClickListener getChampActivityListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			View row = (View) v.getParent();
			
			String champName = dallin.narwhal.get((Integer)row.getTag());
			
			Intent intent = new Intent(MainActivity.this, ChampInfoActivity.class);
			
			intent.putExtra(CHAMPION_NAME, champName);
			
			startActivity(intent);
		}
		
	};
	
	public OnClickListener getChampInfoFromWebsiteListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			//Gets the Parent of the button that you just clicked (which will be a table row) and stores it into tableRow
			TableRow tableRow = (TableRow) v.getParent();
			
			//Finds the "champNameTextView" id that is also in the same table row
			TextView champNameTextView = (TextView) tableRow.findViewById(R.id.championNameTextView);
			
			//Then gets the text and saves it to a string
			String champName = champNameTextView.getText().toString().replace(' ', '_');
			
			//Will Most likely have to mess with this
			String champURL = getString(R.string.champion_wiki_page) + champName;
			
			Intent getChampWikiPage = new Intent(Intent.ACTION_VIEW, Uri.parse(champURL));
			
			startActivity(getChampWikiPage);
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
