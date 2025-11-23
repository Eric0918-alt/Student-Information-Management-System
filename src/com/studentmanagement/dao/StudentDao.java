package com.studentmanagement.dao;

import com.studentmanagement.entity.Student;
import com.studentmanagement.util.Constant;
import com.studentmanagement.util.IOUtil;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    // 获取所有学生
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = IOUtil.readFile(Constant.STUDENT_FILE);

        for (String line : lines) {
            String[] parts = line.split("-");
            if (parts.length == 4) {
                try {
                    students.add(new Student(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            parts[3]
                    ));
                } catch (NumberFormatException e) {
                    System.out.println("数据格式错误: " + line);
                }
            }
        }
        return students;
    }

    // 根据学号查询学生
    public Student getStudentBySid(String sid) {
        for (Student s : getAllStudents()) {
            if (s.getSid().equals(sid)) {
                return s;
            }
        }
        return null;
    }

    // 添加学生（学号不重复返回true）
    public boolean addStudent(Student student) {
        if (getStudentBySid(student.getSid()) != null) {
            return false; // 学号重复
        }
        List<Student> students = getAllStudents();
        students.add(student);
        saveAllStudents(students);
        return true;
    }

    // 更新学生信息（不修改学号）
    public boolean updateStudent(Student student) {
        List<Student> students = getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getSid().equals(student.getSid())) {
                students.set(i, student);
                saveAllStudents(students);
                return true;
            }
        }
        return false; // 未找到该学生
    }

    // 删除学生
    public boolean deleteStudent(String sid) {
        List<Student> students = getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getSid().equals(sid)) {
                students.remove(i);
                saveAllStudents(students);
                return true;
            }
        }
        return false; // 未找到该学生
    }

    // 保存所有学生到文件
    private void saveAllStudents(List<Student> students) {
        List<String> lines = new ArrayList<>();
        for (Student s : students) {
            lines.add(s.toString());
        }
        IOUtil.writeFile(Constant.STUDENT_FILE, lines);
    }
}