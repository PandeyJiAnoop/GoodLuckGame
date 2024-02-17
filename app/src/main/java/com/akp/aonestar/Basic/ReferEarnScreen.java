package com.akp.aonestar.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ReferEarnScreen extends AppCompatActivity {

    ImageView back_img,referimg;
    TextView edtcode;
    AppCompatButton share_tv;
    String UserId,UserName;
    private ClipboardManager myClipboard;
    private RelativeLayout referral_rl;
    private ClipData myClip;
    RelativeLayout rlHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_refer_earn_screen);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        rlHeader=findViewById(R.id.rlHeader);
        edtcode=findViewById(R.id.edtcode);
        back_img=findViewById(R.id.back_img);
        share_tv=findViewById(R.id.share_tv);
        referimg=findViewById(R.id.referimg);
        referral_rl=findViewById(R.id.referral_rl);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createlink();

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        referral_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = edtcode.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createlink(){
        Log.e("main", "create link ");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://AOneStar.in/"))
                .setDomainUriPrefix("AOneStarin.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer "+ dynamicLink.getUri());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link
        createReferlink(UserId, UserId);
    }
    public void createReferlink(String custid, String prodid){
        // manuall link
        String sharelinktext  = "https://aonestar.page.link/?"+
                "link=https://aoneshop.co.in/myrefer.php?custid="+custid +"-"+prodid+
                "&apn="+ getPackageName()+
                "&st="+"AOneStar Refer Link"+
                "&sd="+"Download AOneStar From PlayStore User MY REFERRAL CODE:- "+UserId+" "+
                "&si="+"https://aoneshop.co.in/website-assets/images/logo.png";
        Log.e("mainactivity", "sharelink - "+sharelinktext);
        // shorten the link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                //.setLongLink(dynamicLink.getUri())    // enable it if using firebase method dynamicLink
                .setLongLink(Uri.parse(sharelinktext))  // manually
                .buildShortDynamicLink()
                .addOnCompleteListener(ReferEarnScreen.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Date date = new Date();
                            CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);
                            try {
                                File mainDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
                                if (!mainDir.exists()) {
                                    boolean mkdir = mainDir.mkdir();
                                }
                                String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
                                referimg.setDrawingCacheEnabled(true);
                                Bitmap bitmap = Bitmap.createBitmap(referimg.getDrawingCache());
                                referimg.setDrawingCacheEnabled(false);
                                File imageFile = new File(path);
                                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                edtcode.setText(shortLink.toString());
                                Uri uri =  FileProvider.getUriForFile(getApplicationContext(), "com.akp.aonestar.provider", imageFile);

                                share_tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.setType("image/*");
                                        intent.putExtra(Intent.EXTRA_TEXT, "Hi,\n" +
                                                "Inviting you to join AOneStar\n" +
                                                "an interesting app which provides you incredible offers on Plan,Play Game,Shopping & many more.\n\n" +"Use my referrer code :- "+UserId+"\n\nDownload app from link : "+shortLink.toString());
                                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                                        try {
                                            startActivity(Intent.createChooser(intent, "Share With"));
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(ReferEarnScreen.this, "No App Available", Toast.LENGTH_SHORT).show();
                                        } }});
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                            Log.e("main ", "short link "+ shortLink.toString());
//                            // share app dialog
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);
                        } else {
                            Log.e("main", " error "+task.getException() );
                        }}});

    }

}