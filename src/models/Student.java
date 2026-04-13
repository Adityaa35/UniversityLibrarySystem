package models;

import interfaces.Payable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Payable {
    private List<String> borrowedBooks;
    private double fine;
    
    public Student(String userId, String name, String password) {
        super(userId, name, password, "STUDENT");
        this.borrowedBooks = new ArrayList<>();
        this.fine = 0.0;
    }
    
    @Override
    public String getDashboardInfo() {
        return "Student: " + name + " | Books: " + borrowedBooks.size() + "/3";
    }
    
    @Override
    public void payFine(double amount) {
        if (amount <= fine) {
            fine -= amount;
        }
    }
    
    @Override
    public double getOutstandingFine() {
        return fine;
    }
    
    // Additional methods
    public void addBook(String bookId) { 
        borrowedBooks.add(bookId); 
    }
    
    public void removeBook(String bookId) { 
        borrowedBooks.remove(bookId); 
    }
    
    public List<String> getBorrowedBooks() { 
        return borrowedBooks; 
    }
    
    public void addFine(double amount) { 
        this.fine += amount; 
    }
    
    public boolean canBorrowMore() { 
        return borrowedBooks.size() < 3; 
    }
}