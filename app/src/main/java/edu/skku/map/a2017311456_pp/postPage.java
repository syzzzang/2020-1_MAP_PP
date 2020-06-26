package edu.skku.map.a2017311456_pp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;

import java.util.HashMap;
import java.util.Map;


public class postPage extends AppCompatActivity {

    private static final int PICK_IMAGE=777;
    String name="";
    String day="";
    String todo="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);


        if(getIntent().getExtras() != null){
            Intent signupIntent = getIntent();
            name=signupIntent.getStringExtra("id");

        }

            ViewPager2 viewPager2 = findViewById(R.id.viewpager);
            viewPager2.setAdapter(new myFragmentStateAdapter(this,name));

            TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayout);
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    switch (position){
                        case 0:
                            tab.setText("7D");
                            break;
                        case 1:
                            tab.setText("Mon");
                            break;
                        case 2:
                            tab.setText("Tue");
                            break;
                        case 3:
                            tab.setText("Wed");
                            break;
                        case 4:
                            tab.setText("Thu");
                            break;
                        case 5:
                            tab.setText("Fri");
                            break;
                        case 6:
                            tab.setText("Sat");
                            break;
                        case 7:
                            tab.setText("Sun");
                            break;
                    }
                }
            });
            tabLayoutMediator.attach();




        final ImageButton newPost = (ImageButton)findViewById(R.id.newPost);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items=new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
                final int[] selectedindex={0};
                AlertDialog.Builder dialog=new AlertDialog.Builder(postPage.this);
                dialog.setTitle("무슨 요일????")
                        .setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedindex[0]=which;
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                day=items[selectedindex[0]];

                                 AlertDialog.Builder ad=new AlertDialog.Builder(postPage.this);
                ad.setTitle("할 일 추가");
                ad.setMessage("할 일을 써주쇼");
                final EditText et=new EditText(postPage.this);
                ad.setView(et);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = et.getText().toString();
                        todo=value;
                        post(todo,day);
                        Toast.makeText(postPage.this,day+ " : "+todo+" 이/가 추가! 잠시만 기다려주세요!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("f","No Btn Click");
                        dialog.dismiss();
                    }
                });
                ad.show();
                            }
                        }).create().show();
            }
        });

    }

    public void post(String todo,String day){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user_list").child(name).child("day").child(day);
        Map<String,Object>childUpdates=new HashMap<>();
        childUpdates.put(todo,"N");
        databaseReference.updateChildren(childUpdates);
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

                    Toast.makeText(getApplicationContext(), "방가방가 "+name+" 님", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                    Map<String,Object> chileUpdates=new HashMap<>();
                    Map<String,Object> postValues=null;

                        FirebasePost post=new FirebasePost(name, "1111");
                        postValues=post.toMap();

                    chileUpdates.put("/user_list/"+name,postValues);
                    databaseReference.updateChildren(chileUpdates);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);

    }

}

