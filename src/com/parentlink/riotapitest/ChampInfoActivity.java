package com.parentlink.riotapitest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ChampInfoActivity extends Activity{

	private static final String TAG = "CHAMPINFO";
	
	TextView championNameActivityTextView;
	TextView champAttackTextView;
	TextView champDefenseTextView;
	TextView champDifficultyTextView;
	TextView freeToPlayTextView;
	TextView champMagicTextView;
	
	static final String KEY_ITEM = "champions";
	static final String KEY_NAME = "name";
	static final String KEY_ATTACK = "attack";
	static final String KEY_DEFENSE = "defense";
	static final String KEY_DIFFICULTY = "difficulty";
	static final String KEY_FREE = "free to play";
	static final String KEY_MAGIC = "magic";
	
	static String jsonURL = "https://prod.api.pvp.net/api/lol/na/v1.1/champion?api_key=acf9bb44-85f2-44d4-bde1-26d9b2b4c3db";
	
	String championNameActivity= "";
	String champAttack= "";
	String champDefense= "";
	String champDifficulty= "";
	String freeToPlay= "";
	String champMagic= "";
	String compareName="";
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_champ_info);
		
		Intent intent = getIntent();
		compareName = intent.getStringExtra(MainActivity.CHAMPION_NAME).replace(" ", "").toUpperCase();
		Log.v("my compare name: ", compareName);
		
		championNameActivityTextView = (TextView) findViewById(R.id.championNameTextView);
		champAttackTextView = (TextView) findViewById(R.id.champAttackTextView);
		champDefenseTextView = (TextView) findViewById(R.id.champDefenseTextView);
		champDifficultyTextView = (TextView) findViewById(R.id.ChampDifficultyTextView);
		freeToPlayTextView = (TextView) findViewById(R.id.freeToPlayTextView);
		champMagicTextView = (TextView) findViewById(R.id.champMagicTextView);
		
		new MyAsyncTask().execute();
	}

	private class MyAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			HttpGet httppost = new HttpGet(jsonURL);
			
			httppost.setHeader("Content-type","application/json");
			
			InputStream inputStream = null;
			
			String result = null;
			
			try{
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder theStringBuilder = new StringBuilder();
				
				String line = null;
				
				while((line = reader.readLine()) != null){
					
					theStringBuilder.append(line +"\n");
					
				}
				result = theStringBuilder.toString();
				if (result.equals(null))
				{
					
				}
			}
			
			catch(Exception e){
				
				e.printStackTrace();
			}
			
			finally{
				
				try{
					
					if(inputStream != null)inputStream.close();
				}
				
				catch(Exception e){
					
					e.printStackTrace();
				}
				
				JSONObject jsonArray;
				List<String> allChamps = new ArrayList<String>();
				List<String> allAtt = new ArrayList<String>();
				List<String> allDef = new ArrayList<String>();
				List<String> allMag = new ArrayList<String>();
				List<String> allDiff = new ArrayList<String>();
				List<String> allFree = new ArrayList<String>();
				try{
					
					//Log.v("JSONParser RESULT", result);
					
					jsonArray = new JSONObject(result);
					
					JSONArray champions = jsonArray.getJSONArray("champions");
					
					for(int i=0; i < champions.length(); i++)
					{
						JSONObject champ = champions.getJSONObject(i);
						String champion = champ.getString("name").toUpperCase();
						String att = champ.getString("attackRank").toUpperCase();
						String def = champ.getString("defenseRank").toUpperCase();
						String mag = champ.getString("magicRank").toUpperCase();
						String diff = champ.getString("difficultyRank").toUpperCase();
						String free = champ.getString("freeToPlay").toUpperCase();
						Log.v("Champ Names: ", champion);
						allChamps.add(champion);
						allAtt.add(att);
						allDef.add(def);
						allMag.add(mag);
						allDiff.add(diff);
						allFree.add(free);
					}
					
						if (allChamps.contains(compareName))
						{
							championNameActivity= compareName; 
							champAttack= allAtt.get(allChamps.indexOf(compareName));
							champDefense= allDef.get(allChamps.indexOf(compareName));
							champMagic= allMag.get(allChamps.indexOf(compareName));
							champDifficulty= allDiff.get(allChamps.indexOf(compareName));
							freeToPlay= allFree.get(allChamps.indexOf(compareName));
							
							Log.v("free to play message: ", freeToPlay);
							if (freeToPlay.equals("FALSE"))
							{
								freeToPlay = "No";
							}
							else if (freeToPlay.equals("TRUE"))
							{
								freeToPlay = "Yes";
							}
							else
							{
								freeToPlay = "MERP";
							}

						}
						else
						{
							championNameActivity= "NOPE";
						}
					
				}
				catch(JSONException e){
					
					e.printStackTrace();
				}
				
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			
			championNameActivityTextView.setText("Champion: " + championNameActivity);
			champAttackTextView.setText("Attack Rank: " + champAttack);
			champMagicTextView.setText("Magic Rank: " + champMagic);
			champDefenseTextView.setText("Defense Rank: " + champDefense);
			champDifficultyTextView.setText("Difficulty Rank: " + champDifficulty);
			freeToPlayTextView.setText("Free To Play? " + freeToPlay);

		}
		
	}
}
