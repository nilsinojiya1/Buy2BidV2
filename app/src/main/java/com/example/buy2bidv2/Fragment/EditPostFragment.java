package com.example.buy2bidv2.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.buy2bidv2.DashboardActivity;
import com.example.buy2bidv2.Model.Post;
import com.example.buy2bidv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditPostFragment extends Fragment {

    ImageView bidPostImage;
    EditText title,description,expectedPrice,lastDate;
    Button btnDelete,btnUpdate;
    String postId,strlastbid;
    DatePickerDialog picker;
    public EditPostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        Log.d("PostId",this.getArguments().getString("postId").toString());
        postId = this.getArguments().getString("postId");
        bidPostImage = view.findViewById(R.id.image);
        title = view.findViewById(R.id.postTitle);
        description = view.findViewById(R.id.description);
        expectedPrice=view.findViewById(R.id.expectedPrice);
        btnDelete=view.findViewById(R.id.btnDelete);
        btnUpdate=view.findViewById(R.id.btnUpdate);
        lastDate = view.findViewById(R.id.lastDate);

        lastDate.setShowSoftInputOnFocus(false);
        lastDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                picker = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String tempDay = null;
                                String tempMonth = null;
                                if(dayOfMonth<10)
                                {
                                    tempDay = "0"+dayOfMonth;
                                }
                                if((monthOfYear+1)<10)
                                {
                                    tempMonth = "0"+(monthOfYear+1);
                                }
                                else
                                {
                                    tempMonth = String.valueOf((monthOfYear+1));
                                }

                                lastDate.setText(tempDay + "/" + tempMonth + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                picker.show();
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                try {
                    Glide.with(view).load(post.getPostimage()).into(bidPostImage);
                    description.setText(post.getDescription());
                    title.setText(post.getPostTitle());
                    lastDate.setText(post.getLastDate());
                    expectedPrice.setText(post.getExpectedPrice());
                    Log.d("title",post.getPostTitle());
                    Log.d("description",post.getDescription().toString());
                }
                catch (NullPointerException e)
                {
                    Log.d("image",e.getMessage().toString());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DashboardActivity.class));

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.child("Posts").child(postId).removeValue();
                reference.child("Bids").child(postId).removeValue();
                reference.child("Votes").child(postId).removeValue();


            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Posts")
                        .child(postId);
                reference.child("postTitle").setValue(title.getText().toString().toUpperCase());
                reference.child("description").setValue(description.getText().toString());
                reference.child("expectedPrice").setValue(expectedPrice.getText().toString());
                reference.child("lastDate").setValue(lastDate.getText().toString());
                progressDialog.dismiss();
                startActivity(new Intent(getActivity(), DashboardActivity.class));
            }
        });
        return view;
    }

}
