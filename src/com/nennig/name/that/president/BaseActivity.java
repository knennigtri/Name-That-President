package com.nennig.name.that.president;
import java.util.ArrayList;
import java.util.Arrays;

import com.nennig.constants.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    public void aboutAlert(Context c){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("About"); 
        alert.setMessage("Copyright @ 2012 Kevin Nennig");
        
        alert.setPositiveButton("View Site", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String url = DevConstants.MY_WEBSITE;
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	BaseActivity.this.startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
              // Canceled. 
            } 
      }); 
      alert.show();
    }
    
    //Two methods to get the data into string form to save the data
    private static String _delim = ",";
    public String createPrefSaveString(String[] strArr){
    	ArrayList<String> list = new ArrayList<String>(Arrays.asList(strArr));
    	return createPrefSaveString(list);
    }
    
    public String createPrefSaveString(ArrayList<String> strArr){
    	String prefSaveStr = "";
    	for(String str : strArr)
    		prefSaveStr = prefSaveStr + str + _delim;
    	
    	return prefSaveStr;
    }
    
    //Two methods to unpack a saved string for the game state
    public String[] unpackPrefSaveStringToArray(String str){
    	ArrayList<String> list = new ArrayList<String>(unpackPrefSaveString(str));
    	return list.toArray(new String[list.size()]);
    }
    
    public ArrayList<String> unpackPrefSaveString(String str){
    	ArrayList<String> strArr = new ArrayList<String>();
		String[] split = str.split(_delim);
		for(int i = 0; i < split.length; i++)
		{
			strArr.add(split[i]);
		}
		return strArr;
    }
}
