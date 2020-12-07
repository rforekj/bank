package bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bank.model.Client;

public interface ClientRepository extends CrudRepository<Client, Integer>{
	 @Query("SELECT c FROM Client c WHERE c.name LIKE %?1%")
	 public List<Client> search(String keyword); 
}
