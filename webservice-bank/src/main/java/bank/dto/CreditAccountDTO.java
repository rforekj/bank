package bank.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;


@Data
public class CreditAccountDTO {
	
	private int id;
	private String type;
	private double balance;
	private double creditLimit;
	private Date createAt;
	@JsonBackReference(value = "client_credit")
	private ClientDTO client;
	private EmployeeDTO employeeCreate;
	@JsonManagedReference(value = "credit_transaction")
	private List<TransactionDTO> listTransaction;	
}
