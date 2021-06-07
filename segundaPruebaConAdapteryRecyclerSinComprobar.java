package com.rrvq.inapp_prueba_cobro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.rrvq.inapp_prueba_cobro.Adapter.AdapterINAPP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

     BillingClient billingClient;

     Button btnComprar;
     RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupBillingClient();

        btnComprar = findViewById(R.id.btnComprar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(billingClient.isReady()){

                    /*List<String> skuList = new ArrayList<> ();
                    skuList.add("inapp_prueba");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);*/

                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("inapp_prueba"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();

                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {

                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                loadProductoARecycler(list);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Query producto", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }else
                {
                    Toast.makeText(MainActivity.this, "Cliente no esta listo", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loadProductoARecycler(List<SkuDetails> list) {

        AdapterINAPP adapter = new AdapterINAPP(this, list, billingClient);
        recyclerView.setAdapter(adapter);

    }


    private void setupBillingClient() {

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Toast.makeText(MainActivity.this, "Esta Lista la Conexion", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, ""+billingResult, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(MainActivity.this, "Esta Desconectado", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {


        Toast.makeText(this, "Compra exitosa"+ list.size(), Toast.LENGTH_LONG).show();

    }
}
