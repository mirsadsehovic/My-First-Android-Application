package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.CacheRequest;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import com.google.android.gms.internal.fp;
import com.google.android.gms.internal.ge;


import android.R.string;
import android.net.ConnectivityManager;
import android.net.http.HttpResponseCache;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable.Factory;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.DropBoxManager.Entry;
import android.preference.PreferenceManager;



public class FragmentOne extends Fragment implements OnItemClickListener 
{

	ArrayList<String> list = new ArrayList<String>();
	//t = (TextView) findViewById(R.id.textView2);
	
	ListView messageList;
	String messages;
	EditText multiLine;
	ByteBuffer bb;
	ArrayAdapter<String> adapter;
	
	TextView textView;
	String itemValue;
	File newFile;

	private static String INET_ADRESS = "ny057.lnu.se";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		View view = inflater.inflate(R.layout.fragment_one,container,false);
		super.onCreate(savedInstanceState);
		
		messageList = (ListView) view.findViewById(R.id.message_list);	
		new Thread(new Client()).start();
			
		return view;
	}
	
	public String ItemClick()
	{
		messageList.setOnItemClickListener(new OnItemClickListener()
		{

			//@Override
			public void onItemClick(
					AdapterView<?> parent, View view,
					int position, long id) {
		
				// TODO Auto-generated method stub
			
				int itemPositon = position;
				view = getView().findViewById(R.id.message_list);
				textView = (TextView)view;
				itemValue = (String)messageList.getItemAtPosition(itemPositon);
				textView.setText(itemValue.toString());
			
			}
			
		});
		return itemValue;
	}
	
	public String getItemValue()
	{
		return this.itemValue;
	}
	
	
private class Client implements Runnable 
{
		
	private static final String FILENAME = "myFile.txt";
	
	private ArrayList<String> valuesOfMessages = new ArrayList<String>();
	private ArrayList<String> valuesFromCach = new ArrayList<String>();
	private ArrayList<Integer> maxIdCheck = new ArrayList<Integer>();
	

	int newMessage = 0;

	//Skapar en preference i SharedPreferences för att hålla reda på det nuvarande högsta ID i servern. 
	public void returnMaxId(int x)
	{
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(getString(R.string.max_id_holder), x);
		editor.commit();	
	}
	
	//Detta är en metod som helt enkelt läser värdet i preferences för högsta ID.
	public int readFromPreference()
	{
		SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		int defaultValue = sharedPreferences.getInt(getString(R.string.max_id_holder), 0);
		return defaultValue;
	}
	
	//En metod som hämtar MAXID från servern. 
	private int getMaxID()
	{
		int maxID = 0;	
		try
		{
			InetAddress inetAdr = InetAddress.getByName(INET_ADRESS);	
		
			if(inetAdr != null )
			{
				//	readFromFile();
				
					DatagramSocket socket = new DatagramSocket();
					byte[] buf = ("MAXID;").getBytes();
					DatagramPacket spacket = new DatagramPacket(buf, buf.length, inetAdr, 9050);
					socket.send(spacket);
					
					//final ArrayList<String> list = new ArrayList<String>();						
					byte[] t = new byte[1500];
					spacket = new DatagramPacket(t, t.length);
					socket.receive(spacket);			
					String convertToString = new String(spacket.getData());		
						
					//konverterar om så jag bara får siffran.
					 maxID = Integer.parseInt(convertToString.replaceAll("[\\D]",""));
					 socket.close();

			}

		}
		catch(Exception e)
		{
			Log.d("NO INET ADRESS", e.getMessage());
			e.getMessage();
		}
		
		//Returnerar MAXID
		return maxID;
	}
	
