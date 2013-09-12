package com.syedjafri.motionsensortests;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * An apps that displays the Accelerometers values
 * @author Syed Jafri
 *
 */
public class MainActivity extends Activity {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mLinearAccelerometer;
	private TextView xSpeedValue;
	private TextView ySpeedValue;
	private TextView zSpeedValue;
	
	private float maxX;
	private float maxY;
	private float maxZ;
	
	//TODO Look at the DeviceAdministration guide
	
	SensorEventListener listener = new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.equals(mLinearAccelerometer)){
				float[] currValues;
				currValues = event.values;
				
				if(currValues[0]>maxX){
					maxX= currValues[0];
				}
				if(currValues[1]>maxY){
					maxY= currValues[1];
				}
				if(currValues[2]>maxZ){
					maxZ= currValues[2];
				}
				
				//Format it to display shorter Values
				DecimalFormat formatFloat = new DecimalFormat("#.##");
				xSpeedValue.setText(formatFloat.format(currValues[0]) + "   	Max: " + formatFloat.format(maxX));
				ySpeedValue.setText(formatFloat.format(currValues[1]) + "   	Max: " + formatFloat.format(maxY));
				zSpeedValue.setText(formatFloat.format(currValues[2]) + "   	Max: " + formatFloat.format(maxZ));
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Initialize
		mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		//Accelerometer will have gravity factored in while LinearAccelerometer will not
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		xSpeedValue = (TextView) findViewById(R.id.xSpeedValueTV);
		ySpeedValue = (TextView) findViewById(R.id.ySpeedValueTV);
		zSpeedValue = (TextView) findViewById(R.id.zSpeedValueTV);
		
		mSensorManager.registerListener(listener, mLinearAccelerometer, SensorManager.SENSOR_DELAY_UI);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
