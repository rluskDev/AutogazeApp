package com.example.autogaze;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.io.IOException;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AutogazeMain";
    private static String Name = "ronnie";
    private BluetoothSocket socket;

    private void connect(String Name) throws IOException {
        int btStatus = 1;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, btStatus);
            if (btStatus == RESULT_CANCELED) {
                //Maybe warn the user too? idk
                Log.e(TAG, " error");
            }
        }
        /* Device discovery would go here if we wanted to deal with that */
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                if(deviceName.contentEquals(Name)) {
                    ConnectThread my_dude = new ConnectThread(device, bluetoothAdapter);
                    socket = my_dude.getSocket();
                    my_dude.run();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        /*FloatingActionButton fab = findViewById(R.id.fab);
        //make the button toggle bluetooth or something? btw the snackbar is the action that makes
        //text pop up. It's not the button itself. You need to replace the snackbar action.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            connect(Name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO: Make 5 preset buttons (ideally on main screen), then have connect and disconnect (gotta refactor)
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String preset;

        //Perform work associated with the connection in a separate thread.
        MyBluetoothService my_guy = new MyBluetoothService();
        MyBluetoothService.ConnectedThread piThread = my_guy.new ConnectedThread(socket);

        if (id == R.id.nav_preset1) {
            preset = "1";
            byte[] buff = preset.getBytes();
            piThread.write(buff);
        }
        else if (id == R.id.nav_preset2) {
            preset = "2";
            byte[] buff = preset.getBytes();
            piThread.write(buff);
        }
        else if (id == R.id.nav_preset3) {
            preset = "3";
            byte[] buff = preset.getBytes();
            piThread.write(buff);
        }
        else if (id == R.id.nav_preset4) {
            preset = "4";
            byte[] buff = preset.getBytes();
            piThread.write(buff);
        }
        else if (id == R.id.nav_preset5) {
            preset = "5";
            byte[] buff = preset.getBytes();
            piThread.write(buff);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

/* <item android:title="">
        <menu>
            <item
                android:id="@+id/nav_send"
                android:icon="@drawable/ic_menu_send"
                android:title="@string/menu_send" />
        </menu>
    </item> */


