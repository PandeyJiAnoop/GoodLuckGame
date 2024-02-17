package com.akp.aonestar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcvRecordList;
    AppCompatButton btnMyOrders;
    AlertDialog alertDialog, alertDialog1, alertDialog2, alertDialog3, alertDialog4, alertDialog5, alertDialog6;
    SelectableRoundedImageView ivProfile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String imgString="";
    SelectableRoundedImageView ivImnageProfile;
    String value = String.valueOf(1);
    int n1 = 1;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String UserId, StartTimet, MobileNo, GameId, ivfour;
    EditText edt_name, edt_number, edt_Email;
    Button btnUpdate;
    String Imgpath="";
    TextView tvUserName, tvRecordTypeSet, tvWallet;
    Button btnRedJoin, btnGreenJoin, btnParity, btnSpare, btnBcone, btnEmerd;
    int duration;
    private final ArrayList<HashMap<String, String>> walletlist = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> arrActiveList = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> arrAmountList = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> arrhistoryList = new ArrayList<>();
    RelativeLayout rlWallet, rlAddAmount;
    String type = "card0000001", bitnum = "0";
    String EndTime = "", StartTime = "";
    ArrayList<String> AmountList;
    ArrayList<String> AmountlistId;
    ArrayList<HashMap<String, String>> amountlist;
    double amount = 0;
    double d;
    spinnerAdapter spinner;
    String Selecttype = "";
    TextView tvZero, tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNin;
    EditText edt_amount;
    TextView tvtimeperiod, tvTimeCount;
    boolean namee = true;
    RelativeLayout rlSetAble;
    TextView tvOnee, tvtwo, tvthree, tvfour, tvfive, tvthrees,tvRed,tvGreen;
    ImageView btnSetting;
    LinearLayout llNumbers, llColorsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GetUserlist();
        GetDashboard();
        Log.v("dadadadadada", SimpleDateFormat.getDateTimeInstance().format(new Date()));
    }

    private void findViewById() {

        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("UserId", "");
        GameId = login_preference.getString("GameId", "");
        ivfour = login_preference.getString("ivfour", "");
        rcvRecordList = findViewById(R.id.rcvRecordList);
        ivProfile = findViewById(R.id.ivProfile);
        rlWallet = findViewById(R.id.rlWallet);
        tvUserName = findViewById(R.id.tvUserName);
        tvRecordTypeSet = findViewById(R.id.tvRecordTypeSet);
        tvtimeperiod = findViewById(R.id.tvtimeperiod);
        rlSetAble = findViewById(R.id.rlSetAble);
        btnSetting = findViewById(R.id.btnSetting);
        llNumbers = findViewById(R.id.llNumbers);
        tvRed = findViewById(R.id.tvRed);
        tvGreen = findViewById(R.id.tvGreen);

        //Button
        btnRedJoin = findViewById(R.id.btnRedJoin);
        btnGreenJoin = findViewById(R.id.btnGreenJoin);
        btnMyOrders = findViewById(R.id.btnMyOrders);
        btnParity = findViewById(R.id.btnParity);
        btnSpare = findViewById(R.id.btnSpare);
        btnBcone = findViewById(R.id.btnBcone);
        btnEmerd = findViewById(R.id.btnEmerd);
        rlAddAmount = findViewById(R.id.rlAddAmount);
        tvWallet = findViewById(R.id.tvWallet);
        tvZero = findViewById(R.id.tvZero);
        tvOne = findViewById(R.id.tvOne);
        tvTwo = findViewById(R.id.tvTwo);
        tvThree = findViewById(R.id.tvThree);
        tvFour = findViewById(R.id.tvFour);
        tvFive = findViewById(R.id.tvFive);
        tvSix = findViewById(R.id.tvSix);
        tvSeven = findViewById(R.id.tvSeven);
        tvEight = findViewById(R.id.tvEight);
        tvNin = findViewById(R.id.tvNine);
        tvTimeCount = findViewById(R.id.tvTimeCount);
        llColorsButton = findViewById(R.id.llColorsButton);

        AmountList = new ArrayList<>();
        AmountlistId = new ArrayList<>();
        amountlist = new ArrayList<>();
        tvtimeperiod.setText("Period: " + UserId);

        if (ivfour.equalsIgnoreCase("1")) {
            llNumbers.setVisibility(View.VISIBLE);
            llColorsButton.setVisibility(View.GONE);
        } else {
            llNumbers.setVisibility(View.GONE);
            llColorsButton.setVisibility(View.VISIBLE);
        }
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music();
            }
        });
        btnParity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParity.setBackgroundResource(R.color.darkselect);
                btnSpare.setBackgroundResource(R.color.lightselect);
                btnBcone.setBackgroundResource(R.color.lightselect);
                btnEmerd.setBackgroundResource(R.color.lightselect);
                tvRecordTypeSet.setText("Parity Record");
                type = "card0000001";
            }});

        btnSpare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParity.setBackgroundResource(R.color.lightselect);
                btnSpare.setBackgroundResource(R.color.darkselect);
                btnBcone.setBackgroundResource(R.color.lightselect);
                btnEmerd.setBackgroundResource(R.color.lightselect);
                tvRecordTypeSet.setText("Spare Record");
                type = "card0000002";
            }});
        btnBcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParity.setBackgroundResource(R.color.lightselect);
                btnSpare.setBackgroundResource(R.color.lightselect);
                btnBcone.setBackgroundResource(R.color.darkselect);
                btnEmerd.setBackgroundResource(R.color.lightselect);
                tvRecordTypeSet.setText("Bcone Record");
                type = "card0000003";
            }});
        btnEmerd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParity.setBackgroundResource(R.color.lightselect);
                btnSpare.setBackgroundResource(R.color.lightselect);
                btnBcone.setBackgroundResource(R.color.lightselect);
                btnEmerd.setBackgroundResource(R.color.darkselect);
                tvRecordTypeSet.setText("Emerd Record");
                type = "card0000004";
            }
        });
        btnGreenJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selecttype = "CLR001";
                showbiddinglayout("Green Join", "0");
            }});
        btnRedJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selecttype = "CLR002";
                showbiddinglayout("Red Join", "0");
            }});

        tvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("0 Join", "0");
            }});
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("1 Join", "1");
            }});
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("2 Join", "2");
            }});
        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("3 Join", "3");
            }});
        tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("4 Join", "4");
            }});
        tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("5 Join", "5");
            }});
        tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("6 Join", "6");
            }});
        tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("7 Join", "7");
            }});
        tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("8 Join", "8");
            }});
        tvNin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbiddinglayout("9 Join", "9");
            }});
        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlertDialog();
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletList();
            }
        });
        rlAddAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });
    }

    private void uploadImage() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.uploadimage, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlBack);
        edt_name = (EditText) dialogView.findViewById(R.id.edt_name);
        edt_number = (EditText) dialogView.findViewById(R.id.edt_number);
        edt_Email = (EditText) dialogView.findViewById(R.id.edt_Email);
        btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        TextView tvLogout = (TextView) dialogView.findViewById(R.id.tvLogout);
        GetUserlisttt();
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle("Logout")
                        .setMessage("Are you sure want to Logout").setCancelable(false).setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                                SharedPreferences.Editor editor = myPrefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                startActivity(intent);
                                Intent i = new Intent();
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                //startActivity(i);
                            }
                        }).setNegativeButton(Html.fromHtml("<font color='#000000'>NO</font>"), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }});

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Fields can't be blank!");
                    edt_name.requestFocus();
                } else if (edt_number.getText().toString().equalsIgnoreCase("")) {
                    edt_number.setError("Fields can't be blank!");
                    edt_number.requestFocus();
                } else if (edt_Email.getText().toString().equalsIgnoreCase("")) {
                    edt_Email.setError("Fields can't be blank!");
                    edt_Email.requestFocus();
                } else {
                    ProfileUpdate(edt_name.getText().toString(), edt_number.getText().toString(), edt_Email.getText().toString());
                }
            }});
        ivImnageProfile = (SelectableRoundedImageView) dialogView.findViewById(R.id.ivImnageProfile);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }});
        ivImnageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }});
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder.create();
        alertDialog1.show();

    }


    private void setAlertDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_orders, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlBack);
        RecyclerView rcvOrderHistory = (RecyclerView) dialogView.findViewById(R.id.rcvOrderHistory);
        GetGameHistory(rcvOrderHistory);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }});
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }


    private class MyOrdersAdapter extends RecyclerView.Adapter<MyOrders> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public MyOrdersAdapter(MainActivity mainActivity, ArrayList<HashMap<String, String>> arrhistoryList) {
            data = arrhistoryList;
        }
        public MyOrders onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyOrders(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_orderlist, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final MyOrders holder, final int position) {
            holder.tvCustomerId.setText(data.get(position).get("CustomerId"));
            holder.tvResult.setText(data.get(position).get("GameResult"));
            holder.tvWinPrice.setText(data.get(position).get("winPrice"));
            holder.tvCardType.setText(data.get(position).get("CardName"));
            holder.tvDate.setText(data.get(position).get("EntryDate"));
        }
        public int getItemCount() {
            return data.size();
        }
    }

    public class MyOrders extends RecyclerView.ViewHolder {
        TextView tvCustomerId, tvResult, tvCardType, tvWinPrice, tvPlayerType, tvDate;
        public MyOrders(View itemView) {
            super(itemView);
            tvCustomerId = itemView.findViewById(R.id.tvCustomerId);
            tvResult = itemView.findViewById(R.id.tvResult);
            tvCardType = itemView.findViewById(R.id.tvCardType);
            tvWinPrice = itemView.findViewById(R.id.tvWinPrice);
            tvPlayerType = itemView.findViewById(R.id.tvPlayerType);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    private class MyOrderListAdapter extends RecyclerView.Adapter<OrderList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public MyOrderListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> walletlist) {
            data = walletlist;
        }
        public OrderList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderList(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_history_list, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OrderList holder, final int position) {
            holder.tvDate.setText(data.get(position).get("AddedTime"));
            holder.tvPaidAmount.setText(data.get(position).get("WalletAmt") + " added to your wallet successfully");
        }
        public int getItemCount() {
            return data.size();
        }
    }

    public class OrderList extends RecyclerView.ViewHolder {
        TextView tvCustmore, tvDate, tvPaidAmount;
        public OrderList(View itemView) {
            super(itemView);
            tvPaidAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvCustmore = itemView.findViewById(R.id.tvCustmore);
            tvDate = itemView.findViewById(R.id.tvDate);
        }}
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            } });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivProfile.setImageBitmap(thumbnail);
        ivImnageProfile.setImageBitmap(thumbnail);
        getEncoded64ImageStringFromBitmap(thumbnail);
  }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivProfile.setImageBitmap(bm);
        ivImnageProfile.setImageBitmap(bm);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        Log.v("dadadadadaaaa", imgString);
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
    }


    private class RecordListAdapter extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public RecordListAdapter(MainActivity mainActivity, ArrayList<HashMap<String, String>> arrActiveList) {
            data = arrActiveList;
        }
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_recordlist, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.tvActiveUserId.setText(data.get(position).get("CustomerId"));
            holder.tvPrice.setText(data.get(position).get("CardPrice"));
            holder.TvNumnber.setText(data.get(position).get("No"));
        }
        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvActiveUserId, tvPrice, TvNumnber, tvResult;
        public Holder(View itemView) {
            super(itemView);
            tvActiveUserId = itemView.findViewById(R.id.tvActiveUserId);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            TvNumnber = itemView.findViewById(R.id.TvNumnber);
            tvResult = itemView.findViewById(R.id.tvResult);
        }
    }

    public static class spinnerAdapter extends ArrayAdapter<String> {
        ArrayList<String> data;
        public spinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {
            super(context, textViewResourceId, arraySpinner_time);
            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }


    private void showbiddinglayout(String green_join, String typee) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bitnoinlayout, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlBack);
        Spinner sppiner = (Spinner) dialogView.findViewById(R.id.sppiner);
        ImageView iv_add = (ImageView) dialogView.findViewById(R.id.iv_add);
        ImageView iv_minus = (ImageView) dialogView.findViewById(R.id.iv_minus);
        TextView tv_qty = (TextView) dialogView.findViewById(R.id.tv_qty);
        TextView tvTotal = (TextView) dialogView.findViewById(R.id.tvTotalll);
        TextView tvSelectedType = (TextView) dialogView.findViewById(R.id.tvSelectedType);
        Button btnConfirm = (Button) dialogView.findViewById(R.id.btnConfirm);
        tvSelectedType.setText(green_join);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitActivoties(typee, tv_qty.getText().toString(), tvTotal.getText().toString());

            }
        });
        sppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView spinnerText = (TextView) sppiner.getChildAt(0);
