package com.ledungcobra.cafo.view_adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.customer.CustomerOrder;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.ViewHolder> {
    List<CustomerOrder> customerOrders;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_item, parent, false);
        return new ViewHolder(view);
    }

    public CustomerOrdersAdapter(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    @Override
    public int getItemCount() {
        return customerOrders.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CustomerOrder customerOrder = customerOrders.get(position);
        Log.d("CUSTOMER", "onBindViewHolder: "+customerOrder);
        holder.totalPrice.setText(customerOrder.getPrice());
        holder.foodName.setText(customerOrder.getFoodName());
        Picasso.get().load(customerOrder.getImageUrl()).transform(new CropCircleTransformation()).into(holder.foodImage);
        holder.quantity.setText(customerOrder.getQuantity());

    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView foodName;
        TextView totalPrice;
        TextView quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.ivFoodImage);
            foodName = itemView.findViewById(R.id.tvFoodName_Order);
            totalPrice = itemView.findViewById(R.id.tvTotalPrice) ;
            quantity = itemView.findViewById(R.id.tvQuantity);

        }
    }
}
