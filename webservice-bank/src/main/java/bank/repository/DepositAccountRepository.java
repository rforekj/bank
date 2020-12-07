package bank.repository;

import org.springframework.data.repository.CrudRepository;

import bank.model.DepositAccount;

public interface DepositAccountRepository extends CrudRepository<DepositAccount, Integer>{

}
