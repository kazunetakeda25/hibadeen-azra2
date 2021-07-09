package com.azra2.ui.store;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMCoinPack;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.room.ReviewFragmentDirections;
import com.azra2.ui.tutorial.TutorialActivity;
import com.azra2.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyCoinsFragment extends Fragment implements CoinPackViewHolder.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    public View rootView;
    private RecyclerView rvCoinPackList;
    CoinPackAdapter coinPackAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<YMCoinPack> coinPacks = new ArrayList<>();

    //For iap
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
    private List<String> mSkusList = new ArrayList<>();
    private List<SkuDetails> mSkuDetailsList = new ArrayList<>();


    public BuyCoinsFragment() {
        initCoinPacks();
        //setUpBillingClient();
        //startConnection();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_buy_coins, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCoinPackList = view.findViewById(R.id.rvCoinPackList);
        coinPackAdapter = new CoinPackAdapter(getActivity(), BuyCoinsFragment.this, coinPacks);
        rvCoinPackList.setHasFixedSize(true);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvCoinPackList.setLayoutManager(layoutManager);
        rvCoinPackList.setAdapter(coinPackAdapter);

//        if(coinPackAdapter.selectedHolder != null){
//            coinPackAdapter.selectedHolder.cvPrice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NavDirections action = BuyCoinsFragmentDirections.actionBuyCoinsFragmentToSelectPaymentFragment();
//                    NavController controller = Navigation.findNavController(view);
//                    controller.navigate(action);
//                }
//            });
//        }


//        mSwipeRefreshLayout = view.findViewById(R.id.slAgent);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

//        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClicked(View view, int position) {
        for(int i=0; i<coinPackAdapter.getItemCount();i++){
            if(coinPacks.get(i).selected == true){
                coinPacks.get(i).selected = false;
                coinPackAdapter.notifyItemChanged(i);
            }
        }
        coinPacks.get(position).selected = true;
        coinPackAdapter.notifyItemChanged(position);
    }

    @Override
    public void onRefresh(){

    }

    private void initCoinPacks(){
        /*if(mSkuDetailsList.size() > 0){
            coinPacks.clear();
            for( SkuDetails skuDetails: mSkuDetailsList){
                String sku = skuDetails.getSku();
                String[] strs = sku.split("_");
                int count = Integer.parseInt(strs[1]);
                double price = Double.parseDouble(skuDetails.getPrice());
                coinPacks.add(new YMCoinPack(count, price));
            }
            updateUI();
        }*/

        coinPacks.add(new YMCoinPack(1, 0.99));
        coinPacks.add(new YMCoinPack(2, 1.99));
        coinPacks.add(new YMCoinPack(3, 2.99));
        coinPacks.add(new YMCoinPack(5, 4.99));
        coinPacks.add(new YMCoinPack(10, 9.99));
        coinPacks.add(new YMCoinPack(20, 18.99));
        coinPacks.add(new YMCoinPack(30, 27.99));
        coinPacks.add(new YMCoinPack(50, 35.99));
        coinPacks.add(new YMCoinPack(100, 59.99));
    }

    private void updateUI(){
        coinPackAdapter.notifyDataSetChanged();
    }

    /*********************************************************************************************/
    /**
     *
     * */
    /*********************************************************************************************/
    private void setUpBillingClient(){
        billingClient = BillingClient.newBuilder(getActivity())
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadProducts();

                        }
                    });

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
        mSkusList.add(YMConst.GET_1_COIN);
        mSkusList.add(YMConst.GET_2_COINS);
        mSkusList.add(YMConst.GET_3_COINS);
        mSkusList.add(YMConst.GET_5_COINS);
        mSkusList.add(YMConst.GET_10_COINS);
        mSkusList.add(YMConst.GET_20_COINS);
        mSkusList.add(YMConst.GET_30_COINS);
        mSkusList.add(YMConst.GET_50_COINS);
        mSkusList.add(YMConst.GET_100_COINS);

        if(billingClient.isReady()){
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(mSkusList).setType(BillingClient.SkuType.INAPP);

            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !skuDetailsList.isEmpty()){
                                mSkuDetailsList.addAll(skuDetailsList) ;
                            }
                        }
                    });
        }
        else{
            Log.d("BillingClietn:", "Not ready");
        }

    }

    public void launchPurchageFlow(YMCoinPack coinPack){
        SkuDetails skuDetails = null;
        int count = coinPack.getCount();
        double price = coinPack.getPrice();
        for(int i=0; i< mSkuDetailsList.size(); i++){
            if(Double.parseDouble(mSkuDetailsList.get(i).getPrice()) == price){
                skuDetails = mSkuDetailsList.get(i);
            }
        }
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        if(skuDetails != null){
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();
            int responseCode = billingClient.launchBillingFlow(getActivity(), billingFlowParams).getResponseCode();
            if(responseCode != BillingClient.BillingResponseCode.OK){
                Toast.makeText(getActivity(), getContext().getResources().getString(R.string.something_wrong),
                        Toast.LENGTH_SHORT).show();
            }
        }

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
                    for(String sku: mSkusList){
                        if(sku == purchase.getSku()){
                            String[] strs = sku.split("_");
                            int coinCount = Integer.parseInt(strs[1]);
                            double price = 0.0;
                            purchaseCoins(coinCount, price);  //notify server
                        }
                    }

                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }

    void purchaseCoins(int coinCount, double price){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.buyCoin(AGApplication.the().getMe().getId(), coinCount, price,
                AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();
                YMUserResponse response1 = response.body();
                if(response1 != null){
                    if(response1.getResult().equalsIgnoreCase("false")){
                        Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);
                            showPurchasedDlg(coinCount);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPurchasedDlg(int coinCount){
        final Dialog dlgCoinPurchased = new Dialog(getContext());
        dlgCoinPurchased.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgCoinPurchased.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgCoinPurchased.setCancelable(false);
        dlgCoinPurchased.setContentView(R.layout.dialog_coins_purchased);
        Window window = dlgCoinPurchased.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView closeBtn = dlgCoinPurchased.findViewById(R.id.ivClose);
        TextView tvTitle = dlgCoinPurchased.findViewById(R.id.tvCoinPurchagedTitle);
        TextView tvContent = dlgCoinPurchased.findViewById(R.id.tvCoinPurchagedContent);
        TextView tvCheck = dlgCoinPurchased.findViewById(R.id.tvCheckCoinBalance);

        tvTitle.setText(String.format(getResources().getString(R.string.coin_purchased),coinCount));
        tvContent.setText(String.format(getResources().getString(R.string.you_purchased_), coinCount));

        closeBtn.setOnClickListener(v -> {
            dlgCoinPurchased.dismiss();
        });

        tvCheck.setOnClickListener(v -> {
            dlgCoinPurchased.dismiss();
            NavController navController = Navigation.findNavController(rootView);
            NavDirections directions = BuyCoinsFragmentDirections.actionBuyCoinsFragmentToNavProfile();
            navController.navigate(directions);
        });

        dlgCoinPurchased.show();
    }


}