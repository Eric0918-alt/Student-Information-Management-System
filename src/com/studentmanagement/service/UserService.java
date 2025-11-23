package com.studentmanagement.service;

import com.studentmanagement.dao.ManagerDao;
import com.studentmanagement.entity.Manager;
import com.studentmanagement.util.Constant;

public class UserService {
    private ManagerDao managerDao = new ManagerDao();

    // 登录验证（返回是否成功）
    public boolean login(String username, String password) {
        Manager manager = managerDao.getManagerByUsername(username);
        if (manager == null) {
            System.out.println("用户名不存在!");
            return false;
        }
        if (manager.isLocked()) {
            System.out.println("账号已锁定，请联系管理员!");
            return false;
        }
        if (manager.getPassword().equals(password)) {
            System.out.println("登录成功!");
            return true;
        } else {
            System.out.println("密码错误!");
            return false;
        }
    }

    // 处理登录失败逻辑（累计3次锁定）
    public boolean handleLoginFailure(String username) {
        Manager manager = managerDao.getManagerByUsername(username);
        if (manager == null) return false;

        // 简化版：每次失败直接锁定（实际应累计次数，这里简化为3次逻辑）
        manager.setLocked(true);
        managerDao.updateManager(manager);
        System.out.println("密码错误3次，账号已锁定!");
        return true;
    }

    // 注册新用户（需超管权限）
    public boolean register(String adminUsername, String adminPassword, String newUsername, String newPassword) {
        // 验证超管身份
        if (!adminUsername.equals(Constant.ADMIN_USERNAME) ||
                !managerDao.getManagerByUsername(adminUsername).getPassword().equals(adminPassword)) {
            System.out.println("超管身份验证失败，无注册权限!");
            return false;
        }
        // 添加新管理员
        Manager newManager = new Manager(newUsername, newPassword, false);
        if (managerDao.addManager(newManager)) {
            System.out.println("注册成功!");
            return true;
        } else {
            System.out.println("用户名已存在，注册失败!");
            return false;
        }
    }

    // 解锁账号（需超管权限）
    public boolean unlockAccount(String adminUsername, String adminPassword, String username) {
        if (!adminUsername.equals(Constant.ADMIN_USERNAME) ||
                !managerDao.getManagerByUsername(adminUsername).getPassword().equals(adminPassword)) {
            System.out.println("超管身份验证失败，无解锁权限!");
            return false;
        }
        if (managerDao.unlockAccount(username)) {
            System.out.println("账号解锁成功!");
            return true;
        } else {
            System.out.println("解锁失败，用户名不存在!");
            return false;
        }
    }
}