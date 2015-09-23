package com.example.myfirstapp;

import java.security.Provider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.drive.internal.m;
import com.google.android.gms.internal.am;
import com.google.android.gms.internal.gp;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;


import android.annotation.TargetApi;
import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable.Callback;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import android.widget.RadioButton;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;




public class FragmentFive extends PreferenceFragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	

	private static final String gps_enable ="gps_enable";
	private CheckBoxPreference checkBoxPref;
	private static final String MODE_CHANGING_ACTION =
            "com.android.settings.location.MODE_CHANGING";
	
	private static final String CURRENT_MODE_KEY = "CURRENT_MODE";
	private int mCurrentMode;
	private static final String NEW_MODE_KEY = "NEW_MODE";
	private boolean mActive = false;
	private GpsStatus gpsStatus;
	
	public EditTextPreference editTextBoxPreference;
	public String getUserName;
	public static String edit_text_preference = "edit_text_preference";
	
	public static String gps_enable_switch = "gps_enable_switch";
	public LocationManager locationManager;
	public LocationClient mLocationClient;
	public SwitchPreference switchPreference;
	public LocationRequest mLocationRequest;
	boolean mUpdateRequested;
	
	public LocationListener locationListener = null;
	
	public boolean locationON()
	{
		switchPreference = (SwitchPreference)findPreference(gps_enable_switch);
		
		if(switchPreference.isEnabled())
		{
			return mUpdateRequested = true;
		}
		
		return mUpdateRequested = false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		addPreferencesFromResource(R.xml.preferences);
		
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);	
		
		mLocationClient = new LocationClient(getActivity(), this, this);
		
		
	}
	
	//Användarens namn.
	public String getUserName()
	{	
		editTextBoxPreference = (EditTextPreference)findPreference(edit_text_preference);
		getUserName = editTextBoxPreference.getText().toString();
		return getUserName;
	}
	
	
	
	@Override
	public void onStop()
	{
			//mLocationClient.removeLocationUpdates(locationListener);
		//mLocationClient.removeLocationUpdates((com.google.android.gms.location.LocationListener) getActivity());
		
		mLocationClient.disconnect();
		Toast.makeText(getActivity(), "Position services/ OFF.",
                Toast.LENGTH_SHORT).show();
		super.onStop();
		
	}

	@Override
	public void onStart()
	{
		
		mLocationClient.connect();
		Toast.makeText(getActivity(), "Position services/ On.",
                Toast.LENGTH_SHORT).show();
		super.onStart(); 
		
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		if(mUpdateRequested)
		{	
			Toast.makeText(getActivity(), "Connected. Please re-connect.",
	                Toast.LENGTH_SHORT).show();
			mLocationClient.requestLocationUpdates(mLocationRequest, (com.google.android.gms.location.LocationListener) getActivity());
			
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		if(mUpdateRequested == false)
		{
			mLocationClient.removeLocationUpdates((com.google.android.gms.location.LocationListener) getActivity());
			Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
	                Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, (LocationListener) getActivity());
		Log.d("TESTING", "location has been changed");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	 
}
	
	

