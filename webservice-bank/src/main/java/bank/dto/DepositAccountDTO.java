package bank.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import bank.model.Client;
import bank.model.Employee;
import bank.model.Transaction;
import lombok.Data;
@Data
public class DepositAccountDTO {

	private int id;
	private String type;
	private double balance;
	private Date createAt;
	private float rate;
	private double minBalance;
	@JsonBackReference(value = "client_deposit")
	private ClientDTO client;
	@JsonBackReference(value = "employee_deposit")
	private EmployeeDTO employeeCreate;
	@JsonManagedReference(value = "deposit_transaction")
	private List<TransactionDTO> listTransaction;
}
