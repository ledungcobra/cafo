package com.ledungcobra.cafo.view_adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListHolder> {
    Context context;
    List<DetailOrderResponse> orderResponseList;
    OnItemClickListener mListener;

    public OrderListAdapter(Context context, List<DetailOrderResponse> orderResponseList) {
        this.context = context;
        this.orderResponseList = orderResponseList;
    }

    public List<DetailOrderResponse> getOrderResponseList() {
        return orderResponseList;
    }

    public void setOrderResponseList(List<DetailOrderResponse> orderResponseList) {
        this.orderResponseList = orderResponseList;
    }

    public interface OnItemClickListener{
        void onDeleteOrder(int position);
        void onDetailClick(int position);
    }

    public void deleteItem(int index){
//        orderResponseList.remove(index);
        orderResponseList.get(index).setStatus("CANCEL");
        notifyItemChanged(index);
    }

    public void setOnClickListener(OnItemClickListener listener) { mListener = listener;}

    @NonNull
    @Override
    public OrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.order_item, parent,false);

        return new OrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListHolder holder, int position) {
            holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class OrderListHolder extends RecyclerView.ViewHolder{
        TextView tvResOrder;
        TextView tvOrderID;
        TextView tvPriceOrder;
        TextView tvTimeOrder;
        TextView tvStatusOrder;
        TextView tvDetailOrder;
        ImageView ivDeleteOrder;

        public void bind(int position){
            //Respond to user don't have restaurant name
            //tvResOrder.setText(orderResponseList.get(position).getRestaurant().getName());
            tvOrderID.setText(orderResponseList.get(position).getId());
            tvPriceOrder.setText(String.format("%,d",orderResponseList.get(position).getTotal())+" đ");
            String[] datetimeOrdered = orderResponseList.get(position).getOrderTime().split("T");
            tvTimeOrder.setText(datetimeOrdered[0]+": "+ datetimeOrdered[1].substring(0,datetimeOrdered[1].length()-1)) ;

            String status = orderResponseList.get(position).getStatus();

            //Can only cancel orders which are not handled yet
            if (!status.equals("WAITING")) {
                ivDeleteOrder.setEnabled(false);
                ivDeleteOrder.getDrawable().setTint(Color.GRAY);
            }
            tvStatusOrder.setText(status);
        }


        public OrderListHolder(@NonNull View itemView) {
            super(itemView);
            tvResOrder = itemView.findViewById(R.id.tvResOrder);
            tvOrderID =  itemView.findViewById(R.id.tvOrderID);
            tvPriceOrder =  itemView.findViewById(R.id.tvPriceOrder);
            tvTimeOrder =  itemView.findViewById(R.id.tvTimeOrder);
            tvStatusOrder =  itemView.findViewById(R.id.tvStatusOrder);
            tvDetailOrder =  itemView.findViewById(R.id.tvDetailOrder);
            ivDeleteOrder =  itemView.findViewById(R.id.ivDeleteOrder);

            tvDetailOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onDetailClick(getAdapterPosition());
                }
            });

            ivDeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onDeleteOrder(getAdapterPosition());
                }
            });


        }
    }
}
