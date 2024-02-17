package com.akp.aonestar.New_ShopIt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.akp.aonestar.Basic.BottomOnClick;
import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.GoodLuckModule.ChooseDiscountProduct;
import com.akp.aonestar.GoodLuckModule.ConsolationNew;
import com.akp.aonestar.GoodLuckModule.ConsolationNew1;
import com.akp.aonestar.GoodLuckModule.ConsolationProduct;
import com.akp.aonestar.GoodLuckModule.DoWantAnythingElse;
import com.akp.aonestar.GoodLuckModule.FreeJoiningProduct;
import com.akp.aonestar.GoodLuckModule.FreeTrailProductDetails;
import com.akp.aonestar.GoodLuckModule.NewTab.NewUpdate_GoodLuckProductList;
import com.akp.aonestar.GoodLuckModule.QuizJackpot;
import com.akp.aonestar.GoodLuckModule.SeasonalSaleProduct;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class ShopIt_PrimePackageList extends AppCompatActivity {
    LinearLayout ll;
    GifImageView imga, imgb, imgb1, imgb2, imgc, imgd, imge, imgf, imgg, want_anything_gf;
    GifImageView buynow_btn;
    String UserId, UserName;
    TextView name_tv;
    LinearLayout plan_ll;
    TextView welcome_name_tv;
    View view;
    ImageView back_img, review_img, bottom_viewmore_img;
    GifImageView dumy;
    LinearLayout text_ll;
    RelativeLayout bottom_dummy_rl;

    ReviewManager reviewManager;
    ReviewInfo reviewInfo = null;
    int k = 0;
    TextView dummy_text;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private ShopIt_PrimePackageList.FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    MediaPlayer mp;
    ImageView details;
    private AlertDialog alertDialog2;
    String GetCatId;
    TextView tvtimeperiod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_it_prime_package_list);
        welcome_name_tv = findViewById(R.id.welcome_name_tv);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");

        GetCatId=getIntent().getStringExtra("cat_id");


        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        BottomOnClick bottom = (BottomOnClick) findViewById(R.id.bottom1);
        bottom.initHeader();

        mp = MediaPlayer.create(ShopIt_PrimePackageList.this, R.raw.akp_click);
        want_anything_gf = findViewById(R.id.want_anything_gf);
        ll = findViewById(R.id.ll);
        imga = findViewById(R.id.imga);
        imgb = findViewById(R.id.imgb);
        imgb1 = findViewById(R.id.imgb1);
        imgb2 = findViewById(R.id.imgb2);
        imgc = findViewById(R.id.imgc);
        imgd = findViewById(R.id.imgd);
        imge = findViewById(R.id.imge);
        imgf = findViewById(R.id.imgf);
        imgg = findViewById(R.id.imgg);
        buynow_btn = findViewById(R.id.buynow_btn);
        back_img = findViewById(R.id.back_img);
        tvtimeperiod = findViewById(R.id.tvtimeperiod);
        details = (ImageView) findViewById(R.id.details);


        plan_ll = findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view = findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);
        text_ll = findViewById(R.id.text_ll);
        dummy_text = findViewById(R.id.dummy_text);
        bottom_dummy_rl = findViewById(R.id.bottom_dummy_rl);
        bottom_viewmore_img = findViewById(R.id.bottom_viewmore_img);
        rcvList = findViewById(R.id.rcvList);
        norecord_tv = findViewById(R.id.norecord_tv);
        CategoryAPI();
        bottom_viewmore_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k == 0) {
                    bottom_dummy_rl.setVisibility(View.GONE);
                    k++;
                } else if (k == 1) {
                    bottom_dummy_rl.setVisibility(View.VISIBLE);
                    k = 0;
                }
            }
        });

        review_img = findViewById(R.id.review_img);
        dumy = findViewById(R.id.dumy);

        TextView txtMarquee = (TextView) findViewById(R.id.marqueeText);
        txtMarquee.setSelected(true);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameSelection.class);
                startActivity(intent);
            }
        });

        GameBottomDetailslistAPI();
        FlashSMS_GETDetailsAPI();

        if (UserId.equalsIgnoreCase("")) {
        } else {
            BlockUnblockAPI();
        }

        review_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
