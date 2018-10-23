package es.source.code.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.future.scos.R;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.adpter.Food_Adapter;
import es.source.code.model.Food;
import es.source.code.model.User;

public class FragmentFood extends Fragment implements Food_Adapter.CallBack {
    private View view;
    private List<Food> Food_data = null;
    Context mContext;
    Food_Adapter food_adapter;
    private ListView list_food;
    private int position;
    private static CallBack callback;
    User user;

    List<Food> Food_data_cold;
    List<Food> Food_data_hot;
    List<Food> Food_data_sea;
    List<Food> Food_data_drink;

    public interface CallBack{
        void event(Food f);
    }

    public static void setCallBack(CallBack callBack) {
        callback = callBack;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food, container, false);
        if (Food_data == null) {
            Food_data = new LinkedList<>();
        }
        food_adapter = new Food_Adapter((LinkedList<Food>) Food_data, mContext, this);
        list_food = view.findViewById(R.id.food_list_view);
        list_food.setAdapter(food_adapter);
        //listview监听
        list_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = Food_data.get(i);
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), FoodDetailed.class);
                intent.putExtra("String", "FoodView");
                intent.putExtra("int", position);
                intent.putExtra("position", i);
                intent.putExtra("Food", food);
                intent.putExtra("User", user);
                intent.putExtra("FoodList", (Serializable)Food_data);
                intent.putExtra("cold_food", (Serializable)Food_data_cold);
                intent.putExtra("hot_food", (Serializable)Food_data_hot);
                intent.putExtra("sea_food", (Serializable)Food_data_sea);
                intent.putExtra("drink_food", (Serializable)Food_data_drink);

                startActivity(intent);
            }
        });
        return view;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addFood( List<Food> Food_data) {
        this.Food_data = Food_data;
    }

    public void set_Foodlist( List<Food> Food_data_cold,List<Food> Food_data_hot,List<Food> Food_data_sea,List<Food> Food_data_drink) {
        this.Food_data_cold = Food_data_cold;
        this.Food_data_hot = Food_data_hot;
        this.Food_data_sea = Food_data_sea;
        this.Food_data_drink = Food_data_drink;
    }

    public void set_user(User user){
        this.user = user;
    }

    @Override
    public void onClick(View view, boolean cancel) {
        if(cancel == false) {
            Food_data.get((Integer) view.getTag()).set_food_order(true);
            Food food = Food_data.get((Integer) view.getTag());
            food.set_food_order(true);
            callback.event(food);
        }
        else if(cancel == true) {
            Food_data.get((Integer) view.getTag()).set_food_order(false);
            Food food = Food_data.get((Integer) view.getTag());
            food.set_food_order(false);
            callback.event(food);
        }
    }

    public void set_position(int position){
        this.position = position;
    }
}
