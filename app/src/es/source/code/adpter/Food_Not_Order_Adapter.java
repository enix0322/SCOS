package es.source.code.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.future.scos.R;

import java.util.LinkedList;

import es.source.code.model.Food;

public class Food_Not_Order_Adapter extends BaseAdapter implements View.OnClickListener{
    private ViewHolder mViewHolder;
    private LinkedList<Food> mData;
    private Context mContext;
    CallBack mCallBack;

    public Food_Not_Order_Adapter(LinkedList<Food> mData, Context mContext, CallBack callBack) {
        this.mData = mData;
        this.mCallBack=callBack;
    }

    public interface CallBack{
        void onClick(View v);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Food food_data = mData.get(position);
        if(mContext == null)
            mContext = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_not_order_food,null);
            mViewHolder = new ViewHolder(convertView);
            mViewHolder.food_name = convertView.findViewById(R.id.food_name);
            mViewHolder.food_price = convertView.findViewById(R.id.food_price);
            mViewHolder.food_num = convertView.findViewById(R.id.food_num);
            mViewHolder.food_remark = convertView.findViewById(R.id.food_remark);
            mViewHolder.food_cancel = convertView.findViewById(R.id.food_cancel);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.food_name.setText(food_data.get_food_name());
        mViewHolder.food_price.setText(food_data.get_food_price()+"元");
        mViewHolder.food_num.setText(food_data.get_food_num()+"份");
        mViewHolder.food_remark.setText(food_data.get_food_remark());
        mViewHolder.food_cancel.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(mContext,"退点成功",Toast.LENGTH_SHORT).show();
        mCallBack.onClick(view);
    }

    public class ViewHolder {
        TextView food_name;
        TextView food_price;
        TextView food_num;
        TextView food_remark;
        Button food_cancel;
        public ViewHolder(View convertView)
        {
            food_name = convertView.findViewById(R.id.food_name);
            food_price = convertView.findViewById(R.id.food_price);
            food_num = convertView.findViewById(R.id.food_order);
            food_remark = convertView.findViewById(R.id.food_remark);
            food_cancel = convertView.findViewById(R.id.food_cancel);
        }
    }
}
