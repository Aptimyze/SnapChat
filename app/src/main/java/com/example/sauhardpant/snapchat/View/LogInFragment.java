package com.example.sauhardpant.snapchat.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sauhardpant.snapchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInFragment extends Fragment {

  private FirebaseAuth mAuth;

  @BindView(R.id.fragment_log_in_email_et)
  EditText email;
  @BindView(R.id.fragment_log_in_password_et)
  EditText password;
  @BindView(R.id.fragment_log_in_submit_btn)
  Button loginBtn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
    ButterKnife.bind(this, rootView);

    mAuth = FirebaseAuth.getInstance();

    loginBtn.setOnClickListener(click -> {
      submitForm();
    });

    return rootView;
  }

  private void submitForm() {
    if (isFormValid()) {
      mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
          .addOnCompleteListener(logedIn -> {
            if (logedIn.isSuccessful()) {
              Intent intent = new Intent(getContext(), MainActivity.class);
              startActivity(intent);
            } else {
              Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
          });
    } else {
      Toast.makeText(getContext(), "Email or password cannot be empty", Toast.LENGTH_SHORT)
          .show();
    }
  }

  private boolean isFormValid() {
    return !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty();
  }
}
