package edu.skku.map.a2017311456_pp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Posts> mPost;
    private LayoutInflater mInflate;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Posts> mPost) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.post_list_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //데이터오 뷰를 바인딩
        holder.todo.setText(mPost.get(position).getTodo());
        //holder.check.setText(mPost.get(position).getCheck());
        if(mPost.get(position).getCheck().equals("Y")){
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.checkBox.isChecked()){
                    String name=mPost.get(position).getName();
                    String day=mPost.get(position).getDay();
                    String td=mPost.get(position).getTodo();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user_list").child(name).child("day").child(day);
                    Map<String,Object>childUpdates=new HashMap<>();
                    childUpdates.put(td,"Y");
                    databaseReference.updateChildren(childUpdates);
                }else{
                    String name=mPost.get(position).getName();
                    String day=mPost.get(position).getDay();
                    String td=mPost.get(position).getTodo();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user_list").child(name).child("day").child(day);
                    Map<String,Object>childUpdates=new HashMap<>();
                    childUpdates.put(td,"N");
                    databaseReference.updateChildren(childUpdates);
                }
            }
        });
        holder.todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTwitter(mPost.get(position).getTodo());
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click","눌렀");
                String name=mPost.get(position).getName();
                String day=mPost.get(position).getDay();
                String td=mPost.get(position).getTodo();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_list").child(name).child("day").child(day);
                ref.child(td).removeValue();
                Toast.makeText(mContext.getApplicationContext(),td+"가 삭제됩니다. 기다려주세여!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;
        public Button check;
        public CheckBox checkBox;
        public LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            todo=(TextView)itemView.findViewById(R.id.posttodo);
            check=(Button) itemView.findViewById(R.id.postcheck);
            checkBox=(CheckBox)itemView.findViewById(R.id.postbox);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearlist);
        }
    }
    public void shareTwitter(String a){
        String strLink=null;
        try{
            strLink=String.format("http://twitter.com/intent/tweet?text=%s",
                    URLEncoder.encode(a,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        mContext.startActivity(intent);
    }
    public void deletevalue(){

    }
}