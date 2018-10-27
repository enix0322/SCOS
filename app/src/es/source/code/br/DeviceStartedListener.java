package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.source.code.activity.FoodView;
import es.source.code.activity.MainScreen;
import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
              if (intent.getAction().equals(ACTION_BOOT)) {
                  Log.i(TAG, "BootBroadcastReceiver onReceive(), Do thing!");
                  Intent tIntent = new Intent(context, UpdateService.class);
                  tIntent.putExtra("String", "Boot_Completed");
                  //启动指定Service
                  context.startService(tIntent);
              }

    }
}
