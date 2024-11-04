package apitransactiontreasuryconv.infrastructure.service;

import apitransactiontreasuryconv.entity.Transaction;

import java.util.List;

public interface TransactionService {

    public Transaction save(Transaction transaction);

    public List<Transaction> findAll();

    public Transaction findById(Long id);

}
