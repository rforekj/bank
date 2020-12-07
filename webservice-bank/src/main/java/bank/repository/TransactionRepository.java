package bank.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import bank.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer>{
	@Query("SELECT t FROM Transaction t WHERE t.createAt BETWEEN :start AND :end")
	public List<Transaction> searchTime(@Param("start") Date start,@Param("end") Date end);
}
