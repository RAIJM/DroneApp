package com.rainjm.droneapp.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.rainjm.droneapp.R;
import com.rainjm.droneapp.fragments.AltitudeFragment;
import com.rainjm.droneapp.fragments.AttitudeFragment;
import com.rainjm.droneapp.fragments.GpsFragment;
import com.rainjm.droneapp.fragments.PIDFragment;
import com.rainjm.droneapp.fragments.RecieverFragment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.rainjm.droneapp.R.id.textView;

public class MainActivity extends AppCompatActivity {

    UsbManager usbManager;
    Button startButton;
    UsbDevice device;
    UsbDeviceConnection usbConnection;
    UsbSerialDevice serial;
    UsbSerialDevice serialPort;
    public static final String ACTION_USB_PERMISSION = "action_usb_permission";
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            final ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);

            toggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment,  new AttitudeFragment())
                .commit();
        actionBar.setTitle("Attitude");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_drawer);

        navigationView.setCheckedItem(R.id.attitude);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.altitude:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  new AltitudeFragment())
                                .commit();
                        actionBar.setTitle("Altitude");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.attitude:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  new AttitudeFragment())
                                .commit();
                        actionBar.setTitle("Attitude");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.gps:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  new GpsFragment())
                                .commit();
                        actionBar.setTitle("GPS");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.reciever:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  new RecieverFragment())
                                .commit();
                        actionBar.setTitle("Receiver");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.pid:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment,  new PIDFragment())
                                .commit();
                        actionBar.setTitle("PID");
                        drawerLayout.closeDrawers();
                        return true;

                }
                return true;
            }
        });




        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        ///startButton = (Button) findViewById(R.id.buttonStart);
        //serial = UsbSerialDevice.createUsbSerialDevice(device, usbConnection);
        IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(broadcastReceiver,intentFilter);







    }

    public void connect()
    {
        HashMap usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Object entry : usbDevices.entrySet()) {
                device = (UsbDevice) ((Map.Entry)entry).getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                            new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    usbConnection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
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

        }
        return super.onOptionsItemSelected(item);

    }

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
                //tvAppend(textView, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted =
                        intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    usbConnection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, usbConnection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            //setUiEnabled(true); //Enable Buttons in UI
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback); //
                            //tvAppend(textView,"Serial Connection Opened!\n");

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
               // onClickStart(startButton);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
               // onClickStop(stopButton);
            }
        };
    };
}
