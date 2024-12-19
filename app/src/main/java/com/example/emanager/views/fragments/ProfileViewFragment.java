package com.example.emanager.views.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.emanager.R;
import com.example.emanager.database.DatabaseHelper;

public class ProfileViewFragment extends Fragment {

    private TextView textViewName, textViewEmail;
    private Button btnLogout;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        textViewName = view.findViewById(R.id.nameAndSurname);
        textViewEmail = view.findViewById(R.id.EmailOrPhone1);
        btnLogout = view.findViewById(R.id.btnOut);

        // Загружаем данные пользователя
        loadUserData();

        btnLogout.setOnClickListener(v -> navigateToProfileFragment());

        return view;
    }

    private void loadUserData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        String email = prefs.getString("email", null); // Загружаем email

        if (email != null) {
            Cursor cursor = dbHelper.getUserData(email);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");

                if (nameIndex != -1) {
                    textViewName.setText("Имя: " + cursor.getString(nameIndex));
                }

                if (emailIndex != -1) {
                    textViewEmail.setText("Email: " + cursor.getString(emailIndex));
                }

                cursor.close();
            } else {
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Не удалось загрузить email", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToProfileFragment() {
        // Сбрасываем состояние пользователя
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("email");
        editor.apply();

        // Переход обратно к ProfileFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new ProfileFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}