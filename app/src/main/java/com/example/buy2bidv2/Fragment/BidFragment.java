package com.example.buy2bidv2.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.buy2bidv2.Model.Post;
import com.example.buy2bidv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BidFragment extends Fragment {

    ImageView bidPostImage;
    TextView bidPostTitle,bidPostDescription,lastBid;
    EditText etBidAmount;
    String postId,strlastbid;
    Button bid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_bid, container, false);
       postId = this.getArguments().getString("postId");
       //Log.d("stringget",postId);

        bidPostImage = view.findViewById(R.id.bid_post_Image);
        bidPostTitle = view.findViewById(R.id.bid_post_Title);
        bidPostDescription = view.findViewById(R.id.bid_description);
        etBidAmount=view.findViewById(R.id.bidamount);
        bid=view.findViewById(R.id.btnbid);
        lastBid=view.findViewById(R.id.lastbid);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Post post = dataSnapshot.getValue(Post.class);
                    Glide.with(view).load(post.getPostimage()).into(bidPostImage);
                    bidPostDescription.setText(post.getDescription());
                    bidPostTitle.setText(post.getPostTitle());
                    Log.d("title", post.getPostTitle());
                    Log.d("description", post.getDescription().toString());
                }
                catch(Exception e)
                {
                    Log.d("Exception",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etBidAmount.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Amount", Toast.LENGTH_SHORT).show();
                }
                else if(strlastbid == null)
                {
                    etBidAmount.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bids").child(postId);
                    reference.child(etBidAmount.getText().toString()).setValue(FirebaseAuth.getInstance().getUid());
                    etBidAmount.setText("");

                }
                else {
                    Integer integerLastBid = Integer.parseInt(strlastbid);
                    Integer currentBid = Integer.parseInt(etBidAmount.getText().toString());
                    int templastbid1 = integerLastBid.intValue();
                    int bid = currentBid.intValue();
                    if (bid <= templastbid1) {
                        Toast.makeText(getActivity(), "Enter Bid Amount greater then" + lastBid.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        etBidAmount.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bids").child(postId);
                        reference.child(etBidAmount.getText().toString()).setValue(FirebaseAuth.getInstance().getUid());
                        etBidAmount.setText("");
                    }
                }
            }
        });
        checkForBid(postId,lastBid);
        tempLastBid(postId,lastBid);
        return view;
    }
    private void tempLastBid(final String postId, final TextView lastBid)
    {
        Query query = FirebaseDatabase.getInstance().getReference("Bids").child(postId).orderByKey()
                .limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                        Log.d("TempLastBid", dataSnapshot1.getKey());
                        lastBid.setText(dataSnapshot1.getKey());
                        strlastbid = dataSnapshot1.getKey();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkForBid(String postId, final TextView lastBid)
    {
        final DatabaseReference reference =  FirebaseDatabase.getInstance().getReference()
                .child("Bids")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    lastBid.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
