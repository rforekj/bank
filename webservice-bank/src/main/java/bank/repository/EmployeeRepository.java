package bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bank.dto.EmployeeDTO;
import bank.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>{
	 @Query("SELECT e FROM Employee e WHERE e.name LIKE %?1%")
	 public List<Employee> search(String keyword); 
}
