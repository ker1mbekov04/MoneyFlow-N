package com.example.emanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emanager.R;
import com.example.emanager.databinding.RowTransactionBinding;
import com.example.emanager.models.Category;
import com.example.emanager.models.Transaction;
import com.example.emanager.utils.Constants;
import com.example.emanager.utils.Helper;
import com.example.emanager.views.activites.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter  extends  RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {


    Context context;
    RealmResults<Transaction> transactions;


    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLbl.setText(transaction.getAccount());

        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionIncome = Constants.getIncomeCategoryDetails(transaction.getCategory());
        Category transactionExpense = Constants.getExpenseCategoryDetails(transaction.getCategory());

        if (transactionIncome != null) {
            holder.binding.categoryIcon.setImageResource(transactionIncome.getCategoryImage());
            holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionIncome.getCategoryColor()));
        } else if (transactionExpense != null) {
            holder.binding.categoryIcon.setImageResource(transactionExpense.getCategoryImage());
            holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionExpense.getCategoryColor()));
        }

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if (transaction.getType().equals(Constants.INCOME)) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));
        } else if (transaction.getType().equals(Constants.EXPENSE)) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));
        }
        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Удалить Транзакцию");
                deleteDialog.setMessage("Вы уверены, что хотите удалите эту транзакцию?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Да", (dialogInterface, i) -> {
                    ((MainActivity)context).viewModel.deleteTransaction(transaction);
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Нет", (dialogInterface, i) -> {
                    deleteDialog.dismiss();
                });
                deleteDialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
