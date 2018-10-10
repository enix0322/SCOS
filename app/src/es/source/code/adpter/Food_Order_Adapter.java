package es.source.code.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.future.scos.R;

import java.util.LinkedList;

import es.source.code.model.Food;

public class Food_Order_Adapter extends BaseAdapter{
    private ViewHolder mViewHolder;
    private LinkedList<Food> mData;
    private Context mContext;

    public Food_Order_Adapter(LinkedList<Food> mData, Context mContext) {
        this.mData = mData;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_food,null);
            mViewHolder = new ViewHolder(convertView);
            mViewHolder.food_name = convertView.findViewById(R.id.food_name);
            mViewHolder.food_price = convertView.findViewById(R.id.food_price);
            mViewHolder.food_num = convertView.findViewById(R.id.food_num);
            mViewHolder.food_remark = convertView.findViewById(R.id.food_remark);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.food_name.setText(food_data.get_food_name());
        mViewHolder.food_price.setText(food_data.get_food_price()+"元");
        mViewHolder.food_num.setText(food_data.get_food_num()+"份");
        mViewHolder.food_remark.setText(food_data.get_food_remark());
        return convertView;
    }
    public class ViewHolder {
        TextView food_name;
        TextView food_price;
        TextView food_num;
        TextView food_remark;
        public ViewHolder(View convertView)
        {
            food_name = convertView.findViewById(R.id.food_name);
            food_price = convertView.findViewById(R.id.food_price);
            food_num = convertView.findViewById(R.id.food_order);
            food_remark = convertView.findViewById(R.id.food_remark);
        }
    }
}
