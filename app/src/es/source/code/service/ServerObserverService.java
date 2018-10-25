package es.source.code.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import es.source.code.model.Food;

public class ServerObserverService extends Service {
    public Context app_context;
    //保存客户端的messenger
    private Messenger messenger_client;
    private boolean new_list = true;
    private boolean stop = false;
    Thread mThread;

    public ServerObserverService() {
    }

    public void onCreate() {
        super.onCreate();
        app_context = getApplicationContext();
        Log.i("server", "远程server");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public static boolean isAppOnForeground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcess = activityManager.getRunningAppProcesses();
        if (appProcess == null)
        {
            return false;
        }
        if (appProcess.get(0).processName.contains(packageName))
        {
            return true;
        }
        return false;
    }

    //处理客户消息与线程消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //msg 客户端传来的消息
                case 0:
                    //关闭线程
                    if (mThread != null && mThread.isAlive()) {
                        stop = true;
                    }
                    break;
                case 1:
                    stop = false;
                    if(mThread == null) {
                        mThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!stop) {
                                    //如果菜单列表为新则通知主界面更新
                                    if (new_list) {
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("cold_food", (Serializable) Get_Food("cold"));
                                        bundle.putSerializable("hot_food", (Serializable) Get_Food("hot"));
                                        bundle.putSerializable("sea_food", (Serializable) Get_Food("sea"));
                                        bundle.putSerializable("drink_food", (Serializable) Get_Food("drink"));
                                        message.setData(bundle);
                                        message.what = 2;
                                        mHandler.sendMessage(message);
                                        new_list = false;
                                    }
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("mThread", Thread.currentThread().getName() + "thread is alive");
                                }
                            }
                        });
                        mThread.start();
                    }else {
                        mThread.start();
                    }
                    messenger_client = msg.replyTo;
                    break;

                case 2:
                    //判断主进程是否在运行
                    if(isAppOnForeground(app_context)) {
                        Message message = Message.obtain();
                        message.replyTo = messenger_client;
                        message.setData(msg.getData());
                        message.what = 10;
                        try {
                            message.replyTo.send(message);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Messenger mMessenger = new Messenger(mHandler);

    private List<Food> Get_Food(String food_type){
        List<Food> Food_data_cold;
        List<Food> Food_data_hot;
        List<Food> Food_data_sea;
        List<Food> Food_data_drink;
        switch(food_type){
            case "cold":
                Food_data_cold = new LinkedList<>();
                Food_data_cold.add(new Food("凉拌豆腐丝", 10));
                Food_data_cold.add(new Food("凉拌香辣猪头肉", 30));
                Food_data_cold.add(new Food("白灼虾", 40));
                Food_data_cold.add(new Food("凉拌苦菊", 20));
                Food_data_cold.add(new Food("凉拌木耳白菜丝", 10));
                Food_data_cold.add(new Food("盐水鸡", 30));
                Food_data_cold.add(new Food("凉拌海蜇丝", 20));
                Food_data_cold.add(new Food("香煎柠香鳕鱼配蔬果沙拉", 10));
                Food_data_cold.add(new Food("鲜辣椒拌虾皮", 10));
                Food_data_cold.add(new Food("酸辣开胃的拍黄瓜", 10));
                Food_data_cold.add(new Food("凉拌藕片", 10));
                Food_data_cold.add(new Food("凉拌鸭肉", 10));
                Food_data_cold.add(new Food("麻酱拌豇豆角", 10));
                Food_data_cold.add(new Food("凉拌鸡", 20));
                Food_data_cold.add(new Food("醇香麻酱拉皮", 10));
                Food_data_cold.add(new Food("鹌鹑蛋拌黄瓜", 10));
                Food_data_cold.add(new Food("酸辣凤爪", 10));
                Food_data_cold.add(new Food("老醋木耳", 10));
                Food_data_cold.add(new Food("黄瓜洋葱拌筋", 10));
                Food_data_cold.add(new Food("蔬菜沙拉", 10));
                return Food_data_cold;

            case "hot":
                Food_data_hot = new LinkedList<>();
                Food_data_hot.add(new Food("老豆腐烧灯椒", 10));
                Food_data_hot.add(new Food("生菜煮鸭蛋", 20));
                Food_data_hot.add(new Food("香煎秋刀鱼", 30));
                Food_data_hot.add(new Food("芹菜粉条", 10));
                Food_data_hot.add(new Food("干煸芸豆", 20));
                Food_data_hot.add(new Food("重庆芋儿鸡", 30));
                Food_data_hot.add(new Food("宫爆鸡丁", 30));
                Food_data_hot.add(new Food("茄汁土豆", 20));
                Food_data_hot.add(new Food("素炒三丝", 30));
                Food_data_hot.add(new Food("腐竹烧排骨", 10));
                Food_data_hot.add(new Food("猪肉片炒花菜", 30));
                Food_data_hot.add(new Food("榨菜丝炒油豆角", 30));
                Food_data_hot.add(new Food("红烧茄子", 10));
                Food_data_hot.add(new Food("香菇童子鸡", 20));
                Food_data_hot.add(new Food("尖椒炒火腿肠", 30));
                Food_data_hot.add(new Food("蚝油红薯梗儿", 10));
                Food_data_hot.add(new Food("上汤西兰花", 20));
                Food_data_hot.add(new Food("桂花糯米藕", 30));
                Food_data_hot.add(new Food("小炒牛肉", 10));
                Food_data_hot.add(new Food("山药炒木耳", 20));
                Food_data_hot.add(new Food("蒜蓉快菜", 30));
                Food_data_hot.add(new Food("荷兰豆炒腊肉", 30));
                Food_data_hot.add(new Food("海蟹酱炒鸡蛋", 30));
                Food_data_hot.add(new Food("美味干锅排骨", 40));
                Food_data_hot.add(new Food("腊鸭腿青菜煮年糕", 20));
                Food_data_hot.add(new Food("红烧冻豆腐", 20));
                Food_data_hot.add(new Food("蒜蓉粉丝蒸虾", 30));
                Food_data_hot.add(new Food("大葱炒肉", 30));
                Food_data_hot.add(new Food("美味干锅排骨", 30));
                Food_data_hot.add(new Food("红烧冻豆腐", 20));
                return Food_data_hot;

            case "sea":
                Food_data_sea = new LinkedList<>();
                Food_data_sea.add(new Food("盐焗蛏", 30));
                Food_data_sea.add(new Food("原味皮皮虾", 50));
                Food_data_sea.add(new Food("烤箱版蒸梭子蟹", 60));
                Food_data_sea.add(new Food("吮指麻辣蟹", 50));
                Food_data_sea.add(new Food("烤花甲", 40));
                Food_data_sea.add(new Food("花甲粉丝", 30));
                Food_data_sea.add(new Food("香辣花甲", 40));
                Food_data_sea.add(new Food("炒海瓜子", 30));
                Food_data_sea.add(new Food("香辣螃蟹", 50));
                Food_data_sea.add(new Food("花甲炖蛋", 40));
                Food_data_sea.add(new Food("辣炒花蛤蜊", 30));
                Food_data_sea.add(new Food("黄瓜虾仁", 40));
                Food_data_sea.add(new Food("浇汁鲍鱼", 60));
                Food_data_sea.add(new Food("虾仁蒸豆腐", 30));
                Food_data_sea.add(new Food("金汤虾球烩豆腐", 40));
                Food_data_sea.add(new Food("煎虾", 30));
                Food_data_sea.add(new Food("清蒸大闸蟹", 70));
                Food_data_sea.add(new Food("清蒸大头虾", 30));
                Food_data_sea.add(new Food("蒜蓉酱蒸珍宝蚝", 70));
                Food_data_sea.add(new Food("炒鱿鱼", 40));
                Food_data_sea.add(new Food("浇汁鲍鱼", 80));
                Food_data_sea.add(new Food("虾仁土豆条", 30));
                Food_data_sea.add(new Food("肉螺炒鸡蛋", 30));
                Food_data_sea.add(new Food("花蛤炒虾蛄", 30));
                Food_data_sea.add(new Food("蒸螃蟹", 30));
                Food_data_sea.add(new Food("葱油青口贝", 50));
                Food_data_sea.add(new Food("黄金蟹斗", 30));
                Food_data_sea.add(new Food("蒜味虾", 30));
                Food_data_sea.add(new Food("清炒黄蚬", 30));
                Food_data_sea.add(new Food("红烧籽虾", 30));
                return Food_data_sea;

            case "drink":
                Food_data_drink = new LinkedList<>();
                Food_data_drink.add(new Food("可乐", 3));
                Food_data_drink.add(new Food("雪碧", 3));
                Food_data_drink.add(new Food("芬达", 3));
                Food_data_drink.add(new Food("火龙果汁", 15));
                Food_data_drink.add(new Food("南果梨汁", 10));
                Food_data_drink.add(new Food("金骏眉茶", 10));
                Food_data_drink.add(new Food("浓香豆浆", 10));
                Food_data_drink.add(new Food("糖桂花柠檬水", 10));
                Food_data_drink.add(new Food("猕猴桃酸奶杯", 10));
                Food_data_drink.add(new Food("树莓蜜", 10));
                Food_data_drink.add(new Food("红豆西米奶茶", 10));
                Food_data_drink.add(new Food("双豆豆浆", 10));
                Food_data_drink.add(new Food("椰奶拿铁咖啡", 10));
                Food_data_drink.add(new Food("鲜榨石榴汁", 10));
                Food_data_drink.add(new Food("西柚柠檬苏打水", 10));
                Food_data_drink.add(new Food("毛樱桃汁", 10));
                Food_data_drink.add(new Food("棉花糖巧克力热饮", 10));
                Food_data_drink.add(new Food("红豆莲藕糊", 10));
                return Food_data_drink;

            default:
                return null;
        }
    }
}
