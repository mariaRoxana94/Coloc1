package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class PracticalTest01Service extends Service {

    private ProcesingThread procesingThread =  null;

    //onStartCommand este apelata automat atunci cand
    // startService() este apelat din Activitatea principala;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int firstNumber = intent.getIntExtra("firstNumber",-1);
        int secondNumber = intent.getIntExtra("secondNumber", -1);
        procesingThread = new ProcesingThread(getApplicationContext(), firstNumber, secondNumber);
        //asta ai uitat si deaseamenea cheile !!!
        procesingThread.start();

        return Service.START_REDELIVER_INTENT;
    }

    //null pt ca este started service, si NU bounded
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onDestroy() {
        procesingThread.stopThread();
    }
}
