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

    private String address;
    private int port;
    private TextView timerTextView;
    //TimerInformation timeInformation;
    private String word;

    private Socket socket;


    public ClientThread(String word, String address, int port, TextView timerTextView) {

        this.port = port;
        this.address = address;
        this.word = word;
        this.timerTextView = timerTextView;
        //this.timeInformation = timeInformation;

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

            printWriter.println(word);
            printWriter.flush();

            String wordInformation;
            while ((wordInformation = bufferedReader.readLine()) != null) {
                Log.d(Constants.TAG,"[ClientThread] " + wordInformation);
                final String finalizedWordInformation = wordInformation;
               timerTextView.post(new Runnable() {
                   @Override
                    public void run() {
                       timerTextView.append(finalizedWordInformation);
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
