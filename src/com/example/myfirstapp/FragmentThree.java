package com.example.myfirstapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

/*
 * 
 * EN CLASS SOM JAG TESTAR LITE OLIKA SAKER I: HÄR HAR JAG VALIGA SMILEY MEN JAG FÅR DET INTE ATT FUNGERA I FRAGMENT ONE DÄR MEDDELANDEN FINNS 
 * 
 * 
 */

public class FragmentThree extends Fragment  {
	
	FragmentOne fragmentOne;
	TextView text;
	String getValue;
	SharedPreferences ps;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		View view = inflater.inflate(R.layout.fragment_three, container, false);
		text = (TextView) view.findViewById(R.id.specificMessageView);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		getValue = prefs.getString("edit_text_preference", " ");
		
		text.setText(getSmiledText(getActivity(), getValue));
	
		return view;
		//return inflater.inflate(R.layout.fragment_three,null);
	}
	
	private static final Factory spannableFactory = Spannable.Factory
	        .getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {
	    addPattern(emoticons, ":)", R.drawable.emoticon);
	    addPattern(emoticons, ":-)", R.drawable.emoticon);
	    // ...
	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
	        int resource) {
	    map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue()),
	                        matcher.start(), matcher.end(),
	                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}

	public static Spannable getSmiledText(Context context, String text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	
	
	/*public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}*/

	/*public void onResume()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		getValue = prefs.getString("edit_text_preference", " ");
		text.setText(getValue);
		super.onResume();
	}/*
		
	
	/*public String getName()
	{
		return getUserName();
	}*/

}
