package bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bank.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer>{
	 @Query("SELECT c FROM Client c WHERE c.name LIKE %?1%")
	 public List<Client> search(String keyword); 
	 @Query(value = "select * from client c right join \r\n"
	 		+ " (SELECT id_client FROM deposit_account group by id_client order by sum(balance) desc limit 10) d\r\n"
	 		+ " on(c.id=d.id_client)",nativeQuery = true)
	 public List<Client> findTopTen();
}
