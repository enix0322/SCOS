package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.future.scos.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.MainScreen;
import es.source.code.model.Food;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "es.source.code.service.action.FOO";
    private static final String ACTION_BAZ = "es.source.code.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "es.source.code.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "es.source.code.service.extra.PARAM2";

    private MediaPlayer mPlayer;
    private AudioManager audioService ;

    public UpdateService() {
        super("UpdateService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public void onCreate() {
        super.onCreate();
        Log.i("server", "updata_server");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getExtras().getString("String");
            switch(action){
                case "NewFood":
                    List<Food> food_list = (ArrayList<Food>) intent.getSerializableExtra("Food");
                    Food food;
                    if(food_list.size()>0) {
                        food = food_list.get(food_list.size() - 1);
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification.Builder builder = new Notification.Builder(UpdateService.this);
                        builder.setAutoCancel(true);
                        //设置跳转的页面
                        Intent detailed_intent = new Intent(UpdateService.this, MainScreen.class);
                        detailed_intent.putExtra("String", "UpdateService");
                        //detailed_intent.putExtra("Food", food);
                        audioService = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
                        PendingIntent tIntent = PendingIntent.getActivity(UpdateService.this, 100, detailed_intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        //设置通知栏标题
                        builder.setContentTitle("新的美味");
                        //设置通知栏内容
                        builder.setContentText("美味的" + food.get_food_name() +
                                "，"+food.get_food_price()+"元！"+
                                "，共"+food.get_food_store_num()+"份！");
                        //设置跳转
                        builder.setContentIntent(tIntent);
                        //设置图标
                        builder.setSmallIcon(R.drawable.ic_restaurant_menu_black_24dp);
                        //设置
                        builder.setDefaults(Notification.DEFAULT_ALL);
                        //创建通知类
                        Notification notification = builder.build();
                        //显示在通知栏
                        manager.notify(0, notification);
                    }
                    break;
                case "Boot_Completed":
                    Intent intent_server = new Intent(UpdateService.this, ServerObserverService.class);
                    startService(intent_server);
                    break;
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
