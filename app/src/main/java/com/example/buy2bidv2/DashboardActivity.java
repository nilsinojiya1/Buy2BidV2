package com.example.buy2bidv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.buy2bidv2.Fragment.CartFragment;
import com.example.buy2bidv2.Fragment.HomeFragment;
import com.example.buy2bidv2.Fragment.ProfileFragment;
import com.example.buy2bidv2.Fragment.SearchFragment;
import com.example.buy2bidv2.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Fragment selectedFragment = null;
    LinearLayout linearLayout;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private CartFragment cartFragment;
    private ProfileFragment profileFragment;

    final String zero="0";

    FirebaseAuth auth;

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.side_manu);
        popup.show();
    }

    @Override
   public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                //Log.i("about","clicked");
                return true;
            case R.id.contact:
               // Log.i("contact","clicked");
                return true;
            case R.id.logout:
                auth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                //Log.i("logout","clicked");
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout=findViewById(R.id.framlayout);
        linearLayout=findViewById(R.id.layout_coming_soon);
        homeFragment=new HomeFragment();
        searchFragment = new SearchFragment();
        cartFragment = new CartFragment();
        profileFragment = new ProfileFragment();
        //checkactiveFlag();
        InitializaFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                        InitializaFragment(homeFragment);
                        return true;
                        //break;
                    case R.id.nav_search:
                        InitializaFragment(searchFragment);
                        return true;
                        //break;
                    case R.id.nav_addItem:
                        startActivity(new Intent(DashboardActivity.this,PostActivity.class));
                        return true;
                        //break;
                    case R.id.nav_cart:
                        InitializaFragment(cartFragment);
                        return true;
                        //break;
                    case R.id.nav_profile:
                        InitializaFragment(profileFragment);
                        SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                        editor.putString("profileid",FirebaseAuth.getInstance().getUid());
                        editor.apply();
                        return true;
                        //break;
                }
                return false;
            }
        });
    }
    private void InitializaFragment(Fragment fragment)
    {
        checkactiveFlag();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout,fragment);
        fragmentTransaction.commit();
    }
    void checkactiveFlag()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Log.d("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //activeFlag=user.getactiveFlag();
                Log.d("activFlag",user.getactiveFlag());
                if(user.getactiveFlag().equals(zero))
                {
                    linearLayout.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.GONE);
                }
                else
                {
                    linearLayout.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
