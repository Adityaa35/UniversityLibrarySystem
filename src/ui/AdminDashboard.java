package ui;

import models.Book;
import models.Student;
import services.FileHandler;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private String adminId;
    private JTextArea logArea;
    
    public AdminDashboard(String adminId) {
        this.adminId = adminId;
        
        setTitle("Admin Dashboard - " + adminId);
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        JMenuBar menuBar = new JMenuBar();
        JMenu bookMenu = new JMenu("Books");
        JMenu userMenu = new JMenu("Users");
        
        JMenuItem addBookItem = new JMenuItem("Add Book");
        addBookItem.addActionListener(e -> addBook());
        JMenuItem removeBookItem = new JMenuItem("Remove Book");
        removeBookItem.addActionListener(e -> removeBook());
        JMenuItem viewBooksItem = new JMenuItem("View All Books");
        viewBooksItem.addActionListener(e -> viewAllBooks());
        
        JMenuItem registerStudentItem = new JMenuItem("Register Student");
        registerStudentItem.addActionListener(e -> registerStudent());
        JMenuItem viewStudentsItem = new JMenuItem("View Students");
        viewStudentsItem.addActionListener(e -> viewStudents());
        
        bookMenu.add(addBookItem);
        bookMenu.add(removeBookItem);
        bookMenu.add(viewBooksItem);
        userMenu.add(registerStudentItem);
        userMenu.add(viewStudentsItem);
        
        menuBar.add(bookMenu);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);
        
        // Initialize logArea FIRST before using it
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Now log() can be called safely because logArea is initialized
        log("Admin dashboard initialized. Welcome " + adminId + "!");
    }
    
    private void addBook() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        
        Object[] fields = {
            "Book ID:", idField,
            "Title:", titleField,
            "Author:", authorField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Book", 
                                                   JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Book book = new Book(idField.getText(), titleField.getText(), authorField.getText());
                FileHandler.saveBook(book);
                log("Book added: " + book.getTitle() + " (ID: " + book.getBookId() + ")");
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void removeBook() {
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to remove:");
        if (bookId == null || bookId.trim().isEmpty()) return;
        
        try {
            java.util.List<Book> books = FileHandler.loadBooks();
            boolean removed = books.removeIf(b -> b.getBookId().equals(bookId));
            
            if (removed) {
                FileHandler.updateBooks(books);
                log("Book removed: " + bookId);
                JOptionPane.showMessageDialog(this, "Book removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void viewAllBooks() {
        try {
            java.util.List<Book> books = FileHandler.loadBooks();
            StringBuilder sb = new StringBuilder("=== ALL BOOKS ===\n\n");
            for (Book b : books) {
                sb.append(b.getBookId()).append(" | ")
                  .append(b.getTitle()).append(" | ")
                  .append(b.getAuthor()).append(" | ")
                  .append(b.isAvailable() ? "Available" : "Issued to " + b.getIssuedTo())
                  .append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "All Books", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void registerStudent() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JPasswordField passField = new JPasswordField();
        
        Object[] fields = {
            "Student ID:", idField,
            "Name:", nameField,
            "Password:", passField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, "Register Student", 
                                                   JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Student student = new Student(idField.getText(), nameField.getText(), 
                                              new String(passField.getPassword()));
                FileHandler.saveStudent(student);
                log("Student registered: " + student.getName() + " (ID: " + student.getUserId() + ")");
                JOptionPane.showMessageDialog(this, "Student registered successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
    
    private void viewStudents() {
        try {
            java.util.List<Student> students = FileHandler.loadStudents();
            StringBuilder sb = new StringBuilder("=== REGISTERED STUDENTS ===\n\n");
            for (Student s : students) {
                sb.append(s.getUserId()).append(" | ")
                  .append(s.getName()).append(" | Books: ")
                  .append(s.getBorrowedBooks().size()).append("/3 | Fine: Rs.")
                  .append(s.getOutstandingFine()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "All Students", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void log(String message) {
        if (logArea != null) {
            logArea.append("[" + java.time.LocalTime.now() + "] " + message + "\n");
        } else {
            System.out.println("Log (before UI): " + message);
        }
    }
}