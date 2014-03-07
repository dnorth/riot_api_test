package com.parentlink.riotapitest;

import java.util.Arrays;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {
	
	public final static String CHAMPION_NAME = "com.parentlink.riotapitest.RIOT";
	
	private SharedPreferences champNameEntered;
	
	private TableLayout champListScrollView;
	
	private EditText champNameEditText;
	
	Button enterChampNameButton;
	Button deleteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		champNameEntered = getSharedPreferences("champList",MODE_PRIVATE);
		
		champListScrollView= (TableLayout) findViewById(R.id.champScrollView);
		
		champNameEditText= (EditText) findViewById(R.id.champNameEditText);
		
		enterChampNameButton= (Button) findViewById(R.id.enterChampNameButton);
		deleteButton= (Button) findViewById(R.id.deleteButton);
		
		enterChampNameButton.setOnClickListener(enterChampNameButtonListener);
		deleteButton.setOnClickListener(deleteChampListButtonListener);
		
		updateSavedChampList(null);
	}
	
	private void updateSavedChampList(String newChampName){
		
		String[] champs = champNameEntered.getAll().keySet().toArray(new String[0]);
		
		Arrays.sort(champs, String.CASE_INSENSITIVE_ORDER);
		
		if (newChampName != null){
			
			insertChampInScrollView(newChampName, Arrays.binarySearch(champs, newChampName));
		}
		else{
			
			for (int i= 0; i < champs.length; i++){
				
				insertChampInScrollView(champs[i],i);
			}
		}
	}
	
	private void insertChampInScrollView(String champ, int arrayIndex){
		//Inflater allows champ names to be added to the scroll view dynamically
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View newChampRow = inflater.inflate(R.layout.champ_info_row, null);
		
		View tmp = newChampRow.findViewById(R.id.championNameTextView);
		TextView newChampTextView = (TextView) tmp;
		
		newChampTextView.setText(champ);
		
		Button champInfoButton = (Button) newChampRow.findViewById(R.id.champInfoButton);
		
		champInfoButton.setOnClickListener(getChampActivityListener);
		
		Button champWebButton = (Button) newChampRow.findViewById(R.id.champWebButton);
		
		champWebButton.setOnClickListener(getChampInfoFromWebsiteListener);
		
		champListScrollView.addView(newChampRow, arrayIndex);
		
	}

	private void saveChampName(String newChamp){
		
		String isTheChampNew = champNameEntered.getString(newChamp, null);
		
		SharedPreferences.Editor preferencesEditor = champNameEntered.edit();
		
		preferencesEditor.putString(newChamp, newChamp);
		preferencesEditor.apply();
		
		if(isTheChampNew == null){
			
			updateSavedChampList(newChamp);
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
		
		champListScrollView.removeAllViews();
	}
	
	public OnClickListener getChampActivityListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			TableRow tableRow = (TableRow) v.getParent();
			
			//ChampName or Champion Name?
			TextView champNameTextView = (TextView) tableRow.findViewById(R.id.championNameTextView);
			
			String champName = champNameTextView.getText().toString();
			
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
			TextView champNameTextView = (TextView) tableRow.findViewById(R.id.champNameTextView);
			
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
