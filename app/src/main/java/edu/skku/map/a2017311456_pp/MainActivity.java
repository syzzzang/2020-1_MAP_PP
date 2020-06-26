package edu.skku.map.a2017311456_pp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference mpostReference;
    EditText userid, password;
    String lguserid="", lgpassword="";
    String jjinid="";
    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();


        if(getIntent().getExtras() != null){
            EditText username = (EditText)findViewById(R.id.userid);
            Intent signupIntent = getIntent();
            username.setText(signupIntent.getStringExtra("id"));
        }

        Session.getCurrentSession().addCallback(sessionCallback);

        Button login = (Button)findViewById(R.id.loginButton);
        userid=(EditText)findViewById(R.id.userid);
        password=(EditText)findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lguserid=userid.getText().toString();
                lgpassword=password.getText().toString();
                if((lguserid.length()*lgpassword.length())==0){
                    Toast.makeText(MainActivity.this,"빈칸있써",Toast.LENGTH_SHORT).show();
                }else{
                    checkId(lguserid,lgpassword);


                }

            }
        });

        TextView signup = (TextView)findViewById(R.id.signupButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(MainActivity.this, SignUp.class);
                startActivity(signupIntent);

            }
        });

        Session.getCurrentSession().addCallback(sessionCallback);
    }


    public void checkId(final String userid, final String password){
        DatabaseReference pref= FirebaseDatabase.getInstance().getReference("user_list").child(userid);

        ValueEventListener valueEventListener=new ValueEventListener() {
            //boolean a=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    checkpw(userid,password);
                }else{
                    Toast.makeText(MainActivity.this,"그런 아이디 없어요",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);
    }
    public void checkpw(final String id, final String pw){
        DatabaseReference pref= FirebaseDatabase.getInstance().getReference("user_list").child(id);//.child("password");
        //String is=id;
        ValueEventListener valueEventListener=new ValueEventListener() {
            //boolean a=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("password").getValue(String.class).equals(pw)){
                    Toast.makeText(MainActivity.this,"로그인 성공!",Toast.LENGTH_SHORT).show();
                    jjinid=id;
                    Intent loginIntent = new Intent(MainActivity.this, postPage.class);
                    loginIntent.putExtra("id",id);
                   startActivity(loginIntent);
                }else{
                    Toast.makeText(MainActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", android.util.Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    private ISessionCallback sessionCallback=new ISessionCallback() {
        @Override
        public void onSessionOpened() {

            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KakaoAPI","세션닫"+errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {

                            UserAccount userAccount=result.getKakaoAccount();
                            name=String.valueOf(result.getId());
                            checkId(name);
                            Log.e("mainname",name);
                            Log.i("KAKAO_SESSION", "로그인 성공");
                            Intent intent=new Intent(MainActivity.this,postPage.class);

                            intent.putExtra("id",name);
                            Log.i("mainname",name);

                            startActivity(intent);

                        }
                    });

////////////////////////////////////////////////////////////////////////////

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("KAKAO_SESSION","로그인 실패",exception);
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void postFirebaseDatabase(boolean add){

        Log.d("hi","값넣기");
        DatabaseReference pref=FirebaseDatabase.getInstance().getReference("user_list").child(name);

        ValueEventListener valueEventListener=new ValueEventListener() {
            //boolean a=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("hi","값안넣기");

                    //a=false;
                    Toast.makeText(getApplicationContext(), "방가방가 "+name+"님", Toast.LENGTH_LONG).show();
                }else{
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                    Map<String,Object> chileUpdates=new HashMap<>();
                    Map<String,Object> postValues=null;


                    FirebasePost post=new FirebasePost(name, "1111");
                    postValues=post.toMap();

                    chileUpdates.put("/user_list/"+name,postValues);
                    databaseReference.updateChildren(chileUpdates);
                    Log.d("hi","값넣낳기");

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);

    }

    public void checkId(final String userid){
        DatabaseReference pref= FirebaseDatabase.getInstance().getReference("user_list").child(userid);

        ValueEventListener valueEventListener=new ValueEventListener() {
            //boolean a=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name=userid;
                    Toast.makeText(getApplicationContext(), "방가방가 "+name+"님", Toast.LENGTH_LONG).show();

                }else{
                    postFirebaseDatabase(true);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);
    }



}

