package com.example.myfirstapp;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.example.jsontest.HttpClient;
import com.google.android.gms.drive.internal.GetMetadataRequest;
import com.google.android.gms.drive.internal.h;
import com.google.android.gms.internal.gt;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R.anim;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.preference.PreferenceManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
//import android.support.v4.widget.SearchViewCompatIcs.MySearchView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class FragmentFour extends Fragment {

	
	private  final static String URL = "http://homepage.lnu.se/staff/tblma/tracker/getpos.php";
	private  final static String SENDURL = "http://homepage.lnu.se/staff/tblma/tracker/setpos.php";
	
	public static final Bundle savedInstanceState = null;
	private LocationManager locationManager;
	
	LocationClient mLocationClient;
	private MapView mapView;
	private GoogleMap googleMap;//TextView textView;
	LocationListener locationListener;
	Toast toast;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	
	   View view = inflater.inflate(R.layout.fragment_four, container, false);
	   onCreate(savedInstanceState);
	   mapView = (MapView) view.findViewById(R.id.mapView);
	   mapView.onCreate(savedInstanceState);
	  
	   googleMap = mapView.getMap();
	   MapsInitializer.initialize(getActivity());
	   locationListener = new GPSClient();	   
	   locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);		   
	
	   new Thread((Runnable) new GPSClient()).start();
	  
	   return view;
		
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		//timerHandler.postDelayed(timerRunnable, 10000);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, locationListener);
	}

	@Override
	public void onResume(){
		super.onResume();
		mapView.onResume();
	}
	@Override
	public void onPause(){
		super .onPause();
		mapView.onPause();
	}
	@Override
	public void onDestroy(){
		super .onDestroy();
		mapView.onDestroy();
	}
	@Override
	public void onLowMemory(){
		super .onLowMemory();
		mapView.onLowMemory();
	}
	
private  class GPSClient extends Service implements Runnable,   LocationListener  , GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{
        
		private Location myLocation ;	
		
		double latitude;
		double longitude;
		double altitude;
		String positionName;
		//GoogleMap googleMap;		
		boolean returnLocationUpdate;	
		
		ArrayList<LatLng> pos = new ArrayList<LatLng>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Marker> markerList = new ArrayList<Marker>();

