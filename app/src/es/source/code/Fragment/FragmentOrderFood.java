package es.source.code.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.future.scos.R;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.adpter.Food_Not_Order_Adapter;
import es.source.code.adpter.Food_Order_Adapter;
import es.source.code.model.Food;
import es.source.code.model.User;

public class FragmentOrderFood extends Fragment implements Food_Not_Order_Adapter.CallBack,View.OnClickListener{
    private View  view;
    private List<Food> Food_Not_Order_data = null;
    private List<Food> Food_Order_data = null;
    Context mContext;
    Food_Order_Adapter food_order_adapter;
    Food_Not_Order_Adapter food_not_order_adapter;
    private ListView list_food;
    User user;

    TextView food_total_num;
    TextView food_total_price;
    private ProgressBar progressBar ;
    Button settle_accounts;
    private String fragment_type;
    private static CallBack callback;

    public interface CallBack{
        void event(String type);
        void event_cancel(Food food);
    }

    public static void setCallBack(CallBack callBack) {
        callback = callBack;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_food, container, false);
        progressBar = view.findViewById(R.id.progress);
        if (Food_Not_Order_data == null || Food_Order_data == null) {
            Food_Not_Order_data = new LinkedList<>();
            Food_Order_data = new LinkedList<>();
        }
        food_total_num = view.findViewById(R.id.food_total_num);
        food_total_price = view.findViewById(R.id.food_total_price);
        settle_accounts = view.findViewById(R.id.settle_accounts);
        settle_accounts.setOnClickListener(this);

        switch(fragment_type) {
            case "not_ordered":
                if(user.Get_Not_Order_Food_List().size() == 0){
                    settle_accounts.setEnabled(false);
                }else{
                    settle_accounts.setEnabled(true);
                }
                food_not_order_adapter = new Food_Not_Order_Adapter((LinkedList<Food>) Food_Not_Order_data, mContext,this);
                list_food = view.findViewById(R.id.food_list_view);
                list_food.setAdapter(food_not_order_adapter);
                settle_accounts.setText("提交订单");
                food_total_num.setText(String.valueOf(CountNum(user.Get_Not_Order_Food_List()))+"份");
                food_total_price.setText(String.valueOf(CountPrice(user.Get_Not_Order_Food_List()))+"元");
                break;

            case "ordered":
                if(user.Get_Order_Food_List().size() == 0){
                    settle_accounts.setEnabled(false);
                }else{
                    settle_accounts.setEnabled(true);
                }
                food_order_adapter = new Food_Order_Adapter((LinkedList<Food>) Food_Order_data, mContext);
                list_food = view.findViewById(R.id.food_list_view);
                list_food.setAdapter(food_order_adapter);
                settle_accounts.setText("结账");
                food_total_num.setText(String.valueOf(CountNum(user.Get_Order_Food_List()))+"份");
                food_total_price.setText(String.valueOf(CountPrice(user.Get_Order_Food_List()))+"元");
                break;
        }
        //listview监听
        list_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*if(fragment_type.equals("not_ordered")) {
                    Food food = Food_Not_Order_data.get(i);
                    Intent intent = new Intent();
                    intent.setClass(getActivity().getApplicationContext(), FoodDetailed.class);
                    intent.putExtra("String", "FoodOrderView");
                    intent.putExtra("Food", food);
                    intent.putExtra("User", user);
                    intent.putExtra("position", i);
                    intent.putExtra("FoodList", (Serializable) Food_Not_Order_data);
                    startActivity(intent);
                }*/
            }
        });
        return view;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void add_Not_Order_Food( List<Food> Food_data) {
        this.Food_Not_Order_data = Food_data;
    }

    public void add_Order_Food( List<Food> Food_data) {
        this.Food_Order_data = Food_data;
    }

    public void set_fragment_type(String fragment_type){
       this.fragment_type = fragment_type;
    }

    public void set_user(User user){
        this.user = user;
    }

    @Override
    public void onClick(View view) {
        switch (fragment_type) {
            case "not_ordered":
                callback.event(fragment_type);
                break;
            case "ordered":
                new MyAsyncTask().execute();
                break;
        }
    }

    public void Click(View view) {
        Food food = Food_Not_Order_data.get((Integer) view.getTag());
        callback.event_cancel(food);
    }

    //获取总价格数
    private static int CountPrice(List<Food> list) {
        int total_price = 0;
        if (list == null){
            return 0;
        }
        //for-each 遍历
        for (Food food : list) {
            total_price = total_price + food.get_food_price()*food.get_food_num();
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
            total_num = total_num + food.get_food_num();
        }
        return total_num;
    }

    class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(10);
        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected Boolean doInBackground(String... params) {
            //获取传进来的参数
            for(int i=0;i<10;i++) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setProgress(i*10+10);
            }
            return true;
        }
        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        protected void onPostExecute(Boolean bool) {
            //隐藏progressBar
            if(bool) {
                progressBar.setVisibility(View.INVISIBLE);
                int price = CountPrice(user.Get_Order_Food_List());
                if (user.Getter_oldUser() == true) {
                    Toast.makeText(getActivity(), "您好，老顾客，本次你可享受 7 折优惠！价格"+ price*0.7 +"元"+"获得积分："+price+"分", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), "您好，欢迎下次在来！订单价格："+ price +"元"+"获得积分："+price+"分", Toast.LENGTH_SHORT).show();
                }
                callback.event(fragment_type);
            }
        }
    }
}
