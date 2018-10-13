package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.future.scos.R;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import es.source.code.model.Food;
import es.source.code.model.User;

public class FoodDetailed extends Activity implements View.OnClickListener {
    protected static final float FLIP_DISTANCE = 50;
    private TextView food_name;
    private TextView food_price;
    private TextView food_remark;
    private Button Food_Button_Order;
    private GestureDetector mDetector;
    private Food food;
    private List<Food> Food_data = null;
    private List<Food> food_temp = new LinkedList<>();
    private String str;
    private int position;
    private int f_pos;
    User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);

        food_name = findViewById(R.id.FoodName);
        food_price = findViewById(R.id.FoodPrice);
        food_remark = findViewById(R.id.FoodRemark);
        Food_Button_Order = findViewById(R.id.Food_Button_Order);
        Food_Button_Order.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        str = bundle.getString("String");
        food = (Food)intent.getSerializableExtra("Food");
        f_pos = intent.getIntExtra("int", 0);
        position = intent.getIntExtra("position", 0);
        Food_data  = (List<Food>)intent.getSerializableExtra("FoodList");

        user = (User)getIntent().getSerializableExtra("User");

        food_name.setText(food.get_food_name());
        food_price.setText(food.get_food_price()+"元");
        food_remark.setText(food.get_food_remark());
        if(!food.get_food_order()){
            Food_Button_Order.setText("点菜");
        }

        if(food.get_food_order()){
            Food_Button_Order.setText("退点");
        }

        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向左滑...");
                    if (position >= 0 && position < (Food_data.size()-1)) {
                        position++;
                        food = Food_data.get(position);
                        food_name.setText(food.get_food_name());
                        food_price.setText(food.get_food_price() + "元");
                        food_remark.setText(food.get_food_remark());

                        if (food.get_food_order()) {
                            Food_Button_Order.setText("退点");
                        } else {
                            Food_Button_Order.setText("点菜");
                        }
                    }
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向右滑...");
                    if (position > 0 && position <= (Food_data.size()-1)) {
                        position--;
                        food = Food_data.get(position);
                        food_name.setText(food.get_food_name());
                        food_price.setText(food.get_food_price() + "元");
                        food_remark.setText(food.get_food_remark());

                        if (food.get_food_order()) {
                            Food_Button_Order.setText("退点");
                        } else {
                            Food_Button_Order.setText("点菜");
                        }
                    }
                    return true;
                }
                if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向上滑...");
                    return true;
                }
                if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向下滑...");
                    return true;
                }

                Log.d("TAG", e2.getX() + " " + e2.getY());
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {

                // TODO Auto-generated method stub
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if(food.get_food_order() == true) {
            Food_Button_Order.setText("点菜");
            Food_data.get(position).set_food_order(false);
            food_temp.remove(food);
            user.Delet_Not_Order_Food_List(food);
        }
        else if(food.get_food_order() == false) {
            Food_Button_Order.setText("退点");
            Food_data.get(position).set_food_order(true);
            food_temp.add(food);
            user.Add_Not_Order_Food_List(food_temp);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if(str.equals("FoodView")) {
            Intent intent = new Intent();
            intent.setClass(FoodDetailed.this, FoodView.class);
            intent.putExtra("String", "FoodView");
            intent.putExtra("User", user);
            intent.putExtra("int", f_pos);
            startActivity(intent);
        }
        if(str.equals("FoodOrderView")) {
            Intent intent = new Intent();
            intent.setClass(FoodDetailed.this, FoodOrderView.class);
            intent.putExtra("String", "FromFoodView");
            intent.putExtra("User", user);
            startActivity(intent);
        }
    }
}
