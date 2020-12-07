package bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import bank.model.Client;
import bank.model.CreditAccount;

public interface CreditAccountRepository extends CrudRepository<CreditAccount, Integer>{
//	@Query("SELECT c FROM CreditAccount c WHERE c.client = :id")
//	 public List<CreditAccount> getByClient(@Param("id")int id); 
	List<CreditAccount> findByClient(Client client);
}
