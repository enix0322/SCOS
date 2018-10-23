package es.source.code.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import es.source.code.model.User;

public class FoodView extends AppCompatActivity implements FragmentFood.CallBack{

    private TabLayout tablayout;
    private ViewPager viewPager;
    private List<String> list;
    private int f_pos = 0;
    List<Food> Food_data_cold;
    List<Food> Food_data_hot;
    List<Food> Food_data_sea;
    List<Food> Food_data_drink;
    FragmentFood fragmentfood;
    User user;

    //数据源
    private String[] titles = {"冷菜", "热菜", "海鲜", "酒水"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        user = (User)getIntent().getSerializableExtra("User");
        f_pos = getIntent().getIntExtra("int", 0);

        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        list.add(titles[2]);
        list.add(titles[3]);

        set_data();
        FragmentFood.setCallBack(this);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            fragmentfood = new FragmentFood();
            if (list.get(position).equals(titles[0])){
                fragmentfood.addFood(Food_data_cold);
            }else if (list.get(position).equals(titles[1])){
                fragmentfood.addFood(Food_data_hot);
            }else if (list.get(position).equals(titles[2])){
                fragmentfood.addFood(Food_data_sea);
            }else if (list.get(position).equals(titles[3])){
                fragmentfood.addFood(Food_data_drink);
            }
            fragmentfood.set_user(user);
            fragmentfood.set_position(position);
            fragmentfood.set_Foodlist(Food_data_cold, Food_data_hot, Food_data_sea, Food_data_drink);
            return fragmentfood;
        }
        @Override
        public int getCount() {
            return titles.length;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_menu, menu);
        menu.findItem(R.id.ordered);
        menu.findItem(R.id.order_list);
        menu.findItem(R.id.call_server);
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
        Food_data_cold  = new LinkedList((ArrayList<Food>)getIntent().getSerializableExtra("cold_food"));

        Food_data_hot = new LinkedList((ArrayList<Food>)getIntent().getSerializableExtra("hot_food"));

        Food_data_sea = new LinkedList((ArrayList<Food>)getIntent().getSerializableExtra("sea_food"));

        Food_data_drink = new LinkedList((ArrayList<Food>)getIntent().getSerializableExtra("drink_food"));

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

