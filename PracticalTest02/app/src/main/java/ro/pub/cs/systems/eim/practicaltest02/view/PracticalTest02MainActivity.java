package ro.pub.cs.systems.eim.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.model.TimerInformation;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText cityEditText = null;
    private Spinner informationTypeSpinner = null;
    private Button getWeatherForecastButton = null;
    private TextView timerTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private EditText clientHourEditText;
    private EditText clientMinEditText;
    private Button clientSetButton;
    private Button clientResetButton;
    private Button clientPollButton;
    private String command;
    private TimerInformation timerInformation;

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private TimerButtonClickListener timerButtonClickListener = new TimerButtonClickListener();
    private class TimerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hour = clientHourEditText.getText().toString();
            String minute = clientMinEditText.getText().toString();

            if (hour == null || hour.isEmpty() ||
                    minute == null || minute.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            timerTextView.setText(Constants.EMPTY_STRING);

            switch(view.getId()) {
                case R.id.set_time_button:
                    command = new String("set");
                    timerInformation = new TimerInformation(hour, minute);
                    break;
                case R.id.reset_time_button:
                    command = new String("reset");
                    clientHourEditText.setText(null);
                    clientMinEditText.setText(null);
                    break;
                case R.id.poll_time_button:
                    command = new String("poll");
                    break;
                default:
                    command = new String("");
            }

            clientThread = new ClientThread(
                    command, timerInformation, false,
                    clientAddress, Integer.parseInt(clientPort), timerTextView
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        clientHourEditText = (EditText)findViewById(R.id.hour_edit_text);
        clientMinEditText = (EditText)findViewById(R.id.minute_edit_text);
        clientSetButton = (Button)findViewById(R.id.set_time_button);
        clientResetButton = (Button)findViewById(R.id.reset_time_button);
        clientPollButton = (Button)findViewById(R.id.poll_time_button);

        clientSetButton.setOnClickListener(timerButtonClickListener);
        clientResetButton.setOnClickListener(timerButtonClickListener);
        clientPollButton.setOnClickListener(timerButtonClickListener);

        //cityEditText = (EditText)findViewById(R.id.city_edit_text);
        //informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
        //getWeatherForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        //getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        timerTextView = (TextView)findViewById(R.id.weather_forecast_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
