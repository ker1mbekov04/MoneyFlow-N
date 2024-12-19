package com.example.emanager.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.emanager.R;
import com.example.emanager.models.Account;  // Используем класс Account из models

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    public static class AccountBackend {
        private static List<Account> accounts;

        static {
            accounts = new ArrayList<>();
            accounts.add(new Account(1000.0, "Наличные"));
            accounts.add(new Account(5000.0, "Карта"));
            accounts.add(new Account(200.0, "ЭЛКАРТ"));
            accounts.add(new Account(1500.0, "MBANK"));
        }

        public static List<Account> getAccounts() {
            return accounts;
        }

        public static void updateAccountBalance(String accountName, double newBalance) {
            for (Account account : accounts) {
                if (account.getAccountName().equals(accountName)) {
                    account.setAccountAmount(newBalance);
                    return;
                }
            }
        }

        public static double getTotalBalance() {
            double total = 0.0;
            for (Account account : accounts) {
                total += account.getAccountAmount();
            }
            return total;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Account> accounts = AccountBackend.getAccounts();

        TextView balanceCashTextView = view.findViewById(R.id.sum);
        TextView balanceCardTextView = view.findViewById(R.id.textView9);
        TextView balancePayTmTextView = view.findViewById(R.id.textView13);
        TextView balancePayPalTextView = view.findViewById(R.id.paypal);
        TextView totalBalanceTextView = view.findViewById(R.id.totalBalance);

        balanceCashTextView.setText(String.valueOf(accounts.get(0).getAccountAmount()));
        balanceCardTextView.setText(String.valueOf(accounts.get(1).getAccountAmount()));
        balancePayTmTextView.setText(String.valueOf(accounts.get(2).getAccountAmount()));
        balancePayPalTextView.setText(String.valueOf(accounts.get(3).getAccountAmount()));

        totalBalanceTextView.setText(String.valueOf(AccountBackend.getTotalBalance()));
    }
}
