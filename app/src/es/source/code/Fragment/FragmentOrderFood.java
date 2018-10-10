package es.source.code.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
    private int total_notorder_price;
    private int total_notorder_num;
    private int total_order_price;
    private int total_order_num;
    User user;

    TextView food_total_num;
    TextView food_total_price;
    Button settle_accounts;
    private String fragment_type;
    private static CallBack callback;

    public interface CallBack{
        void event(String type);
    }

    public static void setCallBack(CallBack callBack) {
        callback = callBack;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_food, container, false);
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
                food_not_order_adapter = new Food_Not_Order_Adapter((LinkedList<Food>) Food_Not_Order_data, mContext,this);
                list_food = view.findViewById(R.id.food_list_view);
                list_food.setAdapter(food_not_order_adapter);
                settle_accounts.setText("提交订单");
                food_total_num.setText(String.valueOf(total_notorder_num)+"份");
                food_total_price.setText(String.valueOf(total_notorder_price)+"元");
                break;

            case "ordered":
                food_order_adapter = new Food_Order_Adapter((LinkedList<Food>) Food_Order_data, mContext);
                list_food = view.findViewById(R.id.food_list_view);
                list_food.setAdapter(food_order_adapter);
                settle_accounts.setText("结账");
                food_total_num.setText(String.valueOf(total_order_num)+"份");
                food_total_price.setText(String.valueOf(total_order_price)+"元");
                break;
        }
        //listview监听
        list_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = Food_Not_Order_data.get(i);
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), FoodDetailed.class);
                intent.putExtra("Food", food);
                intent.putExtra("position",i);
                intent.putExtra("FoodList", (Serializable)Food_Not_Order_data);
                startActivity(intent);
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

    public void set_total_not_order_price(int total_price){
        this.total_notorder_price = total_price;
    }

    public void set_total_not_order_num(int total_num){
        this.total_notorder_num = total_num;
    }

    public void set_total_order_price(int total_price){
        this.total_order_price = total_price;
    }

    public void set_total_order_num(int total_num){
        this.total_order_num = total_num;
    }

    public void set_user(User user){
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        switch (fragment_type){
            case "not_ordered":
                callback.event(fragment_type);
                break;
            case "ordered":
                callback.event(fragment_type);
                if(user.Getter_oldUser() == true) {
                    Toast.makeText(getActivity(),"您好，老顾客，本次你可享受 7 折优惠",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
