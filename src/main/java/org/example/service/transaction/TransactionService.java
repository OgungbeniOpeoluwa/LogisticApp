package org.example.service.transaction;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Transaction;

import java.util.List;

public interface TransactionService {

    void addTransaction(String customerEmail, double price, String bookingId, LogisticCompany logisticComapny);

    Transaction searchForTransaction(String bookingId, LogisticCompany logisticCompany);

    void deleteTransaction(String bookingId, LogisticCompany logisticCompany);

    List<Transaction> getAllTransaction(LogisticCompany logisticCompany);
}
