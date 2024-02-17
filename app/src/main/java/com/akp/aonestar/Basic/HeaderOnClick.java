package com.akp.aonestar.Basic;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.GoodLuckModule.SeasonalSaleProduct;
import com.akp.aonestar.Home.DashBoard;
import com.akp.aonestar.MenuWebLink.Menu_AboutUs;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

public class HeaderOnClick extends RelativeLayout {

    public static final String TAG = HeaderOnClick.class.getSimpleName();
    private LinearLayout profile_ll, video_ll, wallet_ll, winner_ll, plan_ll;
    private TextView name_tv, wallet_tv;
    String UserId, UserName;
    private AlertDialog alertDialog1,alertDialog2;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RecyclerView plan_rec;
    private Dialog alertDialogs;
    private String AutoModeStatus;
    TextView statusTV;
    SwitchCompat switchView;

    public HeaderOnClick(Context context) {
        super(context);
    }

    public HeaderOnClick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderOnClick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initHeader() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header, this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");

        profile_ll = (LinearLayout) findViewById(R.id.profile_ll);
        name_tv = (TextView) findViewById(R.id.name_tv);
        video_ll = (LinearLayout) findViewById(R.id.video_ll);
        winner_ll = (LinearLayout) findViewById(R.id.winner_ll);
        wallet_ll = (LinearLayout) findViewById(R.id.wallet_ll);
        plan_ll = (LinearLayout) findViewById(R.id.plan_ll);
        wallet_tv = (TextView) findViewById(R.id.wallet_tv);

        if (UserId.equalsIgnoreCase("")) {
            name_tv.setText("Login");
        } else {
            name_tv.setText(UserName);
            getWalletAPI();
        }


        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")) {
                    name_tv.setText("Login");
                    Intent intent = new Intent(getContext(), LoginScreenAkp.class);
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), SettingScreen.class);
                    getContext().startActivity(intent);
                }
            }
        });


        wallet_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WalletHistory.class);
                getContext().startActivity(intent);

            }
        });
        winner_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Header_BusinessExcelList.class);
                getContext().startActivity(intent);
