package edu.skku.map.a2017311456_pp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MondayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MondayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerViewAdapter adapter;
    String name="";
    String todo="";
    String check="";
    private ArrayList<Posts> posts=new ArrayList<>();

    public MondayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MondayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MondayFragment newInstance(String param1, String param2) {
        MondayFragment fragment = new MondayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        name=getArguments().getString("id");
        // Inflate the layout for this fragment
        Log.e("name",name);
        View view= inflater.inflate(R.layout.fragment_monday, container, false);

        initDataset();

        Context context=view.getContext();
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.monday_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new RecyclerViewAdapter(context,posts);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();


    }

    public void initDataset(){
        posts.clear();
        DatabaseReference pref= FirebaseDatabase.getInstance().getReference("user_list").child(name).child("day").child("Mon");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){

                    todo=data.getKey().toString();
                    Log.d("todo",todo);
                    check=data.getValue().toString();
                    Log.d("check",check);
                    posts.add(new Posts(todo,check,name,"Mon"));
                    reset();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        pref.addListenerForSingleValueEvent(valueEventListener);
    }
    private void reset(){
        todo="";
        check="";
    }
}
