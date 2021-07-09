package com.azra2.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

    private static Utils    instance;
    private Context mContext;

    private static float    reviewMark;

    private static float    userReview;
    private static String   userComment;
    public static boolean   isProductSearch;

    private static int      pageIndex;

    public static Utils getInstance(Context context) {
        if (instance == null) {
            instance = new Utils(context);
        }

        instance.setContext(context);

        return instance;
    }

    private Utils(Context context) {
        mContext = context;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.isEmpty())
            return true;

        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    public static boolean isCollection(Object obj) {
        return obj.getClass().isArray() || obj instanceof Collection;
    }

    public static ProgressDialog openNewDialog(Context context, String msg, boolean cancelable, boolean outsideTouchCancelable) {
        ProgressDialog dialog = null;
        try {
            dialog = new ProgressDialog(context);

            dialog.setMessage(msg);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(outsideTouchCancelable);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }

    public boolean isOnline() {
        ConnectivityManager conManager  = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo         = conManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void setAverageReview(float mark) {
        reviewMark = mark;
    }
    public static float getAverageReview() {
        return reviewMark;
    }

    public static void setUserReview(float mark) {
        userReview = mark;
    }
    public static float getUserReview() {
        return userReview;
    }

    public static void setUserComment(String comment) {
        userComment = comment;
    }
    public static String getUserComment() {
        return userComment;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setPage(int index) {
        pageIndex = index;
    }

    public static int getPage() {
        return pageIndex;
    }
}
