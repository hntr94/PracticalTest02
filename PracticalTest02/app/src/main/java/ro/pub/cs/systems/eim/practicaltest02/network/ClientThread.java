package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.TimerInformation;

public class ClientThread extends Thread {

    private String hour;
    private String minute;
    private boolean active;
    private String address;
    private int port;
    private String command;
    private TextView timerTextView;
    TimerInformation timeInformation;

    private Socket socket;


    public ClientThread(String command, TimerInformation timeInformation, boolean active, String address, int port, TextView timerTextView) {
        this.active = active;
        //this.hour = hour;
        //this.minute = minute;
        this.port = port;
        this.address = address;
        this.command = command;
        this.timerTextView = timerTextView;
        this.timeInformation = timeInformation;

    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(command);
            printWriter.flush();
            printWriter.println(timeInformation.getHour());
            printWriter.flush();
            printWriter.println(timeInformation.getMinute());
            printWriter.flush();

            String timerInformation;
            while ((timerInformation = bufferedReader.readLine()) != null) {
                Log.d(Constants.TAG,"[ClientThread] " + timerInformation);
                final String finalizedTimerInformation = timerInformation;
               timerTextView.post(new Runnable() {
                   @Override
                    public void run() {
                       timerTextView.setText(finalizedTimerInformation);
                   }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
