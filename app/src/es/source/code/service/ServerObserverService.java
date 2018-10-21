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
import android.os.RemoteException;
import android.util.Log;

import com.future.scos.IMyAidlInterface;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import es.source.code.model.Food;

public class ServerObserverService extends Service {
    public Context activity_context;
    int service_type;
    //private MyBinder binder = new MyBinder();

    public ServerObserverService() {
    }

    public void onCreate() {
        super.onCreate();
        activity_context = getApplicationContext();
        Log.i("server", "远程server");

    }

    /*public class MyBinder extends Binder
    {
        public void Set_Context(Context context){
            activity_context = context;
        }

        public ServerObserverService getService() {
            return ServerObserverService.this;
        }
    }*/

    IMyAidlInterface.Stub stub  = new IMyAidlInterface.Stub() {
        @Override
        public IBinder getMessage() throws RemoteException {
            System.out.println("客户端通过AIDL与远程后台成功通信");
            return mMessenger.getBinder();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("执行了onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }



    public static boolean isAppOnForeground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.AppTask> appTask = activityManager.getAppTasks();
        if (appTask == null)
        {
            return false;
        }
        if (appTask.get(0).getTaskInfo().toString().contains(packageName))
        {
            return true;
        }
        return false;
    }

    //处理接收到的消息
    private Messenger mMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(final Message msgfromClient) {
            final Message msgToClient = Message.obtain(msgfromClient);//返回给客户端的消息
            switch (msgfromClient.what) {
                //msg 客户端传来的消息
                case 1:
                    msgToClient.what = 10;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msgToClient.what = 10;
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (isAppOnForeground(activity_context)) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("cold_food", (Serializable)Get_Food("cold"));
                                bundle.putSerializable("hor_food", (Serializable)Get_Food("hot"));
                                bundle.putSerializable("sea_food", (Serializable)Get_Food("sea"));
                                bundle.putSerializable("drink_food", (Serializable)Get_Food("drink"));
                                msgToClient.setData(bundle);
                                try {
                                    msgfromClient.replyTo.send(msgToClient);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            } else if (!isAppOnForeground(activity_context)) {
                                stopSelf();
                            }
                        }
                    }).start();
                    break;
            }
            super.handleMessage(msgfromClient);
        }
    });

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
