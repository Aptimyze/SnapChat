package com.example.sauhardpant.snapchat.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.sauhardpant.snapchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartUpActivity extends AppCompatActivity {

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_start_up_fragment_container, new SplashScreenFragment())
                .commit();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_start_up_fragment_container, new LoginOrRegistrationFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
