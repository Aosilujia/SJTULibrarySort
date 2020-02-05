package com.sjtuserfid.sjtulibrarysort;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/*
        Intent intent = new Intent(this, SensorActivity.class);
        String message = "my last hamon";
        startActivity(intent);
*/

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    //private Sensor mLight;
    private Sensor linearAcceleration;
    private float ax[]=new float[3];
    private float accmax[]=new float[3];
    private float accmin[]=new float[3];
    private TextView textView,textView2;
    private Timer timer;
    private TimerTask task;
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        for (int i=0;i<3;i++){
            accmax[i]=0.0f;
            accmin[i]=0.0f;
        }
        super.onCreate(savedInstanceState);
        System.out.println("this is a");
        setContentView(R.layout.acivity_sensor);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        textView = findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                textView.setText("瞬时加速度:\n"+"ax="+ax[0]+"\nay="+ax[1]+"\naz="+ax[2]);
                textView2.setText("最大加速度:\n"+"x正方向="+accmax[0]+"，x反方向="+accmin[0]+"\ny正方向="+accmax[1]+"，y反方向="+accmin[1]+"\nz正方向="+accmax[2]+"，z反方向="+accmin[2]);
            }
        };
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        ax[0]=event.values[0];
        ax[1]=event.values[1];
        ax[2]=event.values[2];
        for (int i=0;i<3;i++){
            accmax[i]=accmax[i]<ax[i]?ax[i]:accmax[i];
            accmin[i]=accmin[i]>ax[i]?ax[i]:accmin[i];
        }

        //System.out.println(ax);
        // Do something with this sensor value.
        //textView.setText(ax[0]+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        timer.cancel();
    }

    public float getData(){
        return ax[0];
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        timer.schedule(task, 1000, 1000);
    }*/
}

