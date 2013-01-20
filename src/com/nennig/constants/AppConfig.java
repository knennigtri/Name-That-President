package com.nennig.constants;

import java.util.ArrayList;

public class AppConfig {

	public final static String APP_TITLE = "Name that President";
    public final static String APP_PNAME = "com.nennig.name.that.president";

	public static final ArrayList<String> CHANGE_LOG = new ArrayList<String>();
	static{
		CHANGE_LOG.add("Update (1/19/13): This app now supports save games. " +
				"If you decide not to finish your current game, you can finish it later!");
	}
}


