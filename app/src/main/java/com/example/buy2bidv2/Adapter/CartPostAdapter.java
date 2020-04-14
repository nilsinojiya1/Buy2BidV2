package com.example.buy2bidv2.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buy2bidv2.Fragment.BidFragment;
import com.example.buy2bidv2.Model.Post;
import com.example.buy2bidv2.Model.User;
import com.example.buy2bidv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CartPostAdapter extends RecyclerView.Adapter <CartPostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPost;
    private BidFragment bidFragment;

    private FirebaseUser firebaseUser;

    public CartPostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_post_item,parent,false);

        return new CartPostAdapter.ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final CartPostAdapter.ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);
        holder.title.setText(post.getPostTitle());
        holder.expectedPrice.setText("~RS."+post.getExpectedPrice());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        holder.days.setText(String.valueOf(post.days(sdf.format(calendar.getTime()))));
        //Log.d("title",post.getExpectedPrice());

        if(post.getDescription().equals(""))
        {
            holder.description.setVisibility(View.GONE);
        }
        else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());

        }
        publisherInfo(holder.publisher,post.getPublisher());

        isLikes(post.getPostId(),holder.vote);
        nrLikes(holder.votes,post.getPostId());
        isAddedToCart(post.getPostId(),holder.cartbtn);
        checkForBid(post.getPostId(),holder.lastBid);
        tempLastBid(post.getPostId(),holder.lastBid);

        holder.cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cartbtn","clicked");
                if(!holder.cartbtn.getTag().toString().equals("Add"))
                {
        /*FirebaseDatabase.getInstance().getReference().child("Carts").child(firebaseUser.getUid())
        .child(post.getPostId()).setValue(true);*/
                    FirebaseDatabase.getInstance().getReference().child("Carts").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
        /*else
        {
        FirebaseDatabase.getInstance().getReference().child("Carts").child(firebaseUser.getUid())
        .child(post.getPostId()).removeValue();
        }*/
            }
        });

        holder.btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("btnbid",post.getPostId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BidFragment();
                Bundle bundle = new Bundle();
                bundle.putString("postId",post.getPostId());
                bundle.putString("imageUrl",post.getPostimage());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

            }
        });
        holder.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.vote.getTag().equals("vote"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Votes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Votes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView post_image,vote;
        public Button cartbtn,btnbid;
        public TextView title,votes,description,expectedPrice,publisher,lastBid,days;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image=itemView.findViewById(R.id.post_Image);
            vote=itemView.findViewById(R.id.vote);
            title=itemView.findViewById(R.id.post_Title);
            votes=itemView.findViewById(R.id.votes);
            description=itemView.findViewById(R.id.description);
            publisher=itemView.findViewById(R.id.publisher);
            expectedPrice=itemView.findViewById(R.id.expectedPrice);
            btnbid= itemView.findViewById(R.id.btnbid);
            cartbtn=itemView.findViewById(R.id.cartbtn);
            bidFragment=new BidFragment();
            lastBid = itemView.findViewById(R.id.lastbid);
            days = itemView.findViewById(R.id.days2);
        }
    }
    private void isAddedToCart(final String postid, final Button cartbtn)
    {
        Log.d("cartFun","called");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Carts")
                .child(firebaseUser.getUid());
        //.child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).exists())
                {
                    cartbtn.setText("Remove");
                    cartbtn.setTag("Added");
                }
                /*else
                {
                    cartbtn.setTag("Add");
                    cartbtn.setVisibility(View.GONE);
                    cartbtn.setText("Add To Cart");

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void isLikes(String postid, final ImageView imageView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Votes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_voted);
                    imageView.setTag("votes");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_vote);
                    imageView.setTag("vote");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    if(!dataSnapshot1.exists())
                {
                    lastBid.setText("RS. 0.0");
                }
                else {
                    Log.d("TempLastBid", dataSnapshot1.getKey());
                    lastBid.setText(dataSnapshot1.getKey());
                }
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
                /*.orderByKey()
                .limitToLast(1);*/
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    lastBid.setText("RS. 0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Votes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" votes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void publisherInfo(final TextView publisher, final String userid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                publisher.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}