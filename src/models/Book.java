package models;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean isAvailable;
    private String issuedTo;
    
    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.issuedTo = null;
    }
    
    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public String getIssuedTo() { return issuedTo; }
    
    // Setters
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setIssuedTo(String issuedTo) { this.issuedTo = issuedTo; }
    
    @Override
    public String toString() {
        return bookId + "," + title + "," + author + "," + isAvailable + "," + (issuedTo != null ? issuedTo : "null");
    }
}