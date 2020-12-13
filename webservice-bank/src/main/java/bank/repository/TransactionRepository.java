package bank.repository;


import org.springframework.data.repository.CrudRepository;

import bank.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer>{
	
}
