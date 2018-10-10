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

public class FragmentFood extends Fragment implements Food_Adapter.CallBack {
    private View  view;
    private List<Food> Food_data = null;
    Context mContext;
    Food_Adapter food_adapter;
    private ListView list_food;
    private static CallBack callback;

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
                intent.putExtra("Food", food);
                intent.putExtra("FoodList", (Serializable)Food_data);
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

    @Override
    public void onClick(View view) {
        Food food = Food_data.get((Integer) view.getTag());
        callback.event(food);
    }
}
