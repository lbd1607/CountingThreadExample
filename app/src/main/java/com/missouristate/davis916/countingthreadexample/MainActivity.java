package com.missouristate.davis916.countingthreadexample;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*
 * Laura Davis CIS 262-902
 * 28 March 2018
 * This app demonstrates threading in Android
 * using a looper and handler to update the UI.
 * The counter, running on a background thread
 * and output being updated in the UI, will start
 * incrementing when the user presses the start button.
 * When the user presses the stop button, the thread will
 * stop and the buttons will no longer function. This
 * design ensures that an IllegalThreadStateException
 * will not occur if the start button were clicked again.
 */
public class MainActivity extends AppCompatActivity {
    //Declare UI TextView and count object
    private TextView countTextView;
    private Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference the TextView UI element in the layout
        countTextView = (TextView) findViewById(R.id.textView);

        //Initialize the counter
        count = 0;

        //Create thread
        //Must be final to be used in onClick()
        final Thread thread = new Thread(countNumbers);

        Button btnStart = (Button) findViewById(R.id.button);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                //Start thread on button click
                thread.start();
            }
        });//end onClick event

        Button btnStop = (Button) findViewById(R.id.button2);
        btnStop.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                //Stop the thread on button click
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });//end onClick event
    }//onCreate()

    //Initialize the counter to zero each time the app launches
    @Override
    protected void onStart(){
        super.onStart();
        count = 0;
    }

        //*********************************** Runnable *********************************** /
        private Runnable countNumbers = new Runnable() {
            private static final int DELAY = 500;

            @Override
            public void run() {
                try {
                    while (true) {
                        count++;
                        Thread.sleep(DELAY);
                        threadHandler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//end run()
        };//end Runnable countNumbers *************************************************** /

    //*********************************** Handler *************************************** /
    public Handler threadHandler = new Handler(Looper.getMainLooper()){
        public void handleMessage (android.os.Message message){
            countTextView.setText(count.toString());
        }//end handleMessage
    };//end Handler threadHandler ****************************************************** /

    //Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }//end createOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button,
        //as long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

}//end MainActivity class
