package com.example.emanager.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.emanager.R;
import com.example.emanager.database.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editEmail = view.findViewById(R.id.editEmailOrPhone);
        editPassword = view.findViewById(R.id.editPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> navigateToRegister());

        return view;
    }

    private void loginUser() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValid = dbHelper.loginUser(email, password);
        if (isValid) {
            // Сохраняем email и состояние пользователя
            SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("email", email); // Сохраняем email
            editor.apply();

            // Переход к ProfileViewFragment
            navigateToProfileView(email);
        } else {
            Toast.makeText(getActivity(), "Неверный email или пароль", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToRegister() {
        RegisterProfilFragment registerFragment = new RegisterProfilFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToProfileView(String email) {
        ProfileViewFragment profileViewFragment = new ProfileViewFragment();

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        profileViewFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, profileViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}