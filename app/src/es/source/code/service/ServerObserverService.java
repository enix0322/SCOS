package es.source.code.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import es.source.code.model.Food;

import static java.lang.Thread.sleep;

public class ServerObserverService extends Service {
    public Context app_context;
    //保存客户端的messenger
    private Messenger messenger_client;
    private int new_food;
    private boolean stop = false;
    Thread mThread;
    String baseUrl = "http://192.168.1.33:8080/web/FoodUpdateService";
    //String baseUrl = "http://192.168.43.214:8080/web/FoodUpdateService";
    String foodType[] = {"cold_food","hot_food","sea_food","drink_food"};
    public ServerObserverService() {
    }

    public void onCreate() {
        super.onCreate();
        app_context = getApplicationContext();
        Log.i("server", "远程server");
        if(!isAppOnForeground(app_context)) {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
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
                    new_food = 1;
                    if(stop == true) {
                        stop = false;
                    }
                    if(mThread == null) {
                        mThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!stop) {
                                    for(int i=0;i<4;i++) {
                                        sendJson(foodType[i]);
                                    }
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
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
                    List<Food> food_list;
                    String food_type = msg.getData().getString("FoodType");
                    food_list = (ArrayList)msg.getData().getSerializable(food_type);
                    if(isAppOnForeground(app_context)) {
                        if(food_list.size()>0) {
                            Message message = Message.obtain();
                            message.replyTo = messenger_client;
                            message.setData(msg.getData());
                            message.what = 10;
                            try {
                                message.replyTo.send(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Intent intent = new Intent();
                    intent.setClass(ServerObserverService.this, UpdateService.class);
                    intent.putExtra("String", "NewFood");
                    intent.putExtra("Food", (ArrayList<Food>) food_list);
                    startService(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Messenger mMessenger = new Messenger(mHandler);

    private void sendJson(String FoodType) {
        try {
            //合成参数
            JSONObject json = new JSONObject();
            json.put("online", 1);
            json.put("new_food", new_food);
            new_food = 0;
            json.put("FoodType", FoodType);
            System.out.println("=============="+json.toString());
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            String content = String.valueOf(json);
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(content);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("==============开始连接！");
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String recData = null;
                String result = "";
                while ((recData = bf.readLine()) != null){
                    result += recData;
                }
                in.close();
                urlConn.disconnect();
                JSONArray json_array = JSONArray.fromObject(result);
                if(json_array.toString() != "") {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    List<Food> food_list = new ArrayList<>(FoodGetFromJson(json_array));
                    bundle.putSerializable(FoodType, (Serializable)food_list);
                    bundle.putString("FoodType",FoodType);
                    message.setData(bundle);
                    message.what = 2;
                    mHandler.sendMessage(message);
                    System.out.println("==============接收长度："+ food_list.size());
                }

                /*if(result != "") {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    List<Food> food_list = new ArrayList<>(FoodGetFromXml(result));
                    bundle.putSerializable(FoodType, (Serializable) food_list);
                    bundle.putString("FoodType", FoodType);
                    message.setData(bundle);
                    message.what = 2;
                    mHandler.sendMessage(message);
                    System.out.println("==============接收长度：" + food_list.size());
                }*/
            } else {
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
        }
    }

    private List<Food> FoodGetFromJson(JSONArray json_array){
        if (json_array == null)
            return new ArrayList<>();
        List<Food> foodList = new ArrayList<>();
        JSONObject jsonObject = null;
        Food info = null;
        for (int i = 0; i < json_array.size(); i++) {
            jsonObject = json_array.getJSONObject(i);
            info = new Food(jsonObject.getString("food_name"),jsonObject.getInt("food_price"));
            foodList.add(info);
        }
        return foodList;
    }

    private List<Food> FoodGetFromXml(String Xml){
        if (Xml == null)
            return new ArrayList<>();
        List<Food> foodList = new ArrayList<>();
        Food info = null;
        int index = 0;
        int count = 0;
        while((index = Xml.indexOf("Food id", index)) != -1) {
            index += "Food id".length();
            count++;
        }
        String str[] =Xml.split("<Food id");
        for (int i = 1; i < count+1; i++) {
            String food_str[] =str[i].split("<food_name>");
            String food_name[] = food_str[1].split("</food_name>");
            String food_price[] = food_name[1].split("<food_price>");
            String food_p[] = food_price[1].split("</food_price>");
            info = new Food(food_name[0],Integer.valueOf(food_p[0]));
            foodList.add(info);
        }
        return foodList;
    }
}
