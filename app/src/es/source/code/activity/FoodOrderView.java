package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.future.scos.R;

import java.util.ArrayList;
import java.util.List;

import es.source.code.Fragment.FragmentOrderFood;
import es.source.code.model.Food;
import es.source.code.model.User;

public class FoodOrderView extends AppCompatActivity implements FragmentOrderFood.CallBack{

    private static FoodOrderView mInstance;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private List<String> list;
    FragmentOrderFood fragmentorderfood;
    User user;
    //数据源
    private String[] titles = {"未下单菜","已下单菜"};
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_view);
        tablayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        user = (User)getIntent().getSerializableExtra("User");
        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);

        FragmentOrderFood.setCallBack(this);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setAdapter(myPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        //每条之间的分割线
        LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
    }

    @Override
    public void event(String type) {
        switch (type){
            case "not_ordered":
                user.Add_Order_Food_List(user.Get_Not_Order_Food_List());
                user.Clear_Not_Order_Food_List();
                break;
            case "ordered":
                user.Clear_Order_Food_List();
                break;
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
            fragmentorderfood = new FragmentOrderFood();
            if (list.get(position).equals(titles[0])){
                fragmentorderfood.set_fragment_type("not_ordered");
            }else if (list.get(position).equals(titles[1])){
                fragmentorderfood.set_fragment_type("ordered");
                fragmentorderfood.set_user(user);
            }
            fragmentorderfood.add_Not_Order_Food(user.Get_Not_Order_Food_List());
            fragmentorderfood.add_Order_Food(user.Get_Order_Food_List());
            fragmentorderfood.set_total_not_order_price(CountPrice(user.Get_Not_Order_Food_List()));
            fragmentorderfood.set_total_not_order_num(CountNum(user.Get_Not_Order_Food_List()));
            fragmentorderfood.set_total_order_price(CountPrice(user.Get_Order_Food_List()));
            fragmentorderfood.set_total_order_num(CountNum(user.Get_Order_Food_List()));
            return fragmentorderfood;
        }
        @Override
        public int getCount() {
            return titles.length;
        }

    }

    //获取总价格数
    private static int CountPrice(List<Food> list) {
        int total_price = 0;
        if (list == null){
            return 0;
        }
        //for-each 遍历
        for (Food food : list) {
            total_price = total_price + Integer.parseInt(food.get_food_price());
        }
        return total_price;
    }

    //获取总价格数
    private static int CountNum(List<Food> list) {
        int total_num = 0;
        if (list == null){
            return 0;
        }
        if (list.size() == 0){
            return 0;
        }
        //for-each 遍历
        for (Food food : list) {
                total_num = total_num + Integer.parseInt(food.get_food_num());
        }
        return total_num;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(FoodOrderView.this, MainScreen.class);
        intent.putExtra("String", "FromFoodOrderView");
        intent.putExtra("User", user);
        startActivity(intent);
    }
}