		private LatLng latlng;
		//private Marker marker;		
		private MarkerOptions users;
				
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			RunAll();
			
		}
		
		public void RunAll()
		{
			
			int delay = 0;
			int period = 30000;
			
			Timer timer = new Timer();
			Looper.prepare();

			timer.scheduleAtFixedRate(new TimerTask()
			{	
					public void run() {
						
						//onCreate(savedInstanceState);	
						double lat = 0;
						double lon = 0;
						String name = "";
						JSONObject data = null;

						// = new DefaultHttpClient();			
						if(returnLocationService() == true)
						{
							sendMyLocationUpdate();			
						}
						
						JSONArray retrivedJsonArrValues = retrivePositionsFromJSON();

						try
						{
							pos.clear();
							
							markerList.clear();
							
							for(int i = 0; i < retrivedJsonArrValues.length() ; i++)
							{
								
								data = retrivedJsonArrValues.getJSONObject(i);		
								lat = data.getDouble("lat");	
								lon = data.getDouble("lon");					
								name = data.getString("name");					
								//googleMap.addMarker(users);
								latlng = new LatLng(lat, lon);
								//pos.add(new LatLng(lat, lon));
								pos.add(latlng);
								names.add(name);		
	
							}
		
							
						}catch (JSONException e) {
							
							e.printStackTrace();
						}
						Log.d("VALUE OF LIST", pos.toString());
						Map();
						
						Log.d("VALUE OF LIST", pos.toString());
					  }
			},delay, period);
		Looper.loop();
			
			
		}
		
		public double getLatitude()
		{
			if(myLocation != null)
			{
				latitude = myLocation.getLatitude();
			}
			return latitude;
		}
		
		public double getAltitude()
		{
			if(myLocation != null)
			{
				altitude = myLocation.getAltitude();
			}
			
			return altitude;
		}
		
		public double getLongetud()
		{
			if(myLocation != null)
			{
				longitude = myLocation.getLongitude();
			}
			return longitude;
		}
		
		public String getName()
		{
			if(myLocation != null)
			{
				positionName = returnUserNameFromPreferences();
			}
			return positionName;
		}
		String nameFromPreferences;
		
		public String returnUserNameFromPreferences()
		{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			nameFromPreferences = prefs.getString("edit_text_preference", "");
			return nameFromPreferences;
		}
	
		public boolean returnLocationService()
		{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			returnLocationUpdate = prefs.getBoolean("gps_enable_switch", true);			
			return returnLocationUpdate;
		}

		//Skickar min egna position, om användaren tillåter det.
		public void sendMyLocationUpdate()
		{
			
			JSONObject jsonSend = new JSONObject();					
					//timerHandler.postDelayed(timerRunnable,10000);
			myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
			try{
				jsonSend.put("lat", getLatitude());
				jsonSend.put("lon", getLongetud());
				jsonSend.put("acc", getAltitude());
				jsonSend.put("name",  getName());
			}
			catch (JSONException e) {
                e.printStackTrace();
			}			
			Log.d("Values sent" , " " + getLatitude() +" " + getLongetud() + " " + getAltitude() + " " + getName());
			
			SendHttpPost(SENDURL, jsonSend);		
		}
		
		//Hämtar ut alla positioner som finns.
		DefaultHttpClient client= new DefaultHttpClient();
		public JSONArray retrivePositionsFromJSON()
		{
			
			JSONArray newJsonArray = null;
			try
			{
				HttpGet getRequest = new HttpGet(URL);
				HttpResponse responce  = client.execute(getRequest);
				
				InputStream jsonStream	= responce.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
				
				StringBuilder builder = new StringBuilder();
				String line;
				
				while((line = reader.readLine()) != null)
				{
					//Lägger till string till builder
					builder.append(line);
				}
				
				String jsonData = builder.toString();
				Log.d("JASON", jsonData);
				
				try {
					newJsonArray = new JSONArray(jsonData);
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			} 
			
			catch (ClientProtocolException e) {
			
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} //catch (JSONException e) {
				
				//e.printStackTrace();
			//
			return newJsonArray;
		}
		
		

		/*private void removeMarkers()
		{
			if(markerList != null)
			{
				for(Marker mark: markerList)
				{
					mark.remove();
				}
			}
		}*/
	
		public boolean Map()
		{
			
			mapView.post(new Runnable()
			{		
				//@Override
				public void run()
				{
				
					if(googleMap == null)
					{
						return;
					}

					//userLocations = new LatLng(latt, lonn);
					
					//Har haft lite problem med markers på kartan. De kan ritas ut, men jag får inte bort de gamla när nya positioner läggs till.
					//google.clear fungerar inte(eller den tar bort en marker) Så jag löste det genom att sätta alla positioner i en ArrayList<LatLng> och sedan rensar denna lista vid varje
					//uppdatering av positioner.
					googleMap.clear();
					for (LatLng l : pos) {
						for(String n : names)
						{
							
							users = new MarkerOptions().position(l).title(l.toString()).snippet("Location");
							markerList.add(googleMap.addMarker(users));
							
							CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(l, 10);
							googleMap.animateCamera(camera);		
						}
					}

				}

			});
			return true;
		}
		
		//Skicar min position.
		public JSONObject SendHttpPost(String URL, JSONObject jsonObject )
		{
			HttpClient client = new DefaultHttpClient();
			Log.d("JAG GÅR", "IN HÄR ÄDNå");
			try{
				
				// DefaultHttpClient httpclient = new DefaultHttpClient();
				 HttpPost httpPostRequest = new HttpPost(URL);
				 StringEntity se = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
	 
				 httpPostRequest.setEntity(se);
                 httpPostRequest.setHeader("Accept", "application/json");
                 httpPostRequest.setHeader("Content-type", "application/json; charset=utf-8");
                 Log.d(" Recived values", " "+ jsonObject);
                 client.execute(httpPostRequest);   
			}
			 catch (Exception e)
             {			
                 e.printStackTrace();
             }
			return null;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
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

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onConnected(Bundle arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}

		

	
	}
}
