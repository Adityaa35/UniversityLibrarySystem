package ui;

import models.Book;
import models.Student;
import models.Transaction;
import services.FileHandler;
import services.AuthService;
import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class StudentDashboard extends JFrame {
    private String studentId;
    private Student currentStudent;
    private JTextArea infoArea;
    private DefaultListModel<String> bookListModel;
    
    public StudentDashboard(String studentId) throws Exception {
        this.studentId = studentId;
        this.currentStudent = AuthService.getStudent(studentId);
        
        setTitle("Student Dashboard - " + currentStudent.getName());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();
        refreshDisplay();
        setVisible(true);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        topPanel.add(new JScrollPane(infoArea));
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Available Books"));
        bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        centerPanel.add(new JScrollPane(bookList), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton issueBtn = new JButton("Issue Selected Book");
        issueBtn.addActionListener(e -> issueBook(bookList.getSelectedValue()));
        
        JButton returnBtn = new JButton("Return Book");
        returnBtn.addActionListener(e -> returnBook());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshDisplay());
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        
        buttonPanel.add(issueBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(logoutBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshDisplay() {
        try {
            currentStudent = AuthService.getStudent(studentId);
            infoArea.setText("Name: " + currentStudent.getName() + "\n" +
                           "User ID: " + currentStudent.getUserId() + "\n" +
                           "Books Issued: " + currentStudent.getBorrowedBooks().size() + "/3\n" +
                           "Outstanding Fine: Rs. " + currentStudent.getOutstandingFine());
            
            bookListModel.clear();
            List<Book> books = FileHandler.loadBooks();
            for (Book book : books) {
                if (book.isAvailable()) {
                    bookListModel.addElement(book.getBookId() + " - " + book.getTitle() + 
                                            " by " + book.getAuthor());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error refreshing: " + ex.getMessage());
        }
    }
    
    private void issueBook(String selectedBookDisplay) {
        if (selectedBookDisplay == null) {
            JOptionPane.showMessageDialog(this, "Please select a book!");
            return;
        }
        
        try {
            if (!currentStudent.canBorrowMore()) {
                throw new LimitExceededException("You have already borrowed maximum 3 books!");
            }
            
            String bookId = selectedBookDisplay.split(" - ")[0];
            List<Book> books = FileHandler.loadBooks();
            Book selectedBook = null;
            
            for (Book b : books) {
                if (b.getBookId().equals(bookId) && b.isAvailable()) {
                    selectedBook = b;
                    break;
                }
            }
            
            if (selectedBook == null) {
                throw new BookNotFoundException("Book not available!");
            }
            
            selectedBook.setAvailable(false);
            selectedBook.setIssuedTo(studentId);
            currentStudent.addBook(bookId);
            
            Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                bookId,
                studentId,
                LocalDate.now()
            );
            
            FileHandler.updateBooks(books);
            FileHandler.saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, "Book issued successfully!\nDue Date: " + transaction.getDueDate());
            refreshDisplay();
            
        } catch (LimitExceededException | BookNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void returnBook() {
        if (currentStudent.getBorrowedBooks().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books to return!");
            return;
        }
        
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to return:");
        if (bookId == null || bookId.trim().isEmpty()) return;
        
        try {
            List<Book> books = FileHandler.loadBooks();
            Book bookToReturn = null;
            
            for (Book b : books) {
                if (b.getBookId().equals(bookId) && !b.isAvailable() && 
                    b.getIssuedTo().equals(studentId)) {
                    bookToReturn = b;
                    break;
                }
            }
            
            if (bookToReturn == null) {
                throw new BookNotFoundException("Book not found or not issued to you!");
            }
            
            List<Transaction> transactions = FileHandler.loadTransactions();
            Transaction currentTransaction = null;
            for (Transaction t : transactions) {
                if (t.getBookId().equals(bookId) && t.getStudentId().equals(studentId) && 
                    t.getReturnDate() == null) {
                    currentTransaction = t;
                    break;
                }
            }
            
            double fine = currentTransaction != null ? currentTransaction.calculateFine() : 0;
            
            if (fine > 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Book is overdue! Fine: Rs. " + fine + "\nPay fine now?",
                    "Fine Due", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentStudent.payFine(fine);
                } else {
                    return;
                }
            }
            
            bookToReturn.setAvailable(true);
            bookToReturn.setIssuedTo(null);
            currentStudent.removeBook(bookId);
            
            FileHandler.updateBooks(books);
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
            refreshDisplay();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}