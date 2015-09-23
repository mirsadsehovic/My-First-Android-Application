package com.example.myfirstapp;




import com.google.android.gms.drive.internal.m;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawList;
	private String[] mChoiceTitles;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	


	  Integer[] imageId = {
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark,
		      R.drawable.common_signin_btn_icon_dark
		  };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CustomList adapter = new
		        CustomList(MainActivity.this, web, imageId);
		mTitle = mDrawerTitle = getTitle();
		//genererar listan
		mChoiceTitles = getResources().getStringArray(R.array.choices_array);
	
		mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
		
		mDrawList = (ListView)findViewById(R.id.drawer_list);
			
		mDrawList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mChoiceTitles));
		
		mDrawList.setOnItemClickListener(new DrawerItemClickListener());
		
		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
		
		//mDrawList.setOnClickListener(new DrawerItemClickListener());
		        
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		//Är själva knappen uppe till vänster hörn, som kan dra fram listan// dock verkar den inte fungerar riktigt.
				mDrawerToggle = new ActionBarDrawerToggle(
						this, 
						mDrawerLayout, 
						R.drawable.ic_launcher,
						R.string.drawer_open,
						R.string.drawer_close
						){
					public void onDrawerClosed(View view){
						getActionBar().setTitle(mTitle);
						invalidateOptionsMenu();
					}
					public void onDrawerOpened(View drawerView){
						getActionBar().setTitle(mDrawerTitle);
						invalidateOptionsMenu();
					}
				};
				
			//
				mDrawerLayout.setDrawerListener(mDrawerToggle);
			
				if(savedInstanceState==null)
				{
					selectItem(0);
				}
				
		//mDrawList.setAdapter(adapter);
				//ButtonTest();
	}
	
	

	public boolean onPrepareOptionsMenu(Menu menu)
	{
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			selectItem(position);
		}
	}
	
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	//@SuppressLint("NewApi")
	private void selectItem(int position)
	{
		//En metod som skiftar mellan de olika fragmentsen. Använder en switch sats för detta då det verkar
		//Som ett smart alternativ.
		
		Fragment newFragment = new FragmentOne();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		switch (position) {
		case 0:
			
			newFragment = new FragmentOne();
			
			break;
		case 1:
			newFragment = new FragmentTwo();
			break;
		case 2:
			newFragment = new FragmentThree();
			break;
		case 3:
			newFragment = new FragmentFour();
			break;
		case 4:
			newFragment = new FragmentFive();
			break;
		default:
			break;
		}
		
		transaction
		.replace(R.id.content_frame, newFragment);
		
		transaction.addToBackStack(null);
		transaction.commit();
		mDrawList.setItemChecked(position, true);
		setTitle(mChoiceTitles[position]);
		mDrawerLayout.closeDrawer(mDrawList);
		
	}
	
	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getActionBar().setTitle(title);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	

	
}

class MyAdatper extends BaseAdapter {

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
