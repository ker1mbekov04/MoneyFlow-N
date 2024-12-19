package com.example.emanager.utils;

import com.example.emanager.R;
import com.example.emanager.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static ArrayList<Category> IncomeCategories;
    public static ArrayList<Category> ExpenceCategories;

    public static int DAILY = 0;
    public static int MONTHLY = 1;


    public static int SELECTED_TAB = 0;
    public static int SELECTED_TAB_STATS = 0;
    public static String SELECTED_STATS_TYPE = Constants.INCOME;


    public static void  setCategories() {
        IncomeCategories = new ArrayList<>();
        IncomeCategories.add(new Category("Зарплата",R.drawable.ic_salary, R.color.category1));
        IncomeCategories.add(new Category("Бизнес",R.drawable.ic_business, R.color.category2));
        IncomeCategories.add(new Category("Инвестиции",R.drawable.ic_investment,R.color.category3));
        IncomeCategories.add(new Category("Кредит",R.drawable.ic_loan,R.color.category4));
        IncomeCategories.add(new Category("Аренда",R.drawable.ic_rent,R.color.category5));
        IncomeCategories.add(new Category("Другое",R.drawable.ic_other,R.color.category6));

        ExpenceCategories = new ArrayList<>();
        ExpenceCategories.add(new Category("Продукты",R.drawable.ic_product,R.color.category1));
        ExpenceCategories.add(new Category("Одежда",R.drawable.ic_shirt,R.color.category6));
        ExpenceCategories.add(new Category("Машина",R.drawable.ic_car,R.color.category3));
        ExpenceCategories.add(new Category("Камуналка",R.drawable.ic_home,R.color.category4));
        ExpenceCategories.add(new Category("Питомцы",R.drawable.ic_pet,R.color.category5));
        ExpenceCategories.add(new Category("Игры",R.drawable.game_ic,R.color.category2));

    }


    public static Category getIncomeCategoryDetails(String categoryName) {
        for (Category cat : IncomeCategories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public static Category getExpenseCategoryDetails(String categoryName) {
        for (Category cat : ExpenceCategories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }


    public static int getAccountsColor(String accountName) {
        switch (accountName) {
            case "Карта":
                return R.color.bank_color;
            case "Наличные":
                return R.color.category4;
            case "MBANK":
                return R.color.greenColor;
            case "ЭЛКАРТ":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
    }

}
