package com.rainjm.droneapp.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.rainjm.droneapp.R;
import com.rainjm.droneapp.fragments.AltitudeFragment;
import com.rainjm.droneapp.fragments.AttitudeFragment;
import com.rainjm.droneapp.fragments.GpsFragment;
import com.rainjm.droneapp.fragments.PIDFragment;
import com.rainjm.droneapp.fragments.RecieverFragment;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    private Thread workerThread;
    private BluetoothDevice mmDevice;
    private char delimiter = '\n';
    private boolean stopWorker = false;
    private int readBufferPosition = 0;
    private byte[] readBuffer = new byte[1024];
    private AttitudeFragment atitudeFragment;
    private AltitudeFragment altitudeFragment;
    private GpsFragment gpsFragment;
    private RecieverFragment recieverFragment;
    private PIDFragment pidFragment;
    private int step = 0;
    private ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFragments();

        setUpBluetooth();

        setUpActionBar();

        initNavDrawer();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment,  new AttitudeFragment())
                .commit();

    }

    private void initFragments()
    {
        atitudeFragment = new AttitudeFragment();
        altitudeFragment = new AltitudeFragment();
        gpsFragment = new GpsFragment();
        recieverFragment = new RecieverFragment();
        pidFragment = new PIDFragment();
    }


    private void setUpBluetooth()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(Object dev : pairedDevices)
            {
                BluetoothDevice device = (BluetoothDevice) dev;
                if(device.getName().equals("STEVE")) //Note, you will need to change this to match the name of your device
                {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    private void setUpActionBar()
    {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            final ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);

            toggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            mActionBar.setTitle("Attitude");

        }

    }

    private void initNavDrawer()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_drawer);

        navigationView.setCheckedItem(R.id.attitude);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.altitude:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,altitudeFragment)
                                .commit();
                        mActionBar.setTitle("Altitude");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.attitude:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  atitudeFragment)
                                .commit();
                        mActionBar.setTitle("Attitude");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.gps:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,gpsFragment)
                                .commit();
                        mActionBar.setTitle("GPS");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.reciever:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, recieverFragment)
                                .commit();
                        mActionBar.setTitle("Receiver");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.pid:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, pidFragment)
                                .commit();
                        mActionBar.setTitle("PID");
                        drawerLayout.closeDrawers();
                        return true;

                }
                return true;
            }
        });
    }



    public void connect()
    {
        if(mmDevice==null)
        {
            Toast.makeText(this,"Device not Paired",Toast.LENGTH_SHORT).show();
        }else{
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            try{
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
                processHandler();
            }catch (IOException io){
                Toast.makeText(this,"Error connecting",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void processHandler()
    {
        final Handler handler = new Handler();
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try{
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);

                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            Map<String,String> dataMap = parseData(data,step);
                                            atitudeFragment.update_data(dataMap);
                                            altitudeFragment.update_data(dataMap);
                                            gpsFragment.update_data(dataMap);
                                            recieverFragment.update_data(dataMap);


                                           // myLabel.setText(data);
                                        }
                                    });
                                    readBufferPosition = 0;
                                    step+=1;

                                    //The variable data now contains our full command
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }


                    }catch (IOException io){

                    }catch (ArrayIndexOutOfBoundsException a){
                        Log.d("Error",a.getMessage());
                        //Toast.makeText(mContext,a.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        workerThread.start();
    }

    private void disconnect()
    {
        if(mmSocket != null)
        {
            try{
                mmSocket.close();
            }catch (IOException i){
                Log.d("Disconnect error",i.getMessage());
            }

        }
    }

    private Map<String,String> parseData(String data,int step)
    {
        Map<String,String> dataMap = new HashMap<>();
        String dataArr[] = data.split(" ");
        dataMap.put("attitude_pitch",dataArr[0]);
        dataMap.put("attitude_roll",dataArr[1]);
        dataMap.put("attitude_yaw",dataArr[2]);
        dataMap.put("throttle",dataArr[3]);
        dataMap.put("roll",dataArr[4]);
        dataMap.put("pitch",dataArr[5]);
        dataMap.put("yaw",dataArr[6]);
        dataMap.put("altitude",dataArr[7]);
        dataMap.put("latitude",dataArr[8]);
        dataMap.put("longitude",dataArr[9]);
        dataMap.put("step",String.valueOf(step));


        return dataMap;

    }

    public void sendPIDData(String data)
    {
        try{
            if(mmOutputStream != null)
            {
                mmOutputStream.write(data.getBytes());
            }
        }catch (IOException io){
            Log.d("Output Error",io.getMessage());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(mmSocket != null)
                mmSocket.close();
        }catch (IOException io){

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_connect:
                //Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                connect();
                return true;
            case R.id.action_disconnect:
                disconnect();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }




}
