package com.nennig.name.that.president;
import java.util.ArrayList;
import java.util.Arrays;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppManager;
import com.nennig.constants.DevConstants;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    //Two methods to get the data into string form to save the data
    private static String _delim = ",";
    public static String createPrefSaveString(String[] strArr){
    	ArrayList<String> list = new ArrayList<String>(Arrays.asList(strArr));
    	return createPrefSaveString(list);
    }
    
    public static String createPrefSaveString(ArrayList<String> strArr){
    	String prefSaveStr = "";
    	for(String str : strArr)
    		prefSaveStr = prefSaveStr + str + _delim;
    	return prefSaveStr;
    }
    
    //Two methods to unpack a saved string for the game state
    public static String[] unpackPrefSaveStringToArray(String str){
    	ArrayList<String> list = new ArrayList<String>(unpackPrefSaveString(str));
    	return list.toArray(new String[list.size()]);
    }
    
    public static ArrayList<String> unpackPrefSaveString(String str){
    	ArrayList<String> strArr = new ArrayList<String>();
		String[] split = str.split(_delim);
		for(int i = 0; i < split.length; i++)
		{
			strArr.add(split[i]);
		}
		return strArr;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
    	switch(item.getItemId()){
    	case R.id.menu_start_over:
        	intent = new Intent(this,ViewerActivity.class);  
        	startActivity(intent);
    		finish();
    		return true;
    	case R.id.menu_main_menu:
        	intent = new Intent(this,MainActivity.class);     
        	startActivity(intent);
    		finish();
    		return true;
    	case R.id.menu_about:
    		AppManager.aboutAlert(this);
    		return true;
    	case R.id.menu_rate_this:
    		String str = DevConstants.GOOGLE_PLAY + AppConfig.APP_PNAME;
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
