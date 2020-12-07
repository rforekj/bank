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
public class CreditAccountDTO {
	
	private int id;
	private String type;
	private double balance;
	private Date createAt;
	//@JsonIgnore
	@JsonBackReference(value = "client_credit")
	private ClientDTO client;
	//@JsonIgnore
	@JsonBackReference(value = "employee_credit")
	private EmployeeDTO employeeCreate;
	//@JsonIgnore
	@JsonManagedReference(value = "credit_transaction")
	private List<TransactionDTO> listTransaction;	
}
