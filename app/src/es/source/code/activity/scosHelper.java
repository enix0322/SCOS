package es.source.code.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.future.scos.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import es.source.code.model.EmailSender;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class scosHelper extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(scosHelper.this, "求助邮件已发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoshelper);
        gridView = (GridView) findViewById(R.id.help_gridview);
        change_gridview();
        String[] from = {"img", "text"};
        int[] to = {R.id.img, R.id.text};

        final Context context = this;

        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_item, from, to);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent();

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (dataList.get(arg2).get("text").toString()) {
                    case "用户使用协议":
                        //intent.setClass(MainScreen.this, FoodView.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("String", "FromMainScreen");
                        //intent.putExtra("User", user);
                        //startActivity(intent);
                        break;
                    case "关于系统":
                        //intent.setClass(MainScreen.this, FoodOrderView.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("String", "FromMainScreen");
                        //intent.putExtra("User", user);
                        //startActivity(intent);
                        break;
                    case "电话人工帮助":

                        Uri uri = Uri.parse("tel:" + 5554);
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_GRANTED);
                            return;
                        }
                        startActivity(intent);
                        break;
                    case "短信帮助":
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_GRANTED);
                            return;
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        String content = "test scos helper";
                        smsManager.sendTextMessage("5554", null, content, null, null);
                        Toast.makeText(scosHelper.this, "求助短信发送成功", Toast.LENGTH_SHORT).show();
                        break;

                    case "邮件帮助":
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSION_GRANTED);
                            return;
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EmailSender sender = new EmailSender();
                                    //设置服务器地址和端口，可以查询网络
                                    sender.setProperties("smtp.163.com", "25");
                                    //分别设置发件人，邮件标题和文本内容
                                    sender.setMessage("enix0322@163.com", "testscos","hello");
                                    //设置收件人
                                    sender.setReceiver(new String[]{"1544097067@qq.com"});
                                    //添加附件换成你手机里正确的路径
                                    // sender.addAttachment("/sdcard/emil/emil.txt");
                                    //发送邮件
                                    sender.sendEmail("smtp.163.com", "enix0322@163.com", "enix0322");
                                } catch (AddressException e) {
                                    e.printStackTrace();
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                }

                                Message message=new Message();
                                message.what=1;
                                mHandler.sendMessage(message);
                            }
                        }).start();


                        break;
                }
            }
        });
    }

    void initData() {
        //图标
        int icno[] = {
                R.drawable.ic_account_balance_black_24dp,
                R.drawable.ic_settings_black_24dp,
                R.drawable.ic_local_phone_black_24dp,
                R.drawable.ic_message_black_24dp,
                R.drawable.ic_email_black_24dp
        };
        //图标下的文字
        String name[]={"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

    private void change_gridview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String strContentString = "";
        initData();
        if (bundle != null) {
            strContentString = bundle.getString("String");
        }
    }

}
