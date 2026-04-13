package services;

import models.Student;
import java.util.List;

public class AuthService {
    
    public static boolean authenticate(String userId, String password, String role) throws Exception {
        if (role.equals("STUDENT")) {
            List<Student> students = FileHandler.loadStudents();
            for (Student s : students) {
                if (s.getUserId().equals(userId) && s.getPassword().equals(password)) {
                    return true;
                }
            }
            return false;
        } else if (role.equals("ADMIN")) {
            // Admin credentials
            return userId.equals("admin") && password.equals("admin123");
        }
        return false;
    }
    
    public static Student getStudent(String userId) throws Exception {
        List<Student> students = FileHandler.loadStudents();
        for (Student s : students) {
            if (s.getUserId().equals(userId)) {
                return s;
            }
        }
        return null;
    }
}