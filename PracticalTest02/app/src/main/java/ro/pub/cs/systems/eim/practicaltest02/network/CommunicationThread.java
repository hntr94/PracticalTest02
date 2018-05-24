package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.TimerInformation;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String command = bufferedReader.readLine();
            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();

            if (command == null || command.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }
            //HashMap<String, TimerInformation> data = serverThread.getData();
            //String data = serverThread.getData();

            /*if (timerInformation == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast Information is null!");
                return;
            }*/


            // result = new String(command);
            String result = null;

            if (command.compareToIgnoreCase("set") == 0) {
                result = new String(hour + ":" + minute);
                serverThread.setData(hour, minute);
            } else if (command.compareToIgnoreCase("reset") == 0){
                result = new String("00:00");
                serverThread.setData("00","00");
            } else if (command.compareToIgnoreCase("poll") == 0) {
                String wordLink = "http://www.oraexacta.net";
                Document doc = Jsoup.connect(wordLink).get();

                Element getHour = doc.select("div[id=timediv]").first();
                TimerInformation timerInformation = serverThread.getData();

                Log.d("Myhour", getHour.text());

                String clock = getHour.text();
                String[] tokens = clock.split(":");
                if (Integer.parseInt(tokens[0]) > Integer.parseInt(timerInformation.getHour())) {
                    result = new String("yes");
                } else if (Integer.parseInt(tokens[0]) < Integer.parseInt(timerInformation.getHour())) {
                    result = new String("no");
                } else if (Integer.parseInt(tokens[0]) == Integer.parseInt(timerInformation.getHour())) {
                    if (Integer.parseInt(tokens[1]) >= Integer.parseInt(timerInformation.getMinute())) {
                        result = new String("yes");
                    } else {
                        result = new String("no");
                    }
                }

                //result = new String(getHour.text());
            }


            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
