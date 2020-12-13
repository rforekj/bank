package bank.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import bank.model.CreditAccount;

public interface CreditAccountRepository extends CrudRepository<CreditAccount, Integer>{
	@Query(value = "SELECT count(*) FROM CreditAccount WHERE client.id = :id_client")
	int checkNumberOfCredit(@Param("id_client") int idClient);
	
	@Query(value = "SELECT c FROM CreditAccount c WHERE c.employeeCreate.id = :id_employee AND c.createAt BETWEEN :start AND :end")
	List<CreditAccount> getByTimeAndIdEmployee(@Param("id_employee") int id_employee, @Param("start") Date start, @Param("end") Date end);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE CreditAccount SET balance = balance + :amount WHERE id=:id")
	int transaction(@Param("amount") double amount, @Param("id") int id);
	
	@Query(value = "SELECT DISTINCT c FROM CreditAccount c LEFT JOIN FETCH c.listTransaction t WHERE t.createAt BETWEEN :start AND :end AND c.client.id = :id_client")
	 public List<CreditAccount> getByTimeTransaction(@Param("id_client") int id_client, @Param("start") Date start,@Param("end") Date end );
	
	@Query(value = "SELECT c FROM CreditAccount c WHERE balance < 0 ORDER BY balance ASC ")
	List<CreditAccount> getByBalance();
}
