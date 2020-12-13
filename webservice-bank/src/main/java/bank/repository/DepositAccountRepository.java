package bank.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import bank.model.DepositAccount;

public interface DepositAccountRepository extends CrudRepository<DepositAccount, Integer>{
	@Query(value = "SELECT count(*) FROM DepositAccount WHERE client.id = :id_client")
	int checkNumberOfDeposit(@Param("id_client") int idClient);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE DepositAccount SET balance=balance + :amount WHERE id=:id")
	int transaction(@Param("amount") double amount, @Param("id") int id);

	@Query(value = "SELECT DISTINCT d FROM DepositAccount d LEFT JOIN FETCH d.listTransaction t WHERE d.employeeCreate.id = :id_employee AND d.createAt BETWEEN :start AND :end")
	List<DepositAccount> getByTimeAndIdEmployee(@Param("id_employee") int id_employee, @Param("start") Date start,@Param("end") Date end);
}
