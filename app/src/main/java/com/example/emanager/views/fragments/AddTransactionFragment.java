package com.example.emanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.emanager.R;
import com.example.emanager.adapters.AccountsAdapter;
import com.example.emanager.adapters.CategoryAdapter;
import com.example.emanager.databinding.FragmentAddTransactionBinding;
import com.example.emanager.databinding.ListDialogBinding;
import com.example.emanager.models.Account;
import com.example.emanager.models.Transaction;
import com.example.emanager.utils.Constants;
import com.example.emanager.utils.Helper;
import com.example.emanager.views.activites.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

            transaction.setType(Constants.INCOME);

            binding.category.setOnClickListener(c -> {
                ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
                AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
                categoryDialog.setView(dialogBinding.getRoot());

                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.IncomeCategories, category -> {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                });
                dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                dialogBinding.recyclerView.setAdapter(categoryAdapter);
                categoryDialog.show();
            });
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));

            transaction.setType(Constants.EXPENSE);

            binding.category.setOnClickListener(c -> {
                ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
                AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
                categoryDialog.setView(dialogBinding.getRoot());

                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.ExpenceCategories, category -> {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                });
                dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                dialogBinding.recyclerView.setAdapter(categoryAdapter);
                categoryDialog.show();
            });
        });

        // Исправленный блок для выбора даты с DatePickerDialog
        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);

                String dateToShow = Helper.formatDate(calendar.getTime());
                binding.date.setText(dateToShow);
                transaction.setDate(calendar.getTime());
                transaction.setId(calendar.getTime().getTime());
            });
            datePickerDialog.show();
        });

        binding.account.setOnClickListener(c -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(1000.0, "Наличные"));
            accounts.add(new Account(5000.0, "Карта"));
            accounts.add(new Account(200.0, "ЭЛКАРТ"));
            accounts.add(new Account(1500.0, "MBANK"));
            accounts.add(new Account(0.0, "Другое"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, account -> {
                binding.account.setText(account.getAccountName());
                transaction.setAccount(account.getAccountName());
                accountsDialog.dismiss();
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);
            accountsDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(c -> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();

            if (transaction.getType().equals(Constants.EXPENSE)) {
                double accountBalance = getAccountBalance(transaction.getAccount());
                if (accountBalance < amount) {
                    // Если средств недостаточно
                    Toast.makeText(getContext(), "Недостаточно средств на счете!", Toast.LENGTH_SHORT).show();
                    return; // Прерываем выполнение метода
                } else {
                    transaction.setAmount(amount * -1);
                }
            } else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            // Обновляем баланс выбранного счета
            String accountName = transaction.getAccount();
            double newBalance = getAccountBalance(accountName) + transaction.getAmount();
            AccountFragment.AccountBackend.updateAccountBalance(accountName, newBalance);

            ((MainActivity)getActivity()).viewModel.addTransaction(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });

        return binding.getRoot();
    }

    private double getAccountBalance(String accountName) {
        for (Account account : AccountFragment.AccountBackend.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                return account.getAccountAmount();
            }
        }
        return 0.0; // Если счет не найден, возвращаем 0.
    }
}