	//Hämtar De senaste 10 meddelanden från servern genom att kolla vad maxID är och sedan räknar ned. 
	public void getMessagesFromServer()
	{
	
		int maxID = getMaxID();
		maxIdCheck.add(maxID);
		Log.d("VALUE FROM PREFERENCE", " " + readFromPreference());
		
		//Jag kollar om maxIDet som hämtas skiljer sig från det som finns i preferences. Är det det så innebär det att nya meddelanden finns att hämta.
		if(maxID != readFromPreference())
		{
			
			returnMaxId(maxID);
			Log.d("VÄRDET ÄR", "INTE SAMMA");
			
			//Hämtar de 10 senaste meddelanden.
			try
			{	
				InetAddress inetAdr = InetAddress.getByName(INET_ADRESS);	
				
				if(inetAdr != null)
				{				
					DatagramSocket socket = new DatagramSocket();
					for(int i = 0 ; i < 10; i++)
					{
						
						byte[] remainingId = ("2GETID" + maxID + ";").getBytes();
						DatagramPacket packet = new DatagramPacket(remainingId, remainingId.length, inetAdr, 9050);
						
						//skickar meddelandet till socketen. 
						socket.send(packet);									
						byte[] message = new byte[1500];
						packet = new DatagramPacket(message, message.length);					
						socket.receive(packet);
						messages = new String(packet.getData()).replace("2MTEXT;" , "");													
						
						//Sparar meddelanden i en ArrayList. Detta för att presentera meddelanden senare. 
						valuesOfMessages.add(messages.trim() + "\n");
						Log.d("GET MESSSAGES",  " " +i);
						
						maxID = maxID -1;						
					
						if(i == 10)
						{
							socket.close();
						}
					}
					
				}
				
			}
			
			catch(Exception e)
			{
				Log.d("NO INET ADRESS", e.getMessage());
				e.getMessage();
			}
		
			newMessage = 1;		
			//Efter hämtning av meddelande sparar jag dem till filen.
			writeToFile();
		
		}
		
		//om maxID är likadant så läser jag bara från filen och hoppar över hämtningen av meddelanden.
		else
		{		
			newMessage = 2;
			readFromFile();
		}

	}
	private File file = new File(getActivity().getFilesDir(),FILENAME);
	
	//skriver ner meddelanden till en fil, som sedan kan hämtas om inga nya meddelanden finnns eller ingen connection
	private void writeToFile() 
	{
		if(file.exists())
		{
			file.delete();
		}
		
        try 
        {   		        	
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE));		      
            for (String s : valuesOfMessages) 
            {	
            	outputStreamWriter.write(s);
            	Log.d("MESSAGES WRITTEN To FILE", s);
			} 
            outputStreamWriter.close();
        }
        
        catch (IOException e) 
        {
            Log.e("", "File write failed: " + e.toString());
        } 
      
    }
	
	//Läser från filen om det inte finns någon internet anslutning eller om inga nya mail har tillkommit
	private void readFromFile() 
	{
	
	        try {
		            InputStream inputStream = getActivity().openFileInput(FILENAME);
		            valuesFromCach.clear();
		            
		            if ( inputStream != null ) {
		                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		                String receiveString = "";
		               // StringBuilder stringBuilder = new StringBuilder();
		                 		                
		                while ((receiveString = bufferedReader.readLine()) != null ) 
		                {
		                	Log.d("RECIVING STRING FROM FILE", receiveString);
				            valuesFromCach.add(receiveString.trim());	
	
		                }
	                 
	                inputStream.close();
	                
	               // valuesFromCach.add(ret);
	            }
	        }
	        catch (FileNotFoundException e) 
	        {
	           Log.e("", "File not found: " + e.toString());
	        } 
	        catch (IOException e)
	        {
	            Log.e("", "Can not read file: " + e.toString());
	        }
	   }
	

	//Här har jag en switch som avgöra vilka värden som ska presenteras. Ena presenterar de nyaste meddelande(om det finns sådana). Den första. den andra läser de meddelanden som är gamla
	//om inga nya finns.
	
	public void run()
	{
	
		getMessagesFromServer();
		//readFromFile();
		messageList.post(new Runnable() {			
			
			public void run() 
			{					
				
				switch (newMessage) {
				case 1:
					Log.d("ÄR I SWITCH", "ETT");
					messageList.setAdapter(new ArrayAdapter<String>(getActivity(),  R.layout.list_row, valuesOfMessages));	
					break;
				case 2:
					Log.d("ÄR I SWITCH", "TVÅ");
					messageList.setAdapter(new ArrayAdapter<String>(getActivity(),  R.layout.list_row, valuesFromCach));
				default:
					break;
				}
			
			}
		});	
			
	}
		
}	

	@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		long arg3) 
		{
		// TODO Auto-generated method stub	
}
		

}
