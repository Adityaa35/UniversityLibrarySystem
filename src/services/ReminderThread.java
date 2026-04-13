package services;

import models.Transaction;
import java.time.LocalDate;
import java.util.List;

public class ReminderThread extends Thread {
    private volatile boolean running = true;
    
    @Override
    public void run() {
        while (running) {
            try {
                checkDueDates();
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void checkDueDates() throws Exception {
        List<Transaction> transactions = FileHandler.loadTransactions();
        LocalDate today = LocalDate.now();
        
        for (Transaction t : transactions) {
            if (t.getReturnDate() == null && t.getDueDate().isBefore(today)) {
                System.out.println("REMINDER: Transaction " + t.getTransactionId() + " is overdue!");
            }
        }
    }
    
    public void stopReminder() {
        running = false;
    }
}