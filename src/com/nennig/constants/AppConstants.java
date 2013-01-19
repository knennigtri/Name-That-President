package com.nennig.constants;

import java.util.ArrayList;

public class AppConstants {
	//Other Constants
	public static final String DEFAULT_MAIN_IMAGE = "Abraham Lincoln.jpg";
	
	/*
	 * Preferences Constants
	 */
	public static final String NAME_THAT_PREFS = "name.that.prefs";
	
	//Constants to keep track of number right and wrong
	public static final String NAME_THAT_CORRECT = "name.that.correct";
	public static final String NAME_THAT_WRONG = "name.that.wrong";
	
	//Statistical constants
	public static final String NAME_THAT_MOST_CORRECT = "name.that.most.correct";
	public static final String NAME_THAT_NUM_TRIES = "name.that.num.tries";
	
	
	//Constants to reference saved lists
	public static final String NAME_THAT_SAVED_GAME = "name.that.photos.left";
	public static final String NAME_THAT_WRONG_PHOTOS = "name.that.wrong.photos";
	public static final String NAME_THAT_CUR_INDEX = "name.that.cur.photo.index";

	//decision constants
	public static final String NAME_THAT_CONTINUE = "name.that.start.continue";
	public static final String LOAD_WRONG_ANSWERS = "name.that.load.wrong.answers";

	public static final ArrayList<String> CHANGE_LOG = new ArrayList<String>();
	static{
		CHANGE_LOG.add("Update (1/19/13): This app now supports save games. " +
				"If you decide not to finish your current game, you can finish it later!");
	}
}


