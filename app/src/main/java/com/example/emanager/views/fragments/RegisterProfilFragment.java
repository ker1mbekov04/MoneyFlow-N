package com.example.emanager.views.fragments;

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

public class RegisterProfilFragment extends Fragment {

    private EditText editName, editEmail, editPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_profil, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editName = view.findViewById(R.id.editnameAndSurname);
        editEmail = view.findViewById(R.id.editEmailOrPhone1);
        editPassword = view.findViewById(R.id.editPassword1);
        btnRegister = view.findViewById(R.id.btnRegister1);

        btnRegister.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isRegistered = dbHelper.registerUser(name, email, password);
        if (isRegistered) {
            Toast.makeText(getActivity(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            navigateToProfileFragment();
        } else {
            Toast.makeText(getActivity(), "Ошибка регистрации, возможно, email уже существует", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToProfileFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new ProfileFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}