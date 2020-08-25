package com.example.etinvetory.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.etinvetory.GetItemsFragment;
import com.example.etinvetory.R;

import java.util.ArrayList;
import java.util.List;


public class ProductListAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "ProductListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView id;
        TextView description;
        TextView quantity;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ProductListAdapter(Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the products information
        int id = getItem(position).getId();
        String description = getItem(position).getDescription();
        int quantity = getItem(position).getQuantity();


        //Create the product object with the information
        Product product = new Product(description,quantity, id);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.tvId);
            holder.description = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.quantity = (TextView) convertView.findViewById(R.id.tvQuantity);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.id.setText(String.valueOf(product.getId()));
        holder.description.setText(product.getDescription());
        holder.quantity.setText(String.valueOf(product.getQuantity()));


        return convertView;
    }
}

























