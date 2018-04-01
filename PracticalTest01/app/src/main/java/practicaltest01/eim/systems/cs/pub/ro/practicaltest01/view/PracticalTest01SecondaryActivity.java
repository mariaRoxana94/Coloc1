package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.R;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {
    Button ok;
    Button cancel;
    TextView sum;
    ClickListener clickListener = new ClickListener();
    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == ok.getId()) {
                setResult(RESULT_OK, null);
            }else if (view.getId() == cancel.getId()) {
                setResult(RESULT_CANCELED, null);
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        sum = (TextView) findViewById(R.id.sum);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey("numberOfClicks")) {
            int numberOfClick = intent.getIntExtra("numberOfClicks", -1);
            sum.setText(String.valueOf(numberOfClick));
        }

        ok = (Button) findViewById(R.id.ok_button);
        ok.setOnClickListener(clickListener);

        cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(clickListener);
    }
}
