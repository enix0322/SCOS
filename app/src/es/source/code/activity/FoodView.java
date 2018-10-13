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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.future.scos.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.source.code.Fragment.FragmentFood;
import es.source.code.model.Food;
import es.source.code.model.User;

public class FoodView extends AppCompatActivity implements FragmentFood.CallBack{

    private TabLayout tablayout;
    private ViewPager viewPager;
    private List<String> list;
    List<Food> food = new LinkedList<>();
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

        list = new ArrayList<>();
        list.add(titles[0]);
        list.add(titles[1]);
        list.add(titles[2]);
        list.add(titles[3]);

        FragmentFood.setCallBack(this);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setAdapter(myPagerAdapter);
        tablayout.setupWithViewPager(viewPager,true);
        //每条之间的分割线
        LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
    }

    @Override
    public void event(Food f) {
        if(f.get_food_order() == true) {
            food.add(f);
        }
        else if(f.get_food_order() == false) {
            food.remove(f);
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
            List<Food> Food_data = new LinkedList<>();
            fragmentfood = new FragmentFood();
            if (list.get(position).equals(titles[0])){
                Food_data.add(new Food("凉拌豆腐丝", 10));
                Food_data.add(new Food("凉拌香辣猪头肉", 30));
                Food_data.add(new Food("白灼虾", 40));
                Food_data.add(new Food("凉拌苦菊", 20));
                Food_data.add(new Food("凉拌木耳白菜丝", 10));
                Food_data.add(new Food("盐水鸡", 30));
                Food_data.add(new Food("凉拌海蜇丝", 20));
                Food_data.add(new Food("香煎柠香鳕鱼配蔬果沙拉", 10));
                Food_data.add(new Food("鲜辣椒拌虾皮", 10));
                Food_data.add(new Food("酸辣开胃的拍黄瓜", 10));
                Food_data.add(new Food("凉拌藕片", 10));
                Food_data.add(new Food("凉拌鸭肉", 10));
                Food_data.add(new Food("麻酱拌豇豆角", 10));
                Food_data.add(new Food("凉拌鸡", 20));
                Food_data.add(new Food("醇香麻酱拉皮", 10));
                Food_data.add(new Food("鹌鹑蛋拌黄瓜", 10));
                Food_data.add(new Food("酸辣凤爪", 10));
                Food_data.add(new Food("老醋木耳", 10));
                Food_data.add(new Food("黄瓜洋葱拌筋", 10));
                Food_data.add(new Food("蔬菜沙拉", 10));
                fragmentfood.set_user(user);
                fragmentfood.addFood(Food_data);
            }else if (list.get(position).equals(titles[1])){
                Food_data.add(new Food("老豆腐烧灯椒", 10));
                Food_data.add(new Food("生菜煮鸭蛋", 20));
                Food_data.add(new Food("香煎秋刀鱼", 30));
                Food_data.add(new Food("芹菜粉条", 10));
                Food_data.add(new Food("干煸芸豆", 20));
                Food_data.add(new Food("重庆芋儿鸡", 30));
                Food_data.add(new Food("宫爆鸡丁", 30));
                Food_data.add(new Food("茄汁土豆", 20));
                Food_data.add(new Food("素炒三丝", 30));
                Food_data.add(new Food("腐竹烧排骨", 10));
                Food_data.add(new Food("猪肉片炒花菜", 30));
                Food_data.add(new Food("榨菜丝炒油豆角", 30));
                Food_data.add(new Food("红烧茄子", 10));
                Food_data.add(new Food("香菇童子鸡", 20));
                Food_data.add(new Food("尖椒炒火腿肠", 30));
                Food_data.add(new Food("蚝油红薯梗儿", 10));
                Food_data.add(new Food("上汤西兰花", 20));
                Food_data.add(new Food("桂花糯米藕", 30));
                Food_data.add(new Food("小炒牛肉", 10));
                Food_data.add(new Food("山药炒木耳", 20));
                Food_data.add(new Food("蒜蓉快菜", 30));
                Food_data.add(new Food("荷兰豆炒腊肉", 30));
                Food_data.add(new Food("海蟹酱炒鸡蛋", 30));
                Food_data.add(new Food("美味干锅排骨", 40));
                Food_data.add(new Food("腊鸭腿青菜煮年糕", 20));
                Food_data.add(new Food("红烧冻豆腐", 20));
                Food_data.add(new Food("蒜蓉粉丝蒸虾", 30));
                Food_data.add(new Food("大葱炒肉", 30));
                Food_data.add(new Food("美味干锅排骨", 30));
                Food_data.add(new Food("红烧冻豆腐", 20));
                fragmentfood.set_user(user);
                fragmentfood.addFood(Food_data);
            }else if (list.get(position).equals(titles[2])){
                Food_data.add(new Food("盐焗蛏", 30));
                Food_data.add(new Food("原味皮皮虾", 50));
                Food_data.add(new Food("烤箱版蒸梭子蟹", 60));
                Food_data.add(new Food("吮指麻辣蟹", 50));
                Food_data.add(new Food("烤花甲", 40));
                Food_data.add(new Food("花甲粉丝", 30));
                Food_data.add(new Food("香辣花甲", 40));
                Food_data.add(new Food("炒海瓜子", 30));
                Food_data.add(new Food("香辣螃蟹", 50));
                Food_data.add(new Food("花甲炖蛋", 40));
                Food_data.add(new Food("辣炒花蛤蜊", 30));
                Food_data.add(new Food("黄瓜虾仁", 40));
                Food_data.add(new Food("浇汁鲍鱼", 60));
                Food_data.add(new Food("虾仁蒸豆腐", 30));
                Food_data.add(new Food("金汤虾球烩豆腐", 40));
                Food_data.add(new Food("煎虾", 30));
                Food_data.add(new Food("清蒸大闸蟹", 70));
                Food_data.add(new Food("清蒸大头虾", 30));
                Food_data.add(new Food("蒜蓉酱蒸珍宝蚝", 70));
                Food_data.add(new Food("炒鱿鱼", 40));
                Food_data.add(new Food("浇汁鲍鱼", 80));
                Food_data.add(new Food("虾仁土豆条", 30));
                Food_data.add(new Food("肉螺炒鸡蛋", 30));
                Food_data.add(new Food("花蛤炒虾蛄", 30));
                Food_data.add(new Food("蒸螃蟹", 30));
                Food_data.add(new Food("葱油青口贝", 50));
                Food_data.add(new Food("黄金蟹斗", 30));
                Food_data.add(new Food("蒜味虾", 30));
                Food_data.add(new Food("清炒黄蚬", 30));
                Food_data.add(new Food("红烧籽虾", 30));
                fragmentfood.set_user(user);
                fragmentfood.addFood(Food_data);
            }else if (list.get(position).equals(titles[3])){
                Food_data.add(new Food("可乐", 3));
                Food_data.add(new Food("雪碧", 3));
                Food_data.add(new Food("芬达", 3));
                Food_data.add(new Food("火龙果汁", 15));
                Food_data.add(new Food("南果梨汁", 10));
                Food_data.add(new Food("金骏眉茶", 10));
                Food_data.add(new Food("浓香豆浆", 10));
                Food_data.add(new Food("糖桂花柠檬水", 10));
                Food_data.add(new Food("猕猴桃酸奶杯", 10));
                Food_data.add(new Food("树莓蜜", 10));
                Food_data.add(new Food("红豆西米奶茶", 10));
                Food_data.add(new Food("双豆豆浆", 10));
                Food_data.add(new Food("椰奶拿铁咖啡", 10));
                Food_data.add(new Food("鲜榨石榴汁", 10));
                Food_data.add(new Food("西柚柠檬苏打水", 10));
                Food_data.add(new Food("毛樱桃汁", 10));
                Food_data.add(new Food("棉花糖巧克力热饮", 10));
                Food_data.add(new Food("红豆莲藕糊", 10));
                fragmentfood.set_user(user);
                fragmentfood.addFood(Food_data);
            }
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
                if(food != null){
                    user.Add_Not_Order_Food_List(food);
                }
                intent.setClass(FoodView.this, FoodOrderView.class);
                intent.putExtra("String", "FromFoodView");
                intent.putExtra("User", user);
                intent.putExtra("tab", 0);
                startActivity(intent);
                break;
            case R.id.order_list:
                if(food != null){
                    user.Add_Not_Order_Food_List(food);
                }
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
        if(food != null){
            user.Add_Not_Order_Food_List(food);
        }
        Intent intent = new Intent();
        intent.setClass(FoodView.this, MainScreen.class);
        intent.putExtra("String", "FromFoodView");
        intent.putExtra("User", user);
        startActivity(intent);
    }
}

