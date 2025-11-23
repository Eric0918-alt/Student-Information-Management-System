package com.studentmanagement.main;

import com.studentmanagement.dao.ManagerDao;
import com.studentmanagement.view.MenuView;

public class Main {
    public static void main(String[] args) {
        new ManagerDao().initAdmin();
        new MenuView().showFirstMenu();
    }
}