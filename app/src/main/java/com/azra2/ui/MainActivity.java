package com.azra2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.azra2.R;
import com.azra2.ui.room.BaseCallActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtm.ErrorInfo;

public class MainActivity extends BaseCallActivity {
    final static String TAG = MainActivity.class.getSimpleName();
    NavController navController;

    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRtm();
        doRtmLogin();

        setStatusBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because eachnav_view
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_choose_room, R.id.nav_store, R.id.nav_notification, R.id.nav_profile)
//                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//                if(destination.getId() == R.id.full_screen_destination) {
//                    toolbar.setVisibility(View.GONE);
//                    bottomNav.setVisibility(View.GONE);
//                } else {
//                    toolbar.setVisibility(View.VISIBLE);
//                    bottomNav.setVisibility(View.VISIBLE);
//                }
        });

        //Initialize Ads
        MobileAds.initialize(this);

        // Use an activity context to get the rewarded video instance. second argumet is app unit id
        rewardedAd = new RewardedAd(this,"ca-app-pub-9777078163963067/3255458389");
    }

    @Override
    public void onSuccess(Void aVoid) {
        Log.d(TAG, "Succeeded to login to Agora.");
    }

    @Override
    public void onFailure(ErrorInfo errorInfo) {
        Log.e(TAG, "Failed to login to Agora: \n" + errorInfo.toString());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        doRtmLogout();//???????
    }

    //for support navigation up button
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void setStatusBar(){
        ///set status bar color
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.grey_500));
    }



    /**
    private void setUpBillingClient(){
        billingClient = BillingClient.newBuilder(MainActivity.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
    }

    private void startConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    Log.d("TAG_INAPP:", "Setup Billing Done");
                    loadProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d("TAG_INAPP:", "Billing Client Disconnected");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void loadProducts(){
        skuList.add("1 coin");
        skuList.add("2 coins");
        skuList.add("3 coins");
        skuList.add("5 coins");
        skuList.add("10 coins");
        skuList.add("20 coins");
        skuList.add("30 coins");
        skuList.add("50 coins");
        skuList.add("100 coins");

        if(billingClient.isReady()){
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                    // Process the result.
                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !skuDetailsList.isEmpty()){
                        MainActivity.this.skuDetailsList = skuDetailsList;
                        for(int i = 0;i < skuDetailsList.size(); i++){
                            if(skuDetailsList.get(i).getSku() == ""){
                                SkuDetails skuDetails = skuDetailsList.get(i);
                                launchPurchageFlow(skuDetails);
                            }
                        }

                    }
                }
            });
        }
        else{
            Log.d("BillingClietn:", "Not ready");
        }

    }

    private void launchPurchageFlow(SkuDetails skuDetails){
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        int responseCode = billingClient.launchBillingFlow(MainActivity.this, billingFlowParams).getResponseCode();

        // Handle the result
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.
//        Purchase purchase = ...;

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }

    private BillingClient billingClient;

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }
    };

    private List<String> skuList = new ArrayList<>();

    private List<SkuDetails> skuDetailsList;
    **/

}
