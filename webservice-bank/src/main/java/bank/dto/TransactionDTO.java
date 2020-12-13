package bank.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
@Data
public class TransactionDTO {

	private int id;
	private String content;
	private Date createAt;
	private double amount;
	@JsonBackReference(value = "deposit_transaction")
	private DepositAccountDTO depositAccount;
	@JsonBackReference(value = "credit_transaction")
	private CreditAccountDTO creditAccount;
}
