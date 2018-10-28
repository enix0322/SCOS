package es.source.code.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import com.future.scos.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import es.source.code.Fragment.FragmentFood;
import es.source.code.model.Food;
import es.source.code.model.FoodList;
import es.source.code.model.User;

public class FoodView extends AppCompatActivity implements FragmentFood.CallBack{

    private TabLayout tablayout;
    private ViewPager viewPager;
    private List<String> list;
    private int f_pos = 0;
    private boolean start_service;

    List<Food> Food_data_cold = new LinkedList();
    List<Food> Food_data_hot = new LinkedList();
    List<Food> Food_data_sea = new LinkedList();
    List<Food> Food_data_drink = new LinkedList();

    private Intent intentService;
    private IBinder binder;
    private Messenger mServerMessenger;
    ServiceConnection connection;

    MyPagerAdapter myPagerAdapter;
    private ArrayList<FragmentFood> fragments = new ArrayList<>();
    User user;

    //数据源
    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};

    private Handler mHandler =new Handler() {
        @Override
        public void handleMessage(Message msgFromServer)
        {
            switch (msgFromServer.what)
            {
                case 10:
                    if(msgFromServer.getData().getSerializable("cold_food") != null) {
                        Food_data_cold = new LinkedList((ArrayList<Food>) msgFromServer.getData().getSerializable("cold_food"));
                    }
                    if(msgFromServer.getData().getSerializable("hot_food") != null) {
                        Food_data_hot = new LinkedList((ArrayList)msgFromServer.getData().getSerializable("hot_food"));
                    }
                    if(msgFromServer.getData().getSerializable("sea_food") != null) {
                        Food_data_sea = new LinkedList((ArrayList) msgFromServer.getData().getSerializable("sea_food"));
                    }
                    if(msgFromServer.getData().getSerializable("drink_food") != null) {
                        Food_data_drink = new LinkedList((ArrayList) msgFromServer.getData().getSerializable("drink_food"));
                    }
                    myPagerAdapter.remove();
                    fragments.add(new FragmentFood());
                    fragments.add(new FragmentFood());
                    fragments.add(new FragmentFood());
                    fragments.add(new FragmentFood());

                    FoodList foodList = new FoodList();
                    foodList.setFood_data_cold(Food_data_cold);
                    foodList.setFood_data_hot(Food_data_hot);
                    foodList.setFood_data_sea(Food_data_sea);
                    foodList.setFood_data_drink(Food_data_drink);
                    user.setFoodList(foodList);

                    myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
                    viewPager.setAdapter(myPagerAdapter);
                    tablayout.setupWithViewPager(viewPager,true);
                    tablayout.getTabAt(f_pos).select();
                    break;
            }
            super.handleMessage(msgFromServer);
        }
    };

    private Messenger mMessenger = new Messenger(mHandler);

    private void bindRemoteService() {
        Intent intentService = new Intent();
        intentService.setAction ("com.future.scos.ServerObserverService");
        intentService.setPackage("com.future.scos");
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //当连接service成功后发送what=1
                mServerMessenger = new Messenger(iBinder);
                Message msg = new Message();
                msg.what = 1;
                msg.replyTo = mMessenger;
                try {
                    mServerMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                binder = null;
            }
        };
        bindService(intentService, connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        user = (User)getIntent().getSerializableExtra("User");
        f_pos = getIntent().getIntExtra("int", 0);

        FragmentFood.setCallBack(this);

        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        list.add(titles[2]);
        list.add(titles[3]);

        set_data();

        fragments.add(new FragmentFood());
        fragments.add(new FragmentFood());
        fragments.add(new FragmentFood());
        fragments.add(new FragmentFood());

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setAdapter(myPagerAdapter);
        tablayout.setupWithViewPager(viewPager,true);
        tablayout.getTabAt(f_pos).select();
        //每条之间的分割线

        LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection != null) {
            unbindService(connection);
            Log.i("FoodView", "解除远程service的绑定！");
        }
    }

    @Override
    public void event(Food f) {
        if(f.get_food_order() == true) {
            user.Add_Not_Order_Food(f);
        }
        else if(f.get_food_order() == false) {
            user.Delet_Not_Order_Food_List(f);
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private FragmentManager fm;
        ArrayList<FragmentFood> fragments;
        public MyPagerAdapter(FragmentManager fm, ArrayList<FragmentFood> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            if (list.get(position).equals(titles[0])){
                fragments.get(0).addFood(user.getFoodList().getFood_data_cold());
                fragments.get(0).set_user(user);
                fragments.get(0).set_position(position);
            }else if (list.get(position).equals(titles[1])){
                fragments.get(1).addFood(user.getFoodList().getFood_data_hot());
                fragments.get(1).set_user(user);
                fragments.get(1).set_position(position);
            }else if (list.get(position).equals(titles[2])){
                fragments.get(2).addFood(user.getFoodList().getFood_data_sea());
                fragments.get(2).set_user(user);
                fragments.get(2).set_position(position);
            }else if (list.get(position).equals(titles[3])){
                fragments.get(3).addFood(user.getFoodList().getFood_data_drink());
                fragments.get(3).set_user(user);
                fragments.get(3).set_position(position);
            }
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return titles.length;
        }

        public void remove() {
            fragments.clear();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_menu, menu);
        menu.findItem(R.id.ordered);
        menu.findItem(R.id.order_list);
        menu.findItem(R.id.call_server);

        if (start_service) {
            menu.findItem(R.id.RT_service).setVisible(false);
            menu.findItem(R.id.RT_service_close).setVisible(true);
        } else {
            menu.findItem(R.id.RT_service).setVisible(true);
            menu.findItem(R.id.RT_service_close).setVisible(false);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.ordered:
                intent.setClass(FoodView.this, FoodOrderView.class);
                intent.putExtra("String", "FromFoodView");
                intent.putExtra("User", user);
                intent.putExtra("tab", 0);
                startActivity(intent);
                break;
            case R.id.order_list:
                intent.setClass(FoodView.this, FoodOrderView.class);
                intent.putExtra("String", "FromFoodView");
                intent.putExtra("User", user);
                intent.putExtra("tab", 1);
                startActivity(intent);
                break;
            case R.id.call_server:
                break;

            case R.id.RT_service:
                start_service = true;
                invalidateOptionsMenu();
                if(mServerMessenger == null){
                    bindRemoteService();
                }else {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.replyTo = mMessenger;
                    try {
                        mServerMessenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.RT_service_close:
                start_service = false;
                invalidateOptionsMenu();
                Message msg = new Message();
                msg.what = 0;
                msg.replyTo = mMessenger;
                try {
                    mServerMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(FoodView.this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("String", "FromFoodView");
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void set_data(){
        Food_data_cold = user.getFoodList().getFood_data_cold();
        Food_data_hot = user.getFoodList().getFood_data_hot();
        Food_data_sea = user.getFoodList().getFood_data_sea();
        Food_data_drink = user.getFoodList().getFood_data_drink();

        Iterator<Food> it_user = user.Get_Not_Order_Food_List().iterator();
        Iterator<Food> it_data = Food_data_cold.iterator();
        while(it_data.hasNext()){
            Food f= it_data.next();
            while(it_user.hasNext()){
                Food fd= it_user.next();
                if(f.get_food_name().equals(fd.get_food_name())){
                    f.set_food_order(true);
                }
            }
            it_user = user.Get_Not_Order_Food_List().iterator();
        }

        it_user = user.Get_Not_Order_Food_List().iterator();
        it_data = Food_data_hot.iterator();
        while(it_data.hasNext()){
            Food f= it_data.next();
            while(it_user.hasNext()){
                Food fd= it_user.next();
                if(f.get_food_name().equals(fd.get_food_name())){
                    f.set_food_order(true);
                }
            }
            it_user = user.Get_Not_Order_Food_List().iterator();
        }

        it_user = user.Get_Not_Order_Food_List().iterator();
        it_data = Food_data_sea.iterator();
        while(it_data.hasNext()){
            Food f= it_data.next();
            while(it_user.hasNext()){
                Food fd= it_user.next();
                if(f.get_food_name().equals(fd.get_food_name())){
                    f.set_food_order(true);
                }
            }
            it_user = user.Get_Not_Order_Food_List().iterator();
        }

        it_user = user.Get_Not_Order_Food_List().iterator();
        it_data = Food_data_drink.iterator();
        while(it_data.hasNext()){
            Food f= it_data.next();
            while(it_user.hasNext()){
                Food fd= it_user.next();
                if(f.get_food_name().equals(fd.get_food_name())){
                    f.set_food_order(true);
                }
            }
            it_user = user.Get_Not_Order_Food_List().iterator();
        }
    }
}

