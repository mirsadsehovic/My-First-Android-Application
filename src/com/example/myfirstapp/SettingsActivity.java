package com.example.myfirstapp;

import java.util.List;

import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onBuildHeaders(List<Header>target)
	{
		super.onBuildHeaders(target);
		loadHeadersFromResource(R.xml.headers, target);
	}
}
