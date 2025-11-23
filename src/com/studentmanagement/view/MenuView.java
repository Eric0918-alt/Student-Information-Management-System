package com.studentmanagement.view;

import com.studentmanagement.entity.Student;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.UserService;
import com.studentmanagement.util.InputUtil;

import java.util.List;

public class MenuView {
    private UserService userService = new UserService();
    private StudentService studentService = new StudentService();
    private boolean isLogin = false; // 登录状态标记

    // 显示一级菜单
    public void showFirstMenu() {
        while (true) {
            System.out.println("\n===== 一级菜单 =====");
            System.out.println("1. 登录");
            System.out.println("2. 注册系统使用人员（需超管）");
            System.out.println("3. 超管解锁账号");
            System.out.println("4. 退出");
            int choice = InputUtil.getInt("请选择功能: ");

            switch (choice) {
                case 1:
                    handleLogin();
                    if (isLogin) {
                        showSecondMenu(); // 登录成功进入二级菜单
                    }
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    handleUnlock();
                    break;
                case 4:
                    System.out.println("系统退出，再见!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效选择，请重新输入!");
            }
        }
    }

    // 显示二级菜单（学生管理）
    private void showSecondMenu() {
        while (isLogin) {
            System.out.println("\n===== 二级菜单 =====");
            System.out.println("1. 查询所有学生");
            System.out.println("2. 输入学号查询单个学生");
            System.out.println("3. 输入学号修改学生信息");
            System.out.println("4. 输入学号删除学生信息");
            System.out.println("5. 添加学生");
            System.out.println("6. 退出登录");
            int choice = InputUtil.getInt("请选择功能: ");

            switch (choice) {
                case 1:
                    queryAllStudents();
                    break;
                case 2:
                    queryStudentBySid();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    addStudent();
                    break;
                case 6:
                    isLogin = false;
                    System.out.println("已退出登录!");
                    break;
                default:
                    System.out.println("无效选择，请重新输入!");
            }
        }
    }

    // 处理登录
    private void handleLogin() {
        String username = InputUtil.getString("请输入用户名: ");
        // --- 新增代码 START ---
        // 1. 在输入密码前，先检查是否锁定
        if (userService.isUserLocked(username)) {
            System.out.println("账号已锁定，请联系管理员!");
            return; // 直接结束方法，不再执行下面的 while 循环
        }
        // --- 新增代码 END ---
        int failCount = 0;
        while (failCount < 3) {
            String password = InputUtil.getPassword("请输入密码: ");
            if (userService.login(username, password)) {
                isLogin = true;
                return;
            }
            failCount++;
            System.out.println("密码错误，剩余重试次数: " + (3 - failCount));
        }
        // 当循环结束（即 failCount == 3）时，锁定账号
        userService.handleLoginFailure(username);
    }

    // 处理注册
    private void handleRegister() {
        String adminUser = InputUtil.getString("请输入超管用户名: ");
        String adminPwd = InputUtil.getPassword("请输入超管密码: ");
        // 先验证超级管理员是否正确
        if (!userService.verifyAdmin(adminUser, adminPwd)) {
            System.out.println("超管身份验证失败，无注册权限!");
            return;
        }
        // 验证通过再输入新账户信息
        String newUser = InputUtil.getString("请输入新用户名: ");
        String newPwd = InputUtil.getPassword("请输入新密码: ");
        userService.register(adminUser, adminPwd, newUser, newPwd);
    }

    // 处理解锁
    private void handleUnlock() {
        String adminUser = InputUtil.getString("请输入超管用户名: ");
        String adminPwd = InputUtil.getPassword("请输入超管密码: ");
        // 1. 先只验证超管身份，避免进入循环后再验证
        if (!userService.verifyAdmin(adminUser, adminPwd)) {
            System.out.println("超管身份验证失败，无法进行解锁操作!");
            return; // 身份不对，直接结束
        }

        System.out.println(">> 超管身份验证通过，已进入批量解锁模式 <<");

        // 2. 验证通过后，进入循环
        while (true) {
            System.out.println("--------------------------------");
            // 提示用户怎么退出
            String username = InputUtil.getString("请输入需要解锁的用户名 (输入 exit 退出): ");

            // 3. 设置退出条件
            if ("exit".equalsIgnoreCase(username)) {
                System.out.println("退出解锁模式。");
                break;
            }

            // 4. 执行解锁
            // 注意：虽然 unlockAccount 内部会再次校验超管密码，但因为我们前面验证过了且没改变量，所以肯定通过
            boolean success = userService.unlockAccount(adminUser, adminPwd, username);

            // 无论成功还是失败，循环都不会结束，用户可以立即输入下一个，或者重输刚才输错的
        }
    }

    // 查询所有学生
    private void queryAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("暂无学生信息!");
            return;
        }
        System.out.println("学号-姓名-年龄-居住地");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    // 按学号查询学生
    private void queryStudentBySid() {
        String sid = InputUtil.getString("请输入学号: ");
        Student student = studentService.getStudentBySid(sid);
        if (student != null) {
            System.out.println("查询结果: " + student);
        } else {
            System.out.println("未找到该学生!");
        }
    }

    // 添加学生
    private void addStudent() {
        String sid = InputUtil.getString("请输入学号: ");
        String name = InputUtil.getString("请输入姓名: ");
        int age = InputUtil.getInt("请输入年龄: ");
        String address = InputUtil.getString("请输入居住地: ");
        studentService.addStudent(new Student(sid, name, age, address));
    }

    // 修改学生
    private void updateStudent() {
        String sid = InputUtil.getString("请输入要修改的学生学号: ");
        Student student = studentService.getStudentBySid(sid);
        if (student == null) {
            System.out.println("未找到该学生!");
            return;
        }
        String name = InputUtil.getString("请输入新姓名(" + student.getName() + "): ");
        int age = InputUtil.getInt("请输入新年龄(" + student.getAge() + "): ");
        String address = InputUtil.getString("请输入新居住地(" + student.getAddress() + "): ");
        // 保持学号不变
        studentService.updateStudent(new Student(sid, name, age, address));
    }

    // 删除学生
    private void deleteStudent() {
        String sid = InputUtil.getString("请输入要删除的学生学号: ");
        studentService.deleteStudent(sid);
    }
}