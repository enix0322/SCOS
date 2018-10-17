package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import es.source.code.model.User;


public class MainScreen extends Activity {

    //private TextView mTextMessage;
    //private BottomNavigationView navigation;
    //private BottomNavigationView hide_navigation ;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    User user;
    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.navigation_order:
                    mTextMessage.setText(R.string.title_home);
                    intent.setClass(MainScreen.this, FoodView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("String", "FromMainScreen");
                    startActivity(intent);
                    return true;
                case R.id.navigation_watch_order:
                    mTextMessage.setText(R.string.title_dashboard);
                    intent.setClass(MainScreen.this, FoodOrderView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("String", "FromMainScreen");
                    startActivity(intent);
                    return true;
                case R.id.navigation_log_or_sign_in:
                    intent.setClass(MainScreen.this, LoginOrRegister.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("String", "FromMainScreen");
                    startActivity(intent);
                    return true;
                case R.id.navigation_sys_help:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    }/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnHideNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_log_or_sign_in:
                    Intent intent = new Intent();
                    intent.setClass(MainScreen.this, LoginOrRegister.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
                    intent.putExtra("String", "FromMainScreen");
                    startActivity(intent);
                    return true;
                case R.id.navigation_sys_help:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        gridView = (GridView) findViewById(R.id.gridview);
        user = new User("temp","0");
        change_gridview();
        String[] from={"img","text"};

        int[] to={R.id.img,R.id.text};

        adapter=new SimpleAdapter(this, dataList, R.layout.gridview_item, from, to);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent();
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch(dataList.get(arg2).get("text").toString()){
                    case "点菜":
                        intent.setClass(MainScreen.this, FoodView.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("String", "FromMainScreen");
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case "查看订单":
                        intent.setClass(MainScreen.this, FoodOrderView.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("String", "FromMainScreen");
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    case "登陆/注册":
                        intent.setClass(MainScreen.this, LoginOrRegister.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("String", "FromMainScreen");
                        startActivity(intent);
                        break;
                    case "系统帮助":
                        intent.setClass(MainScreen.this, scosHelper.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("String", "FromMainScreen");
                        startActivity(intent);
                        break;
                }
            }
        });
        //mTextMessage = (TextView) findViewById(R.id.message);

        //navigation = findViewById(R.id.navigation);
        //hide_navigation = findViewById(R.id.hide_navigation);

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //hide_navigation.setOnNavigationItemSelectedListener(mOnHideNavigationItemSelectedListener);

        //change_navigation();
    }

    void initData() {
        //图标
        int icno[] = {
                R.drawable.ic_restaurant_menu_black_24dp,
                R.drawable.ic_assignment_black_24dp,
                R.drawable.ic_person_black_24dp,
                R.drawable.ic_help_black_24dp
        };
        //图标下的文字
        String name[]={"点菜","查看订单","登陆/注册","系统帮助"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

    void initData_hide() {
        //图标
        int icno[] = {
                R.drawable.ic_person_black_24dp,
                R.drawable.ic_help_black_24dp
        };
        //图标下的文字
        String name[]={"登陆/注册","系统帮助"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //change_navigation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void change_gridview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String strContentString = "";
        if(bundle != null) {
            strContentString = bundle.getString("String");
        }
        switch (strContentString) {
            case "FromEntry":
                if(getIntent().getIntExtra("loginState", 0)==1) {
                    initData();
                }else{
                    initData_hide();
                }
                break;
            case "LoginSuccess":
                if(getIntent().getIntExtra("loginState", 0)==1) {
                    initData();
                    //获取登陆成功的返回值
                    user = (User) getIntent().getSerializableExtra("User");
                }else{
                    initData_hide();
                }
                break;
            case "RegisterSuccess":
                if(getIntent().getIntExtra("loginState", 0)==1) {
                    initData();
                    //获取注册成功的返回值
                    user = (User) getIntent().getSerializableExtra("User");
                    Toast.makeText(MainScreen.this, "欢迎您成为 SCOS 新用户", Toast.LENGTH_SHORT).show();
                }else{
                    initData_hide();
                }
                break;
            case "FromFoodView":
                initData();
                user = (User)getIntent().getSerializableExtra("User");
                break;
            case "FromFoodOrderView":
                initData();
                user = (User)getIntent().getSerializableExtra("User");
                break;
            case "Return":
                initData_hide();
                break;
            default:
                initData_hide();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
