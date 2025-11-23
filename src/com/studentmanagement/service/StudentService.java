package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDao;
import com.studentmanagement.entity.Student;

import java.util.List;

public class StudentService {
    private StudentDao studentDao = new StudentDao();

    // 查询所有学生
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    // 根据学号查询学生
    public Student getStudentBySid(String sid) {
        return studentDao.getStudentBySid(sid);
    }

    // 添加学生
    public boolean addStudent(Student student) {
        if (studentDao.addStudent(student)) {
            System.out.println("添加成功!");
            return true;
        } else {
            System.out.println("学号已存在，添加失败!");
            return false;
        }
    }

    // 修改学生信息（不修改学号）
    public boolean updateStudent(Student student) {
        if (studentDao.updateStudent(student)) {
            System.out.println("修改成功!");
            return true;
        } else {
            System.out.println("学号不存在，修改失败!");
            return false;
        }
    }

    // 删除学生
    public boolean deleteStudent(String sid) {
        if (studentDao.deleteStudent(sid)) {
            System.out.println("删除成功!");
            return true;
        } else {
            System.out.println("学号不存在，删除失败!");
            return false;
        }
    }
}