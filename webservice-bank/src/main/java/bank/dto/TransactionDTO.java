package bank.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import bank.model.CreditAccount;
import bank.model.DepositAccount;
import lombok.Data;
@Data
public class TransactionDTO {

	private int id;
	private String content;
	private Date createAt;
	private double amount;
	//@JsonIgnore
	@JsonBackReference(value = "deposit_transaction")
	private DepositAccountDTO depositAccount;
	//@JsonIgnore
	@JsonBackReference(value = "credit_transaction")
	private CreditAccountDTO creditAccount;
}
