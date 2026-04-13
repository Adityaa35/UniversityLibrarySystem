package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String transactionId;
    private String bookId;
    private String studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    
    // Constructor
    public Transaction(String transactionId, String bookId, String studentId, LocalDate issueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(14); // 14 days due period
        this.returnDate = null;
        this.fine = 0.0;
    }
    
    // Calculate fine: Rs. 5 per day after due date
    public double calculateFine() {
        if (returnDate == null && LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysLate * 5;
        } else if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysLate * 5;
        }
        return 0;
    }
    
    // ========== GETTER METHODS ==========
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public double getFine() {
        return fine;
    }
    
    // ========== SETTER METHODS ==========
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(14);
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public void setFine(double fine) {
        this.fine = fine;
    }
    
    // ========== TOSTRING METHOD (optional, useful for debugging) ==========
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", fine=" + fine +
                '}';
    }
}