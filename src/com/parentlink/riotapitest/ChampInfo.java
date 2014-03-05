package com.parentlink.riotapitest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChampInfo {

	private String  champion_name_info= "";
	private String champion_attack_rank = "";
	private String champion_defense_rank = "";
	private String champion_magic_rank = "";
	private String champion_difficulty_rank = "";
	private String champion_free_to_play = "";
	
	
	
	public ChampInfo(String champion_name_info, String champion_attack_rank,
			String champion_defense_rank, String champion_magic_rank,
			String champion_difficulty_rank, String champion_free_to_play) {
		super();
		this.champion_name_info = champion_name_info;
		this.champion_attack_rank = champion_attack_rank;
		this.champion_defense_rank = champion_defense_rank;
		this.champion_magic_rank = champion_magic_rank;
		this.champion_difficulty_rank = champion_difficulty_rank;
		this.champion_free_to_play = champion_free_to_play;
	}
	
	public String getChampion_name_info() {
		return champion_name_info;
	}
	public void setChampion_name_info(String champion_name_info) {
		this.champion_name_info = champion_name_info;
	}
	public String getChampion_attack_rank() {
		return champion_attack_rank;
	}
	public void setChampion_attack_rank(String champion_attack_rank) {
		this.champion_attack_rank = champion_attack_rank;
	}
	public String getChampion_defense_rank() {
		return champion_defense_rank;
	}
	public void setChampion_defense_rank(String champion_defense_rank) {
		this.champion_defense_rank = champion_defense_rank;
	}
	public String getChampion_magic_rank() {
		return champion_magic_rank;
	}
	public void setChampion_magic_rank(String champion_magic_rank) {
		this.champion_magic_rank = champion_magic_rank;
	}
	public String getChampion_difficulty_rank() {
		return champion_difficulty_rank;
	}
	public void setChampion_difficulty_rank(String champion_difficulty_rank) {
		this.champion_difficulty_rank = champion_difficulty_rank;
	}
	public String getChampion_free_to_play() {
		return champion_free_to_play;
	}
	public void setChampion_free_to_play(String champion_free_to_play) {
		this.champion_free_to_play = champion_free_to_play;
	}
	
}
