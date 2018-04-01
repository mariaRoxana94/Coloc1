package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.R;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.general.Constants;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.service.PracticalTest01Service;

public class PracticalTest01MainActivity extends AppCompatActivity {
    EditText left;
    EditText right;
    Button navigate;
    Button leftButton;
    Button rightButton;
    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    int serviceStatus = Constants.SERVICE_STOPPED;

    private IntentFilter intentFilter = new IntentFilter();

    ButtonClickListner buttonClickListner = new ButtonClickListner();
    private  class ButtonClickListner implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == leftButton.getId()) {
                int leftNumber = Integer.parseInt(left.getText().toString());
                leftNumber++;
                left.setText(String.valueOf(leftNumber));
            }
            if (view.getId() == rightButton.getId()) {
                int rightNumber = Integer.parseInt(right.getText().toString());
                rightNumber++;
                right.setText(String.valueOf(rightNumber));

            }
            if (view.getId() == navigate.getId()) {

                Intent intent = new Intent(getApplicationContext(), practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view.PracticalTest01SecondaryActivity.class);
                int numberOfClicks =  Integer.parseInt(right.getText().toString()) + Integer.parseInt(left.getText().toString());
                intent.putExtra("numberOfClicks", numberOfClicks);
                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);

            }

            int leftNumberOfClicks = Integer.parseInt(left.getText().toString());
            int rightNumberOfClicks = Integer.parseInt(right.getText().toString());

            if (leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicks);
                intent.putExtra("secondNumber", rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
                navigate.setText(getResources().getString(R.string.centerButtonTwo));
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log .d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        left = (EditText) findViewById(R.id.editleft);
        left.setText(String.valueOf(0));

        right = (EditText) findViewById(R.id.editright);
        right.setText(String.valueOf(0));

        navigate = (Button) findViewById(R.id.center_button);
        navigate.setOnClickListener(buttonClickListner);

        leftButton = (Button) findViewById(R.id.press_me);
        leftButton.setOnClickListener(buttonClickListner);

        rightButton = (Button) findViewById(R.id.press_me_too);
        rightButton.setOnClickListener(buttonClickListner);

        //Bundle este nenul în situația în care există o stare anterioară
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("leftCount")) {
                left.setText(savedInstanceState.getString("leftCount"));
            } else {
                left.setText(String.valueOf("0"));
            }

            if (savedInstanceState.containsKey("rightCount")) {
                right.setText(savedInstanceState.getString("rightCount"));
            } else {
                right.setText(String.valueOf("0"));
            }
        } else {
            left.setText(String.valueOf("0"));
            right.setText(String.valueOf("0"));
        }
        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

    }

    //sunt salvate infor prin Bundle
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("leftCount", left.getText().toString());
        outState.putString("rightCount", right.getText().toString());
    }

    //invocată în mod automat situația în care există o stare anterioară;
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("leftCount")) {
            left.setText(savedInstanceState.getString("leftCount"));
        } else {
            left.setText(String.valueOf("0"));
        }

        if (savedInstanceState.containsKey("rightCount")) {
            right.setText(savedInstanceState.getString("rightCount"));
        } else {
            right.setText(String.valueOf("0"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
            navigate.setText(getResources().getString(R.string.centerButton));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log .d("[DEBUG]", "onResume");
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        Log .d("[DEBUG]", "onPause");
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log .d("[DEBUG]", "onDestroy");
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