//                getReviewInfo();
//                startReviewFlow();
            }
        });

        Animation ab = AnimationUtils.loadAnimation(this, R.anim.move);
//        ll.startAnimation(ab);

        Animation an = AnimationUtils.loadAnimation(this, R.anim.blink);

//        imga.startAnimation(an);
//        imgb.startAnimation(an);
//        imgc.startAnimation(an);
//        imgd.startAnimation(an);
//        imge.startAnimation(an);
//        imgf.startAnimation(an);
//        buynow_btn.startAnimation(an);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetails_popup1(GetCatId);
            }
        });
        //        Floating layout call here
        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("GoodLuck");
        imga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FreeTrailProductDetails.class);
                startActivity(intent);

            }
        });
        want_anything_gf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoWantAnythingElse.class);
                startActivity(intent);
            }
        });
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsolationProduct.class);
                startActivity(intent);
            }
        });


        imgb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsolationNew.class);
                startActivity(intent);
            }
        });


        imgb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConsolationNew1.class);
                startActivity(intent);
            }
        });


        imgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FreeJoiningProduct.class);
                startActivity(intent);
            }
        });
        imgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeasonalSaleProduct.class);
                startActivity(intent);
            }
        });
        imge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseDiscountProduct.class);
                startActivity(intent);
            }
        });
        imgf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizJackpot.class);
                startActivity(intent);
            }
        });

        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), GameSelection.class);
        startActivity(intent);
    }

    public void BlockUnblockAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "UserblockUnblockStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("UserblockUnblockStatus", "sadsad" + response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {

                    } else {
                        Toast.makeText(ShopIt_PrimePackageList.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShopIt_PrimePackageList.this);
                        builder.setTitle("Login Alert Confirmation!")
                                .setMessage(object.getString("Message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPrefs.edit();
                                        editor.clear();
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                        startActivity(intent);
                                    }
                                });
                        builder.create().show();
                        Toast.makeText(ShopIt_PrimePackageList.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(ShopIt_PrimePackageList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_PrimePackageList.this);
        requestQueue.add(stringRequest);

    }


    public void GameBottomDetailslistAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "GameBottomDetailslist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GameBottomDetailslist", "sadsad" + response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            dummy_text.setText(Html.fromHtml(jsonObject.getString("Dis")));
                            Glide.with(ShopIt_PrimePackageList.this).load(jsonObject.getString("BannerImg")).error(R.drawable.a_logo).into(dumy);
                        }
                    } else {
                        Toast.makeText(ShopIt_PrimePackageList.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(ShopIt_PrimePackageList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_PrimePackageList.this);
        requestQueue.add(stringRequest);

    }


    public void CategoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_PrimePackageList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "GameSubCategoryList_New", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GameSubCategoryList_New",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
//                            title_tv.setText("External Link Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("SubCatName", jsonObject.getString("SubCatName"));
                            hashlist.put("SubCatImg", jsonObject.getString("SubCatImg"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        pdfAdapTer = new ShopIt_PrimePackageList.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(ShopIt_PrimePackageList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
//                Toast.makeText(ShopIt_PrimePackageList.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", GetCatId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_PrimePackageList.this);
        requestQueue.add(stringRequest);

    }


    public class FriendsListAdapter extends RecyclerView.Adapter<ShopIt_PrimePackageList.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }

        public ShopIt_PrimePackageList.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopIt_PrimePackageList.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_catshopit, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ShopIt_PrimePackageList.FriendsList holder, final int position) {
            holder.cat_name.setText(data.get(position).get("SubCatName"));
            if (data.get(position).get("SubCatImg").equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Image not found!", Toast.LENGTH_LONG).show();
            } else {
                Glide.with(ShopIt_PrimePackageList.this).load(data.get(position).get("SubCatImg")).into(holder.imgb);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   Log.d("res_id",data.get(position).get("PKID"));
                    mp.start();
                    if (data.get(position).get("PKID").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getApplicationContext(), PP_WelcomePrimeGift.class);
                        intent.putExtra("cat_id",GetCatId);
                        intent.putExtra("sub_cat_id",data.get(position).get("PKID"));
                        startActivity(intent);
                    }
                    else if (data.get(position).get("PKID").equalsIgnoreCase("3")) {
                        Intent intent = new Intent(getApplicationContext(), PP_PrimeTradeProductList.class);
                        startActivity(intent);
                    }
                    else if (data.get(position).get("PKID").equalsIgnoreCase("2")) {
                        Intent intent = new Intent(getApplicationContext(), PP_MyChoiceList.class);
                        startActivity(intent);
                    }
                    else if (data.get(position).get("PKID").equalsIgnoreCase("4")) {
                        Intent intent = new Intent(getApplicationContext(), PP_BasicSwingProduct.class);
                        startActivity(intent);
                    }


         /*           else if (data.get(position).get("PKID").equalsIgnoreCase("3")) {
                        Intent intent = new Intent(getApplicationContext(), ConsolationProduct.class);
                        startActivity(intent);
                    } else if (data.get(position).get("PKID").equalsIgnoreCase("7")) {
                        Intent intent = new Intent(getApplicationContext(), ConsolationNew.class);
                        startActivity(intent);
                    } else if (data.get(position).get("PKID").equalsIgnoreCase("8")) {
                        Intent intent = new Intent(getApplicationContext(), ConsolationNew1.class);
                        startActivity(intent);
                    } else if (data.get(position).get("PKID").equalsIgnoreCase("6")) {
                        Intent intent = new Intent(getApplicationContext(), QuizJackpot.class);
                        startActivity(intent);
                    } else if (data.get(position).get("PKID").equalsIgnoreCase("9")) {
                        Intent intent = new Intent(getApplicationContext(), DoWantAnythingElse.class);
                        startActivity(intent);
                    }

                    else if (data.get(position).get("PKID").equalsIgnoreCase("16")) {
                        Intent intent = new Intent(getApplicationContext(), ShopIt_productBetting.class);
                        startActivity(intent);
                    }

                    else {
                        Intent intent = new Intent(getApplicationContext(), NewUpdate_GoodLuckProductList.class);
                        intent.putExtra("update_id",data.get(position).get("PKID"));
                        intent.putExtra("update_name",data.get(position).get("SubCatName"));
                        startActivity(intent);
                    }}*/}
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class FriendsList extends RecyclerView.ViewHolder {
        TextView cat_name;
        GifImageView imgb;


        public FriendsList(View itemView) {
            super(itemView);
            imgb = itemView.findViewById(R.id.imgb);
            cat_name = itemView.findViewById(R.id.cat_name);
        }
    }


    private void GameDetails_popup1(String getId1) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ShopIt_PrimePackageList.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_PrimePackageList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "GoodLuck_GameDetailsbyCat", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resdetails",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        view_details_tv.setText(Html.fromHtml(jsonObject1.getString("GameDes")));
                    }
                } catch (JSONException e) {
                    Toast.makeText(ShopIt_PrimePackageList.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
//                Toast.makeText(ShopIt_PrimePackageList.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getId1);
                Log.d("resdetails", String.valueOf(params));

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_PrimePackageList.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ShopIt_PrimePackageList.this);
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

    public void FlashSMS_GETDetailsAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "FlashSMS_GETDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GameBottomDetailslist", "sadsad" + response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            tvtimeperiod.setText(Html.fromHtml(jsonObject.getString("GameDes")));
                            tvtimeperiod.setSelected(true);
                        }
                    } else {
                        Toast.makeText(ShopIt_PrimePackageList.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }}}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(ShopIt_PrimePackageList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            } });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_PrimePackageList.this);
        requestQueue.add(stringRequest);
    }
}