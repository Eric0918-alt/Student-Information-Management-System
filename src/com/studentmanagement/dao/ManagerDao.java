package com.studentmanagement.dao;

import com.studentmanagement.entity.Manager;
import com.studentmanagement.util.Constant;
import com.studentmanagement.util.IOUtil;

import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    // 初始化超管账号（首次运行时）
    public void initAdmin() {
        if (getManagerByUsername(Constant.ADMIN_USERNAME) == null) {
            Manager admin = new Manager(
                    Constant.ADMIN_USERNAME,
                    Constant.ADMIN_PASSWORD,
                    false
            );
            addManager(admin);
        }
    }

    // 获取所有管理员
    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        List<String> lines = IOUtil.readFile(Constant.MANAGER_FILE);

        for (String line : lines) {
            String[] parts = line.split("-");
            if (parts.length == 3) {
                managers.add(new Manager(
                        parts[0],
                        parts[1],
                        Boolean.parseBoolean(parts[2])
                ));
            }
        }
        return managers;
    }

    // 根据用户名查询管理员
    public Manager getManagerByUsername(String username) {
        for (Manager m : getAllManagers()) {
            if (m.getUsername().equals(username)) {
                return m;
            }
        }
        return null;
    }

    // 添加管理员（用户名不重复返回true）
    public boolean addManager(Manager manager) {
        if (getManagerByUsername(manager.getUsername()) != null) {
            return false; // 用户名重复
        }
        List<Manager> managers = getAllManagers();
        managers.add(manager);
        saveAllManagers(managers);
        return true;
    }

    // 更新管理员信息（如锁定状态）
    public void updateManager(Manager manager) {
        List<Manager> managers = getAllManagers();
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getUsername().equals(manager.getUsername())) {
                managers.set(i, manager);
                saveAllManagers(managers);
                return;
            }
        }
    }

    // 解锁账号
    public boolean unlockAccount(String username) {
        Manager manager = getManagerByUsername(username);
        if (manager == null) {
            return false;
        }
        manager.setLocked(false);
        updateManager(manager);
        return true;
    }

    // 保存所有管理员到文件
    private void saveAllManagers(List<Manager> managers) {
        List<String> lines = new ArrayList<>();
        for (Manager m : managers) {
            lines.add(m.toString());
        }
        IOUtil.writeFile(Constant.MANAGER_FILE, lines);
    }
}