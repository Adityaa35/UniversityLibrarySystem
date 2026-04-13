package interfaces;

public interface Payable {
    void payFine(double amount);
    double getOutstandingFine();
}