package com.flask.colorpicker.sample;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

import java.io.IOException;
import java.util.UUID;

public class SampleActivity2 extends AppCompatActivity {
	Button play,pause,stop;
	MediaPlayer mdx;

	String address = null;
	private ProgressDialog progress;
	BluetoothAdapter myBluetooth = null;
	BluetoothSocket btSocket = null;
	private boolean isBtConnected = false;
	static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent newint = getIntent();
		address = newint.getStringExtra(SampleActivity.EXTRA_ADDRESS);

		setContentView(R.layout.activity_sample2);
		new ConnectBT().execute();
		ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
		play=(Button)findViewById(R.id.button5);
		pause=(Button)findViewById(R.id.button2);
		stop=(Button)findViewById(R.id.button3);
		mdx=MediaPlayer.create(SampleActivity2.this,R.raw.virus);

		play.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mdx.start();
				turnOnLed();
			}
		});

		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mdx.pause();
				pauseLed();
			}
		});

		stop.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mdx.stop();
				mdx=MediaPlayer.create(SampleActivity2.this,R.raw.virus);
				turnOffLed();
				Disconnect();
			}
		});



		colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
			@Override
			public void onColorSelected(int selectedColor) {
				Toast.makeText(
						SampleActivity2.this,
						"selectedColor: " + Integer.toHexString(selectedColor).toUpperCase(),
						Toast.LENGTH_SHORT).show();
                      String color= Integer.toHexString(selectedColor).toUpperCase();



				try
				{
					btSocket.getOutputStream().write(color.toString().getBytes());

				}
				catch (IOException e)
				{

				}
			}
		});
	}

	private void msg(String s)
	{
		Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_led_control, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				int id = item.getItemId();


		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class ConnectBT extends AsyncTask<Void, Void, Void>
	{
		private boolean ConnectSuccess = true;

		@Override
		protected void onPreExecute()
		{
			progress = ProgressDialog.show(SampleActivity2.this, "Connecting...", "Please wait!!!");
		}

		@Override
		protected Void doInBackground(Void... devices)
		{
			try
			{
				if (btSocket == null || !isBtConnected)
				{
					myBluetooth = BluetoothAdapter.getDefaultAdapter();
					BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
					btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					btSocket.connect();
				}
			}
			catch (IOException e)
			{
				ConnectSuccess = false;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);

			if (!ConnectSuccess)
			{
				msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
				finish();
			}
			else
			{
				msg("Connected.");
				isBtConnected = true;
			}
			progress.dismiss();
		}
	}

	private void Disconnect()
	{
		if (btSocket!=null)
		{
			try
			{
				btSocket.close();
			}
			catch (IOException e)
			{ msg("Error");}
		}
		finish();

	}
	private void turnOffLed()
	{
		if (btSocket!=null)
		{
			try
			{
				btSocket.getOutputStream().write("hhhhhhhs".toString().getBytes());
			}
			catch (IOException e)
			{
				msg("Error");
			}
		}
	}
	private void pauseLed()
	{
		if (btSocket!=null)
		{
			try
			{
				btSocket.getOutputStream().write("hhhhhhhp".toString().getBytes());
			}
			catch (IOException e)
			{
				msg("Error");
			}
		}
	}
	private void turnOnLed()
	{
		if (btSocket!=null)
		{


					try

					{


							btSocket.getOutputStream().write("gggggggg".toString().getBytes());

					} catch (IOException e) {
						msg("Error");
					}

	}
	}




}