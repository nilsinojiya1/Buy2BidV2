package com.example.buy2bidv2.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buy2bidv2.Adapter.CartPostAdapter;
import com.example.buy2bidv2.Model.Post;
import com.example.buy2bidv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartPostAdapter cartPostAdapter;
    private List<Post> postLists;
    private List<String> inCartList;
    private Button btnbid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        btnbid=view.findViewById(R.id.btnbid);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.startSmoothScroll();
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        cartPostAdapter = new CartPostAdapter(getContext(),postLists);
        recyclerView.setAdapter(cartPostAdapter);

        checkInCart();

        return view;
    }
    private void checkInCart(){
        inCartList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Carts")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inCartList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    inCartList.add(snapshot.getKey());
                   // Log.d("key","key");
                }
                Log.d("cart",inCartList.toString());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts()
    {
       // Log.d("readpost","this is a read post");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postLists.clear();
                for (DataSnapshot  snapshot : dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    for(String id: inCartList) {
                        //Log.d("innerfor","in for loop");
                        if(post.getPostId().equals(id))
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar calendar = Calendar.getInstance();
                            long day = post.days(sdf.format(calendar.getTime()));

                            Log.d("days", String.valueOf(day));
                            if(day<0)
                            {
                                continue;
                            } else {
                                postLists.add(post);
                            }
                        }
                    }

                }
                cartPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
