package org.example.service.transaction;

import jakarta.transaction.Transactional;
import org.example.data.model.LogisticCompany;
import org.example.data.model.Transaction;
import org.example.data.repository.TransactionRepository;
import org.example.exception.TransactionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void addTransaction(String customerEmail, double price, String bookingId, LogisticCompany logisticComapny) {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(price));
        transaction.setCustomerEmail(customerEmail);
        transaction.setBookingId(bookingId);
        transaction.setCompany(logisticComapny);
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction searchForTransaction(String bookingId, LogisticCompany logisticCompany) {
        for(Transaction transaction: transactionRepository.findAll()){
            if(transaction.getBookingId().equals(bookingId) && transaction.getCompany().equals(logisticCompany)){
                return transaction;
            }
        }
        throw new TransactionsException("Invalid Details");
    }

    @Override
    @Transactional
    public void deleteTransaction(String bookingId, LogisticCompany logisticCompany) {
        Transaction transaction = searchForTransaction(bookingId,logisticCompany);
        transactionRepository.delete(transaction);
    }

    @Override
    public List<Transaction> getAllTransaction(LogisticCompany logisticCompany) {
        List<Transaction> allTransaction = new ArrayList<>();
        for(Transaction transaction: transactionRepository.findAll()){
            if(transaction.getCompany().equals(logisticCompany))allTransaction.add(transaction);
        }
        return allTransaction;
    }

}