//                Intent intent = new Intent(getContext(), BChartFolder.class);
//                getContext().startActivity(intent);

            }
        });
        video_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HeaderVideoScreen.class);
                getContext().startActivity(intent);

            }
        });

        plan_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan_popup();

            }
        });


    }

    public void getWalletAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "walletResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("Status")) {
                        JSONArray Response = jsonObject.getJSONArray("Response");
                        JSONObject jsonObject2 = Response.getJSONObject(0);
                        wallet_tv.setText("₹"+String.valueOf(jsonObject2.getDouble("MainWallet")));
                        /*for (int i = 0; i < Response.length(); i++) {


                            //String mainWallet = String.valueOf(jsonObject2.getDouble("MainWallet"));
                            //name_tv.setText(String.valueOf(jsonObject2.getDouble("EMIWallet")));
                            //Toast.makeText(getContext(), mainWallet, Toast.LENGTH_SHORT).show();

                        }*/
                    }
                    else {
                        Toast.makeText(getContext(), jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void Plan_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_plan, viewGroup, false);
        plan_rec = dialogView.findViewById(R.id.plan_rec);
        // on below line we are initializing our variables.
         switchView = dialogView.findViewById(R.id.idSwitch);
        statusTV = dialogView.findViewById(R.id.idTVStatus);

        GetProfile();
        // on below line we are checking
        // the status of switch
        if (switchView.isChecked()) {
            // on below line we are setting text
            // if switch is checked.
            statusTV.setText("Auto Mode ON");
        } else {
            // on below line we are setting the text
            // if switch is un checked
            statusTV.setText("Auto Mode OFF");
        }
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!UserId.equalsIgnoreCase("")){
                    // on below line we are adding check change listener for our switch.
                    // on below line we are checking
                    // if switch is checked or not.
                    if (isChecked) {
                        // on below line we are setting text
                        // if switch is checked.
                        statusTV.setText("Auto Mode ON");
                        AutoModeStatusUpdate("ON");

                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("AUTO MODE CONFIRMATION!")
                                .setMessage("Are You Sure Want To OFF Auto Mode?")
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // on below line we are setting text
                                        // if switch is unchecked.
                                        statusTV.setText("Auto Mode OFF");
                                        AutoModeStatusUpdate("OFF");
                                        dialog.dismiss(); }});
                        builder.create().show();
                    }
                }
                else {
                    switchView.setClickable(false);
                    Toast.makeText(getContext(),"Login First",Toast.LENGTH_LONG).show();
                }
            } });


        arrayList.clear();
        GetPlanList();
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    private void AutoModeStatusUpdate(String Status) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "AutoModeStatusUpdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AutoModeStatusUpdate",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        } }
                    else {
//                        Toast.makeText(getContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("Status", Status);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public void GetPlanList() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "PlansDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PlansDetails",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("PKID", jsonObject1.getString("PKID"));
                        hm.put("PlanName", jsonObject1.getString("PlanName"));
                        hm.put("PlanDis", jsonObject1.getString("PlanDis"));
                        hm.put("PlanDuration", jsonObject1.getString("PlanDuration"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("PlanStatus", jsonObject1.getString("PlanStatus"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                    PositionrAdapter HistoryAdapte = new PositionrAdapter(getContext(), arrayList);
                    plan_rec.setLayoutManager(gridLayoutManager);
                    plan_rec.setAdapter(HistoryAdapte);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public class PositionrAdapter extends RecyclerView.Adapter<PositionrAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        public PositionrAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public PositionrAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_plan, viewGroup, false);
            PositionrAdapter.VH viewHolder = new PositionrAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PositionrAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
            vh.plan_tv.setText(arrayList.get(i).get("PlanDis"));
            vh.duration_tv.setText(arrayList.get(i).get("PlanName"));

            if (arrayList.get(i).get("PlanStatus").equalsIgnoreCase("On")){
                vh.radioMale.setVisibility(VISIBLE);
            }
            else if (arrayList.get(i).get("PlanStatus").equalsIgnoreCase("Off")){
//                vh.radioMale.setChecked(true);
//                vh.radioMale.setClickable(false);
//                vh.radioMale.setFocusable(false);
                                vh.radioMale.setVisibility(INVISIBLE);

            }
            else {
//                vh.radioMale.setChecked(true);
//                vh.radioMale.setClickable(false);
//                vh.radioMale.setFocusable(false);
                vh.radioMale.setVisibility(INVISIBLE);
            }

            vh.radioMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserId.equalsIgnoreCase("")) {
                        Intent intent = new Intent(getContext(), LoginScreenAkp.class);
                        getContext().startActivity(intent);
                        alertDialog1.dismiss();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                        builder.setTitle("Plan Buy Confirmation!")
                                .setMessage("Are you sure you want to Buy this Package?")
                                .setCancelable(false)
                                .setIcon(R.drawable.a_logo)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        PlanPurchaseAPI(arrayList.get(i).get("PKID"));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel(); }
                                });
                        builder.create().show();

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView plan_tv, duration_tv;
            RadioButton radioMale;
            public VH(@NonNull View itemView) {
                super(itemView);
                radioMale = itemView.findViewById(R.id.radioMale);
                plan_tv = itemView.findViewById(R.id.plan_tv);
                duration_tv = itemView.findViewById(R.id.duration_tv);

            }
        }
    }


    public void PlanPurchaseAPI(String pid) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PlanPurchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PlanPurchase",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            Plan_popupScretch(jsonObject1.getString("BonusAmount"));
                            Toast.makeText(getContext(), jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                            alertDialog1.dismiss();
                        }
                    } else {
                        Toast.makeText(getContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                        alertDialog1.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("PlanId", pid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }




    private void Plan_popupScretch(String bonus) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_plan_scretch, viewGroup, false);
        LinearLayout one = dialogView.findViewById(R.id.one);
        LinearLayout two = dialogView.findViewById(R.id.two);

       one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Toast.makeText(PlayBuyDashboard.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                openScratchDialogBox(bonus);
                alertDialog2.dismiss();
            }
        });


        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Toast.makeText(PlayBuyDashboard.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                openScratchDialogBox(bonus);
                alertDialog2.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog2 = builder1.create();
        alertDialog2.show();
    }

    @SuppressLint("SetTextI18n")
    private void openScratchDialogBox(String Bounusamount) {
//        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.blast_one);
//        mp.start();
        alertDialogs = new Dialog(getContext());
        alertDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogs.setContentView(R.layout.alert_scratch_layout_plan);
        alertDialogs.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialogs.setCancelable(false);
        alertDialogs.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView proName = alertDialogs.findViewById(R.id.productName);
        LinearLayout linearExit = alertDialogs.findViewById(R.id.linearExit);
        LinearLayout linearProceed = alertDialogs.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialogs.findViewById(R.id.scratchCard1);


        proName.setText(Bounusamount);
        scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.6) {
//                        scratch(true);
//                    mp.stop();
                    WiningBonusAmount_OnEMIPaidAPI(Bounusamount);
                }
            }
        });
        linearExit.setOnClickListener(v -> {
            alertDialogs.dismiss();
//            mp.stop();
            Intent intent=new Intent(getContext(), GoodLuckGameProductView.class);
            getContext().startActivity(intent);
        });

        linearProceed.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Firstly Scratch !!",Toast.LENGTH_LONG).show();
            linearProceed.setVisibility(View.GONE);

            scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.8) {
//                        scratch(true);
//                        mp.stop();
                        WiningBonusAmount_OnEMIPaidAPI(Bounusamount);

                    }
                }
            });

        });

    }

    public void WiningBonusAmount_OnEMIPaidAPI(String pid) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "WiningBonusAmount_OnEMIPaid", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("WiningBonEMIPaid",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            Toast.makeText(getContext(), jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                            alertDialogs.dismiss();

                        }
                    } else {
                        Toast.makeText(getContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                        alertDialogs.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId", UserId);
                params.put("BonusAmount", pid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    public void GetProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("reponse",response);

                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                             AutoModeStatus       = jsonobject.getString("AutoModeStatus");
                             if (!AutoModeStatus.equalsIgnoreCase("")){
                                 statusTV.setText("Auto Mode "+AutoModeStatus);
                                 if (AutoModeStatus.equalsIgnoreCase("ON")){
                                     switchView.setChecked(true);
                                 }
                                 else {
                                     switchView.setChecked(false);
                                 }
                             }
                        }
                    }
                    else {
//                        Toast.makeText(getContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}


    
