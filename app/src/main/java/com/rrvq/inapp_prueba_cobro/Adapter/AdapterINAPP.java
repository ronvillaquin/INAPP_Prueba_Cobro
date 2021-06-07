package com.rrvq.inapp_prueba_cobro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.rrvq.inapp_prueba_cobro.Interface.ProductoListener;
import com.rrvq.inapp_prueba_cobro.MainActivity;
import com.rrvq.inapp_prueba_cobro.R;

import java.util.List;

public class AdapterINAPP extends RecyclerView.Adapter<AdapterINAPP.MyViewHolder> {

    MainActivity mainActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;


    public AdapterINAPP(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient){

        this.mainActivity = mainActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mainActivity.getBaseContext())
                .inflate(R.layout.productos,parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        holder.tvNombre.setText(skuDetailsList.get(i).getTitle());

        //Click del producto
        holder.setProductoListener(new ProductoListener() {
            @Override
            public void onProductoListener(View view, int position) {

                // ejecutamos la pantalla de cobro de google
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(i))
                        .build();
                billingClient.launchBillingFlow(mainActivity,billingFlowParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNombre;

        ProductoListener productoListener;

        public void setProductoListener(ProductoListener productoListener){
            this.productoListener = productoListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.nombreProducto);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            productoListener.onProductoListener(view, getAdapterPosition());
        }

    }
}
