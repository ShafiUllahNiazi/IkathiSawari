package com.example.shafi.ikathisawari;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpScreen extends AppCompatActivity {



    private TabLayout sign_up_tablayout;
    public ViewPager sign_up_viewPager;
    public Sign_Up_View_Pager_Adapter sign_up_view_pager_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        sign_up_tablayout=findViewById(R.id.sign_up_tablayout);
        sign_up_viewPager=findViewById(R.id.sign_up_viewPager);
        sign_up_view_pager_adapter=new Sign_Up_View_Pager_Adapter(getSupportFragmentManager());
        sign_up_view_pager_adapter.addFragment(new RiderSignUp(),"Rider");
        sign_up_view_pager_adapter.addFragment(new DriverSignUp(),"Driver");
        sign_up_viewPager.setAdapter(sign_up_view_pager_adapter);
        sign_up_tablayout.setupWithViewPager(sign_up_viewPager);
    }
}
