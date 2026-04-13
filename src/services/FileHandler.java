package services;

import models.Book;
import models.Student;
import models.Transaction;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class FileHandler {
    
    private static final String USER_FILE = "data/users.txt";
    private static final String BOOK_FILE = "data/books.txt";
    private static final String TRANSACTION_FILE = "data/transactions.txt";
    
    static {
        new File("data").mkdir();
    }
    
    // Save Student
    public static void saveStudent(Student student) throws IOException {
        try (FileWriter fw = new FileWriter(USER_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(student.getUserId() + "," + student.getName() + "," + 
                     student.getPassword() + ",STUDENT");
            bw.newLine();
        }
    }
    
    // Load all Students
    public static List<Student> loadStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) return students;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals("STUDENT")) {
                    Student s = new Student(parts[0], parts[1], parts[2]);
                    students.add(s);
                }
            }
        }
        return students;
    }
    
    // Save Book
    public static void saveBook(Book book) throws IOException {
        try (FileWriter fw = new FileWriter(BOOK_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(book.toString());
            bw.newLine();
        }
    }
    
    // Load all Books
    public static List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOK_FILE);
        if (!file.exists()) return books;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Book b = new Book(parts[0], parts[1], parts[2]);
                    b.setAvailable(Boolean.parseBoolean(parts[3]));
                    if (parts.length > 4 && !parts[4].equals("null")) {
                        b.setIssuedTo(parts[4]);
                    }
                    books.add(b);
                }
            }
        }
        return books;
    }
    
    // Update Books file
    public static void updateBooks(List<Book> books) throws IOException {
        try (FileWriter fw = new FileWriter(BOOK_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Book b : books) {
                bw.write(b.toString());
                bw.newLine();
            }
        }
    }
    
    // Save Transaction
    public static void saveTransaction(Transaction t) throws IOException {
        try (FileWriter fw = new FileWriter(TRANSACTION_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(t.getTransactionId() + "," + t.getBookId() + "," + 
                     t.getStudentId() + "," + t.getIssueDate() + "," + t.getDueDate());
            bw.newLine();
        }
    }
    
    // Load Transactions
    public static List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) return transactions;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Transaction t = new Transaction(parts[0], parts[1], parts[2], 
                                                    LocalDate.parse(parts[3]));
                    transactions.add(t);
                }
            }
        }
        return transactions;
    }
}