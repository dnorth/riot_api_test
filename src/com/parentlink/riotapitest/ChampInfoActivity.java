package com.parentlink.riotapitest;

import android.widget.TextView;

public class ChampInfoActivity {

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
	
	String championNameActivity= "";
	String champAttack= "";
	String champDefense= "";
	String champDifficulty= "";
	String freeToPlay= "";
	String champMagic= "";
}
