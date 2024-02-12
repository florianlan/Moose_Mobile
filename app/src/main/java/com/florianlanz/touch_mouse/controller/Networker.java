//Code from original Moose Project (Mahmoud Sadeghi)
//https://github.com/mahci/Proto-Moose

package com.florianlanz.touch_mouse.controller;

import static com.florianlanz.touch_mouse.data.Consts.STRINGS.*;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.florianlanz.touch_mouse.data.Memo;

@SuppressWarnings("ALL")
public class Networker {
    private String NAME = "Networker/";
    //-------------------------------------------------------------------------------
    private final String DESKTOP_IP = "192.168.8.10";
    private final int DESKTOP_PORT = 8000;
    private final long SUCCESS_VIBRATE_DUR = 500; // ms
    private final long CONN_THREAD_SLEEP_DUR = 2 * 1000; // ms

    private static Networker instance;

    private Socket socket;
    private ExecutorService executor;
    private PrintWriter outPW;
    private BufferedReader inBR;
    private Vibrator vibrator;
    private Handler mainThreadHandler;

    // -------------------------------------------------------------------------------

    //-- Runnable for connecting to desktop
    private class ConnectRunnable implements Runnable {
        String TAG = NAME + "connectRunnable";

        @Override
        public void run() {
            Log.d(TAG, "Connecting to desktop...");
            while (socket == null) {
                try {
                    socket = new Socket(DESKTOP_IP, DESKTOP_PORT);
                    Log.d(TAG, "Connection successful!");
                    vibrate(SUCCESS_VIBRATE_DUR);

                    // Start the main activity part
                    Message closeDialogMssg = new Message();
                    //closeDialogMssg.what = CLOSE_DLG;
                    mainThreadHandler.sendMessage(closeDialogMssg);

                    // Create buffers
//                    inBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outPW = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())), true);

                    // Send intro
                    sendMemo(new Memo(INTRO, INTRO, "init", "init"));

                    // Start receiving
                    // executor.execute(new InRunnable());

                } catch (ConnectException e) { // Server offline
                    Log.d(TAG, "Server not responding. Trying again in 2 sec.");
                    try {
                        Thread.sleep(CONN_THREAD_SLEEP_DUR);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    //-- Runnable for outgoing messages
    private class OutRunnable implements Runnable {
        String TAG = NAME + "OutRunnable";
        String message;

        public OutRunnable(String mssg) {
            message = mssg;
        }

        @Override
        public void run() {
            if (message != null && outPW != null) {
                outPW.println(message);
                outPW.flush();
                Log.d(TAG, message + " sent");
            } else {
                Log.d(TAG, "Problem in sending messages");
            }
        }
    }

    /**
     * Get the singletong instance
     *
     * @return instance
     */
    public static Networker get() {
        if (instance == null) instance = new Networker();
        return instance;
    }

    /**
     * Constructor
     */
    private Networker() {
        // Init the ExecuterService for running the threads
        executor = Executors.newCachedThreadPool();
    }

    /**
     * Connect to
     */
    public void connect() {
        String TAG = NAME + "connect";

        executor.execute(new ConnectRunnable());
    }

    public void resetConnection() {
        socket = null;
        outPW = null;
        inBR = null;
        connect();
    }

    /**
     * Send a memo to desktop
     *
     * @param memo Memo
     */
    public void sendMemo(Memo memo) {
        final String TAG = NAME + "sendMemmo";
        executor.execute(new OutRunnable(memo.toString()));
    }


    /**
     * Vibrate for millisec
     *
     * @param millisec time in milliseconds
     */
    private void vibrate(long millisec) {
        if (vibrator != null) vibrator.vibrate(millisec);
    }

    /**
     * Set the main handler (to MainActivity)
     *
     * @param mainHandler Handler to MainActivity
     */
    public void setMainHandler(Handler mainHandler) {
        mainThreadHandler = mainHandler;
    }

    /**
     * Set the vibrator (called from the MainActivity)
     *
     * @param vib Vibrator from system
     */
    public void setVibrator(Vibrator vib) {
        vibrator = vib;
    }

}