//                spinnerText.setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString() == "Select State") {

                } else {

                    d = Double.parseDouble((AmountlistId.get(sppiner.getSelectedItemPosition())));
                    DecimalFormat twoDForm = new DecimalFormat("#.##");
                    amount = Double.valueOf(twoDForm.format(d));


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetAmountlist(sppiner);

        iv_minus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                n1 = Integer.parseInt(tv_qty.getText().toString());

                if (n1 > 1) {
                    n1 = n1 - 1;
                    value = String.valueOf(n1);

                    tv_qty.setText(value);

                    int ProductPrice = Integer.parseInt(value);

                    Log.v("asggsa", String.valueOf(ProductPrice * amount));
                    tvTotal.setText(String.valueOf(ProductPrice * amount));

                }

            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                n1 = Integer.parseInt(tv_qty.getText().toString());

                n1 = n1 + 1;

                value = String.valueOf(n1);
                tv_qty.setText(value);
                int ProductPrice = Integer.parseInt(value);
                Log.v("asggsa", String.valueOf(ProductPrice * amount));


                tvTotal.setText(String.valueOf(ProductPrice * amount));


            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog4.dismiss();
            }
        });


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog4 = builder.create();
        alertDialog4.show();

    }


    public void GetWalletlist(RecyclerView dynamic_ll) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetWalletlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                walletlist.clear();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    JSONArray Jarray = object.getJSONArray("mwcls");
                    if (Jarray.length() == 0) {
                        Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CustomerId", jsonObject1.getString("CustomerId"));
                            hashlist.put("WalletAmt", jsonObject1.getString("WalletAmt"));
                            hashlist.put("AddedTime", jsonObject1.getString("AddedTime"));

                            walletlist.add(hashlist);

                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    MyOrderListAdapter adapter = new MyOrderListAdapter(getApplicationContext(), walletlist);
                    dynamic_ll.setLayoutManager(layoutManager);
                    dynamic_ll.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void ProfileUpdate(String name, String number, String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/ProfileUpdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Msg = object.getString("Msg");
                    String Status = object.getString("Status");


                    if (Status.equalsIgnoreCase("true")) {
                        alertDialog1.dismiss();
                        Toast.makeText(MainActivity.this, "Profile Update Success", Toast.LENGTH_SHORT).show();

                        GetUserlisttt();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);
                params.put("Name", name);
                params.put("Gender", "");
                params.put("MobileNo", number);
                params.put("EmailId", email);
                params.put("StateId", "");
                params.put("CityId", "");
                params.put("Imagefile", imgString);
                params.put("Path", Imgpath);
                params.put("Address", "");

                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void GetUserlist() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetUserlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Msg = object.getString("Msg");
                    String Status = object.getString("Status");
                    MobileNo = object.getString("MobileNo");
                    String EmailId = object.getString("EmailId");
                    String Imgpath = object.getString("profile");
                    String Name = object.getString("Name");
                /*    edt_Email.setText(EmailId);
                    edt_number.setText(MobileNo);
                    edt_name.setText(Name);*/
                    tvUserName.setText(Name);
                    if (Imgpath.equalsIgnoreCase("")) {

                    } else {
/*
                        Picasso.get().load(Imgpath).into(ivImnageProfile);
*/
                        Picasso.get().load(Imgpath).into(ivProfile);

                    }


                    if (Status.equalsIgnoreCase("true")) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void GetUserlisttt() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetUserlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Msg = object.getString("Msg");
                    String Status = object.getString("Status");
                    MobileNo = object.getString("MobileNo");
                    String EmailId = object.getString("EmailId");
                     Imgpath = object.getString("Imgpath");
                    String profile = object.getString("profile");
                    String Name = object.getString("Name");
                    edt_Email.setText(EmailId);
                    edt_number.setText(MobileNo);
                    edt_name.setText(Name);
                    tvUserName.setText(Name);
                    if (profile.equalsIgnoreCase("")) {

                    } else {
                        Picasso.get().load(profile).into(ivImnageProfile);
                        Picasso.get().load(profile).into(ivProfile);

                    }


                    if (Status.equalsIgnoreCase("true")) {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void walletList() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.wallethistory, viewGroup, false);
        ImageView rlBack = (ImageView) dialogView.findViewById(R.id.back_img);
        Button btnWithDraw = (Button) dialogView.findViewById(R.id.btnWithDraw);

        btnWithDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debit_popup();
            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog3.dismiss();
            }
        });
        RecyclerView dynamic_ll = (RecyclerView) dialogView.findViewById(R.id.dynamic_ll);

        GetWalletlist(dynamic_ll);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog3 = builder.create();
        alertDialog3.show();
    }

    private void addWallet() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_wallet, viewGroup, false);
        ImageView rlBack = (ImageView) dialogView.findViewById(R.id.ivback);

        tvOnee = dialogView.findViewById(R.id.tvOne);
        tvtwo = dialogView.findViewById(R.id.tvtwo);
        tvthree = dialogView.findViewById(R.id.tvthree);
        tvfour = dialogView.findViewById(R.id.tvfour);
        tvfive = dialogView.findViewById(R.id.tvfive);
        tvthrees = dialogView.findViewById(R.id.tvthrees);
        edt_amount = dialogView.findViewById(R.id.edt_amount);
        Button addButton = dialogView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(edt_amount.getText().toString());
