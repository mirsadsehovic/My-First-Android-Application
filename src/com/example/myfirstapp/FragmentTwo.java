package com.example.myfirstapp;

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FragmentTwo extends Fragment implements OnClickListener {
	
	String nameFromPreferences;
	
	EditText et;
	String Message;
	Button sendButton;
	TextView charCounterTextView;
	ImageButton imageSendutton;
	public int CHAR_COUNTER  = 140;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view =  inflater.inflate(R.layout.fragment_two, container,false);
		onCreate(savedInstanceState);
		charCounterTextView = (TextView)view.findViewById(R.id.charCounter);
		et = (EditText)view.findViewById(R.id.sendTextField);	
		
		countChars();
	
		sendButton = (Button)view.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(this);
		
		return view;
	}
	
	//ETT försök till att skapa en counter som håller reda på hur många tecken som är kvar att använda. Fungerar till hälften men backspace fungerar inte.
	public void countChars()
	{
		
		charCounterTextView.setText(String.valueOf(CHAR_COUNTER));
		int maxLenght = 140;
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLenght)});
		TextWatcher mTextWatcher = new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				CHAR_COUNTER -= 1; 
				// TODO Auto-generated method stub
				}
				
			
			
		
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {	
				charCounterTextView.setText(String.valueOf(CHAR_COUNTER));
			}
			
			
			public void afterTextChanged(Editable s) {
				
			
			    charCounterTextView.setText(String.valueOf(CHAR_COUNTER));		
			}
		};
		//charCounterTextView = (TextView)getView().findViewById(R.id.charCounter);
		et.addTextChangedListener(mTextWatcher);

	}
	
	//Hämtar ut namnet man väljer i preferences.
	public String returnPreferenceName()
	{
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		nameFromPreferences = prefs.getString("edit_text_preference", " ");
		return nameFromPreferences;	
		
	}
	
	public void onClick(View v)
	{
		new Thread(new Client()).start();			
	}
	
	private class Client implements Runnable {
		
		//Skickar det nya meddelande.
		public void run()
		{
			onStart();
			try
			{
				
				InetAddress inetAdr = InetAddress.getByName("ny057.lnu.se");				
				if(inetAdr != null)
				{
				
					if(et.length() != 0)
					{	
						DatagramSocket socket = new DatagramSocket();
						Message = et.getText().toString();
						Time now = new Time();
						int min = now.minute;
						int sec = now.second;
						int day = now.monthDay;
						int month = now.month;
						
						String test = String.valueOf(day);
						//Log.d("DATE", test);
						byte[] buf= ("2MSGAD;" + returnPreferenceName()+ ";"+ et.getText().toString()).getBytes();	 	
						DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAdr, 9050);

						socket.send(packet);
						socket.receive(packet);											
						socket.close();		

					}
					
					else
					{
						et.setHint("Du måste skriva något!");
					}
				}
			}
				
		
			catch(Exception e)
			{
				
				e.getMessage();
			}
		}
	}

}
