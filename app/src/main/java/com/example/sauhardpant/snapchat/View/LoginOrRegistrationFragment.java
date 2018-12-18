package com.example.sauhardpant.snapchat.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sauhardpant.snapchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginOrRegistrationFragment extends Fragment {

  @BindView(R.id.log_in_button)
  Button loginBtn;
  @BindView(R.id.sign_up_button)
  Button signUpBtn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_login_or_registration, container, false);
    ButterKnife.bind(this, rootView);

    loginBtn.setOnClickListener(click -> {
      if (getActivity() != null) {
        getActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_start_up_fragment_container, new LogInFragment())
            .commit();
      }
    });

    signUpBtn.setOnClickListener(click -> {
      if (getActivity() != null) {
        getActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_start_up_fragment_container, new SignUpFragment())
            .commit();
      }
    });

    return rootView;
  }
}
