package com.example.autogaze;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.autogaze.MyBluetoothService;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmAdapter;

    private static final String TAG = "AutogazeConnect";
    private static final UUID MY_UUID = UUID.fromString("c4e9fdf7-8711-403c-aa44-85b791bef4d3");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmAdapter = adapter;
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        mmAdapter.cancelDiscovery();
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.

            /*The problem occurs here*/

            mmSocket.connect();
            Log.e(TAG, "You connected!");

        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                Log.e(TAG, "You didn't connect :(");

                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        MyBluetoothService my_guy = new MyBluetoothService();
        MyBluetoothService.ConnectedThread piThread = my_guy.new ConnectedThread(mmSocket);
        piThread.run();
        String testString = "Test String";
        piThread.write(testString.getBytes());
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