//                startPayment(price);
            }
        });
        tvOnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("100");
                tvOnee.setBackgroundResource(R.drawable.selectsizebackground);
                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);

            }
        });

        tvtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("150");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
                tvtwo.setBackgroundResource(R.drawable.selectsizebackground);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);

            }
        });
        tvthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("200");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.selectsizebackground);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);

            }
        });
        tvfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("500");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.selectsizebackground);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);

            }
        });
        tvfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("2000");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.selectsizebackground);

            }
        });
        tvthrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_amount.setText("3000");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.selectsizebackground);

            }
        });


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog2.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog2 = builder.create();
        alertDialog2.show();
    }

    public void GetDashboard() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetDashboard", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                walletlist.clear();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    tvTimeCount.setText("");
                    JSONObject object = new JSONObject(jsonString);
                    StartTime = object.getString("StartTime");
                    EndTime = object.getString("EndTime");
                    int TimeDiff = Integer.parseInt(object.getString("TimeDiff"));


                    String WalletBalance = object.getString("WalletBalance");
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true"))
                        tvWallet.setText(" Wallet Amount Rs." + WalletBalance);

                    GetActiveCustomer(type);


                    new CountDownTimer(TimeDiff * 60 * 1000, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {
                            namee = true;
                            rlSetAble.setFocusable(false);
                            tvTimeCount.setText("" + String.format("%d:%02d",

                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),

                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -

                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


                        }

                        @Override
                        public void onFinish() {
                            rlSetAble.setFocusable(true);

                            winresult(login_preference.getString("StartTime", ""));
                            GetDashboard();
                        }

                    }.start();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void GetActiveCustomer(String type) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetActiveCustomer", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                arrActiveList.clear();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Status = object.getString("Status");


                    if (Status.equalsIgnoreCase("true")) {
                        refreshdashboard();
                        JSONArray Jarray = object.getJSONArray("Actcls");


                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CustomerId", jsonObject1.getString("CustomerId"));
                            hashlist.put("CustomerName", jsonObject1.getString("CustomerName"));
                            hashlist.put("CardPrice", jsonObject1.getString("CardPrice"));
                            hashlist.put("No", jsonObject1.getString("No"));
                            arrActiveList.add(hashlist);

                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
                        RecordListAdapter adapter = new RecordListAdapter(MainActivity.this, arrActiveList);
                        rcvRecordList.setLayoutManager(layoutManager);
                        rcvRecordList.setAdapter(adapter);

                    }





                    /*RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    MyOrderListAdapter adapter = new MyOrderListAdapter(getApplicationContext(), walletlist);
                    dynamic_ll.setLayoutManager(layoutManager);
                    dynamic_ll.setAdapter(adapter);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Cardid", type);
                params.put("StartTime", SimpleDateFormat.getDateTimeInstance().format(new Date()));
                login_editor = login_preference.edit();
                login_editor.putString("StartTime", SimpleDateFormat.getDateTimeInstance().format(new Date()));
                login_editor.commit();
                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }


    public void GetAmountlist(Spinner sppiner) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetAmountlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {

                        amountlist.clear();
                        AmountList.clear();
                        AmountlistId.clear();

                        JSONArray Jarray = object.getJSONArray("amtcls");


                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("Amount", jsonObject1.getString("Amount"));

                            arrAmountList.add(hashlist);

                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            AmountList.add(arrAmountList.get(i).get("Amount"));
                            AmountlistId.add(arrAmountList.get(i).get("Amount"));
                        }
                        spinner = new spinnerAdapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>) AmountList);
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sppiner.setAdapter(spinner);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void GetGameHistory(RecyclerView rcvOrderHistory) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetGameHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {


                        JSONArray Jarray = object.getJSONArray("ghCls");

                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CustomerId", jsonObject1.getString("CustomerId"));
                            hashlist.put("GameResult", jsonObject1.getString("GameResult"));
                            hashlist.put("CardPrice", jsonObject1.getString("CardPrice"));
                            hashlist.put("winPrice", jsonObject1.getString("winPrice"));
                            hashlist.put("ColorId", jsonObject1.getString("ColorId"));
                            hashlist.put("ColorName", jsonObject1.getString("ColorName"));
                            hashlist.put("CardId", jsonObject1.getString("CardId"));
                            hashlist.put("CardName", jsonObject1.getString("CardName"));
                            hashlist.put("EntryDate", jsonObject1.getString("EntryDate"));
                            arrhistoryList.add(hashlist);

                        }

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
                        MyOrdersAdapter adapter = new MyOrdersAdapter(MainActivity.this, arrhistoryList);
                        rcvOrderHistory.setLayoutManager(layoutManager);
                        rcvOrderHistory.setAdapter(adapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void winner(String customerName, String colorName) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.congratulation, viewGroup, false);
        Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
        id_tv.setText("is " +colorName+" Name"+ customerName);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog5.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog5 = builder.create();
        alertDialog5.show();
    }

    public void winresult(String startTime) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetGameResult", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("testttt", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Status = object.getString("Status");

                    if (Status.equalsIgnoreCase("true")) {


                        JSONArray Jarray = object.getJSONArray("grCls");


                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            winner(jsonObject1.getString("CustomerName"),jsonObject1.getString("ColorName"));


                        }


                    } else {
                        Toast.makeText(MainActivity.this, "No Person Available", Toast.LENGTH_SHORT).show();
                    }





                    /*RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    MyOrderListAdapter adapter = new MyOrderListAdapter(getApplicationContext(), walletlist);
                    dynamic_ll.setLayoutManager(layoutManager);
                    dynamic_ll.setAdapter(adapter);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Cardid", type);
                params.put("StartTime", startTime);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public void refreshdashboard() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetDashboard", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                walletlist.clear();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    tvTimeCount.setText("");
                    JSONObject object = new JSONObject(jsonString);
                    StartTime = object.getString("StartTime");
                    EndTime = object.getString("EndTime");
                    int TimeDiff = Integer.parseInt(object.getString("TimeDiff"));


                    String WalletBalance = object.getString("WalletBalance");
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true"))
                        tvWallet.setText(" Wallet Amount Rs." + WalletBalance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
/*
    private void startPayment(double toString) {
        Checkout chackout = new Checkout();
        chackout.setKeyID("rzp_live_2WUX4uxFdrFtjV");
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "StarProducts");
            options.put("description", "Your Add Money");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", toString * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "abc@gmail.com");
            preFill.put("contact", MobileNo);
            options.put("prefill", preFill);

            co.open(MainActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error in payment:" + e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorPaymentId) {
        try {
            Log.v("adsdfsas", razorPaymentId);
            Toast.makeText(MainActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
            Payment(razorPaymentId);
        } catch (Exception e) {
            Log.e("razorPayment", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {

            Toast.makeText(MainActivity.this, "Payment failed:", Toast.LENGTH_SHORT).show();

            Log.e("razorPayment", " " + code + " " + response);
        } catch (Exception e) {
            Log.e("razorPayment", "Exception in onPaymentError", e);
        }
    }*/

    public void Payment(String razorPaymentId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/Payment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {

                    JSONObject object = new JSONObject(jsonString);
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        wallet();
                        alertDialog2.dismiss();
                        Toast.makeText(MainActivity.this, "Payment Add SuccessFully", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);
                params.put("Amount", edt_amount.getText().toString());
                params.put("TransactionId", razorPaymentId);

                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void music() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.music, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlHeader);
        Switch swtich = (Switch) dialogView.findViewById(R.id.swtich);
        swtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(BackgroundSoundService.class)) {
                    stopService(new Intent(MainActivity.this, BackgroundSoundService.class));
                } else {
                    Toast.makeText(MainActivity.this, "Music Service started by user.", Toast.LENGTH_LONG).show();

                    startService(new Intent(MainActivity.this, BackgroundSoundService.class));
                }
            }
        });


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog6.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog6 = builder.create();
        alertDialog6.show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void wallet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetDashboard", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    tvTimeCount.setText("");
                    JSONObject object = new JSONObject(jsonString);

                    int TimeDiff = Integer.parseInt(object.getString("TimeDiff"));


                    String WalletBalance = object.getString("WalletBalance");
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true"))
                        tvWallet.setText(" Wallet Amount Rs." + WalletBalance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
    public void BitActivoties(String typee, String quantity, String totalamount) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/BitActivoties", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                walletlist.clear();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    String Status = object.getString("Status");
                    String Msg = object.getString("Msg");

                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray array=object.getJSONArray("Bcls");
                        Toast.makeText(MainActivity.this, "Bidding Successfully", Toast.LENGTH_SHORT).show();
                        alertDialog4.dismiss();

                        GetActiveCustomer(type);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            if(jsonObject1.getString("Bcolor").equalsIgnoreCase("Green"))
                            {
                                tvGreen.setText("Green Rs."+jsonObject1.getString("Amount"));

                            }
                            else {
                                tvRed.setText("Red Rs."+jsonObject1.getString("Amount"));

                            }


                        }



                    } else {


                        Toast.makeText(MainActivity.this, Msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);
                params.put("Cardid", type);
                params.put("CardPrice", String.valueOf(amount));
                params.put("TotalAmount", totalamount);
                params.put("CLRid", Selecttype);
                params.put("BitNo", quantity);
                params.put("Amtid", typee);
                params.put("GameId", GameId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
    private void Debit_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.debitpopup, viewGroup, false);
       EditText rupee_et1 = (EditText) dialogView.findViewById(R.id.rupee_et1);
       EditText name_et = (EditText) dialogView.findViewById(R.id.name_et);
       EditText account_no_et = (EditText) dialogView.findViewById(R.id.account_no_et);
       EditText ifsc_et = (EditText) dialogView.findViewById(R.id.ifsc_et);
       EditText branch_name_et = (EditText) dialogView.findViewById(R.id.branch_name_et);

        Button ok1 = (Button) dialogView.findViewById(R.id.buttonOk1);
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rupee_et1.getText().toString().equalsIgnoreCase("")){
                    rupee_et1.setError("Fields can't be empty");
                    rupee_et1.requestFocus();
                }
                else if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be empty");
                    name_et.requestFocus();
                }
                 else if (account_no_et.getText().toString().equalsIgnoreCase("")){
                    account_no_et.setError("Fields can't be empty");
                    account_no_et.requestFocus();
                }
                  else if (ifsc_et.getText().toString().equalsIgnoreCase("")){
                    ifsc_et.setError("Fields can't be empty");
                    ifsc_et.requestFocus();
                }
                   else if (branch_name_et.getText().toString().equalsIgnoreCase("")){
                    branch_name_et.setError("Fields can't be empty");
                    branch_name_et.requestFocus();
                }
                else {
                   // DebitWalletMoneyAPI(UserId,rupee_et1.getText().toString());
                }
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
    }

}