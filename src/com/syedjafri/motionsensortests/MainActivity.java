package com.syedjafri.motionsensortests;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * An app that displays the Accelerometer's values and locks the screen when a certain speed is passed
 * @author Syed Jafri
 *
 */
public class MainActivity extends Activity {
	//TODO unregister receivers maybe
	
	/***onActivityResult key-pairs***/
	private static final int REQUEST_ENABLE_ADMIN = 1;
	
	/***UI components***/
	private TextView xSpeedValue;
	private TextView ySpeedValue;
	private TextView zSpeedValue;
	//Allows the user to decide if the screen should lock if a certain speed is exceeded
	private Switch lockSwitch;
	
	/***Sensor related***/
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mLinearAccelerometer;
	private float maxX;
	private float maxY;
	private float maxZ;
	
	/***Admin related***/
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName adminComponent;
	
	/***Action Bar related***/
	ColorDrawable actionBarColorGreen;
	ColorDrawable actionBarColorRed;
	ActionBar actionBar;
	
	/***Listener(s)***/
	SensorEventListener listener = new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.equals(mLinearAccelerometer)){
				//TODO have this update at a slower rate I could use event.timestamp
				float[] currValues;
				currValues = event.values;
				
				//Save Max Values to display
				if(currValues[0]>maxX){
					maxX= currValues[0];
				}
				if(currValues[1]>maxY){
					maxY= currValues[1];
				}
				if(currValues[2]>maxZ){
					maxZ= currValues[2];
				}
				
				//Format the numbers to display shorter Values
				DecimalFormat formatFloat = new DecimalFormat("#.##");
				xSpeedValue.setText(formatFloat.format(currValues[0]) + "   	Max: " + formatFloat.format(maxX));
				ySpeedValue.setText(formatFloat.format(currValues[1]) + "   	Max: " + formatFloat.format(maxY));
				zSpeedValue.setText(formatFloat.format(currValues[2]) + "   	Max: " + formatFloat.format(maxZ));
				for(int x =0; x<3; x++){
					if(currValues[x]>7.0F){
						//If any of the value exceeds 7 m/s^2
			            lockScreen();
					}
					if(currValues[x]>0.65F){
						changeActionBarColor(true);
					}else{
						changeActionBarColor(false);
					}
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/***Initialize***/
		
		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		
		//Accelerometer will have gravity factored in while LinearAccelerometer will not
		//mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		xSpeedValue = (TextView) findViewById(R.id.xSpeedValueTV);
		ySpeedValue = (TextView) findViewById(R.id.ySpeedValueTV);
		zSpeedValue = (TextView) findViewById(R.id.zSpeedValueTV);
		lockSwitch = (Switch)findViewById(R.id.lockSwitch);
		
		mSensorManager.registerListener(listener, mLinearAccelerometer, SensorManager.SENSOR_DELAY_UI);
		
		mDevicePolicyManager = (DevicePolicyManager) this.getSystemService(DEVICE_POLICY_SERVICE);
		
		//encapsulates the AdminLockReceiver.class
		adminComponent = new ComponentName(this, AdminLockReceiver.class);
		/***************/
		
		actionBarColorGreen = new ColorDrawable(Color.rgb(46,139,87));
		actionBarColorRed = new ColorDrawable(Color.rgb(255,69,0));
		actionBar = getActionBar();
		//Check for admin
		requestAdmin();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("MainActivity", "In onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==MainActivity.REQUEST_ENABLE_ADMIN){
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * Locks screen if it has admin privilege.
	 */
	private void lockScreen(){
		//If the lockSwitch is set to on then lock the screen
		if(lockSwitch.isChecked()){
			mDevicePolicyManager.lockNow();
		}
	}

	/**
	 * Request Admin privilege if it doesn't have it otherwise it does nothing.
	 */
	private void requestAdmin(){
		if (!mDevicePolicyManager.isAdminActive(adminComponent)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            startActivityForResult(intent, REQUEST_ENABLE_ADMIN);
        } 
	}
	private void setupActionBar(){
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);
	}
	private void changeActionBarColor(boolean changeColor){
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);
		if(changeColor==true){
			actionBar.setBackgroundDrawable(actionBarColorGreen);
		}else{
			actionBar.setBackgroundDrawable(actionBarColorRed);
		}
	}
	
}
