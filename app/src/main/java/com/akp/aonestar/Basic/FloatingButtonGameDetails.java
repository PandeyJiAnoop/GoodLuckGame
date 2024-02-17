package com.akp.aonestar.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.WalletReport.WalletHistory;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FloatingButtonGameDetails extends RelativeLayout {
    public static final String TAG = HeaderOnClick.class.getSimpleName();
    String UserId, UserName;
    FloatingActionButton fab_add_my_album_listing;
    RelativeLayout main_rl;
    private AlertDialog alertDialog1;
    private AlertDialog alertDialog2;

    public FloatingButtonGameDetails(Context context) {
        super(context);
    }

    public FloatingButtonGameDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingButtonGameDetails(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initFloating(final String id) {
        inflateHeader(id);
    }

    private void inflateHeader(String a_id) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_floating_button_game_details, this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");
        fab_add_my_album_listing=findViewById(R.id.fab_add_my_album_listing);
        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        fab_add_my_album_listing.startAnimation(startAnimation);
        main_rl=findViewById(R.id.mail_rl);
        fab_add_my_album_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a_id.equalsIgnoreCase("GoodLuck")){
                    GameDetails_popup1(a_id); }
                else {
                    GameDetails_popup(a_id); }}});
    }

    private void GameDetails_popup(String getId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_gameoverview, viewGroup, false);
        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "GoodLuck_GameDetailsbyCat", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        view_details_tv.setText(Html.fromHtml(jsonObject1.getString("GameDes")));
                    }
                } catch (JSONException e) {
//                    Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
//                Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
        WindowManager.LayoutParams wmlp = alertDialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.99);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

        alertDialog1.getWindow().setLayout(width, height);
    }

    private void GameDetails_popup1(String getId1) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "MainGameDetailsbyCat", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        view_details_tv.setText(Html.fromHtml(jsonObject1.getString("GameDes")));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
//                Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getId1);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog2 = builder1.create();
        alertDialog2.show();
        WindowManager.LayoutParams wmlp = alertDialog2.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.99);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

        alertDialog2.getWindow().setLayout(width, height);

    }

}



