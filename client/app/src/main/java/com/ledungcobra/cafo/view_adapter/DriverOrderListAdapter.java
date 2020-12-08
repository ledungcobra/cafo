package com.ledungcobra.cafo.view_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.activity.MapScreen;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;

import java.util.List;

public class DriverOrderListAdapter extends RecyclerView.Adapter<DriverOrderListAdapter.DriverOrderListHolder> {
    Context context;
    List<DetailOrderResponse> orderResponseList;
    OnItemClickListener mListener;

    public DriverOrderListAdapter(Context context, List<DetailOrderResponse> orderResponseList) {
        this.context = context;
        this.orderResponseList = orderResponseList;
    }

    public interface OnItemClickListener {
        //        void onDeleteOrder(int position);
        void onDetailClick(int position);

        void onMapButtonClicked(int pos);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public DriverOrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.order_item_driver, parent, false);

        return new DriverOrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverOrderListHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class DriverOrderListHolder extends RecyclerView.ViewHolder {
        TextView tvOrderID;
        TextView tvResOrder;
        TextView tvResAddress;
        TextView tvCustomerAddress;
        TextView tvPriceOrder;
        TextView tvStatusOrder;
        TextView tvDetailOrder;
        Button btnOpenMap;


        public void bind(final int position) {
            tvResOrder.setText(orderResponseList.get(position).getRestaurant().getName());
            //Toast.makeText(context, orderResponseList.get(position).toString(),Toast.LENGTH_LONG).show();
            tvOrderID.setText(orderResponseList.get(position).getId());
            tvPriceOrder.setText(String.format("%,d", orderResponseList.get(position).getTotal()) + " đ");
            tvResAddress.setText(orderResponseList.get(position).getRestaurant().getAddress());
            tvCustomerAddress.setText(MapScreen.getAddress(itemView.getContext(), Double.parseDouble(orderResponseList.get(position)
                    .getOrderPosition().getLatitude()), Double.parseDouble(orderResponseList.get(position).getOrderPosition().getLongitude())));
            String status = orderResponseList.get(position).getStatus();
            switch (status) {
                case "SHIPPING":
                    tvStatusOrder.setTextColor(context.getColor(android.R.color.holo_blue_dark));
                    break;
                case "CANCELLED":
                    tvStatusOrder.setTextColor(context.getColor(android.R.color.holo_red_dark));
                    break;
                case "DONE":
                    tvStatusOrder.setTextColor(context.getColor(android.R.color.holo_green_dark));
                    break;
                default:
                    tvStatusOrder.setTextColor(context.getColor(android.R.color.holo_orange_dark));
                    break;
            }
            tvStatusOrder.setText(status);

            btnOpenMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMapButtonClicked(position);
                }
            });
        }


        public DriverOrderListHolder(@NonNull View itemView) {
            super(itemView);
            tvResOrder = itemView.findViewById(R.id.tvResOrder);
            tvOrderID = itemView.findViewById(R.id.tvOrderID);
            tvPriceOrder = itemView.findViewById(R.id.tvPriceOrder);
            tvResAddress = itemView.findViewById(R.id.tvResAddressOrder);
            tvCustomerAddress = itemView.findViewById(R.id.tvCustomerAddressOrder);
            tvStatusOrder = itemView.findViewById(R.id.tvStatusOrder);
            tvDetailOrder = itemView.findViewById(R.id.tvDetailOrder);
            btnOpenMap = itemView.findViewById(R.id.btnOpenMap);

            tvDetailOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onDetailClick(getAdapterPosition());
                }
            });

        }
    }
}
