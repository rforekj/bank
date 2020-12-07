package bank.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import bank.model.CreditAccount;
import bank.model.DepositAccount;
import lombok.Data;

@Data
public class ClientDTO {
	private int id;
	private String identityCard;
	private String name;
	private Date dateOfBirth;
	private String address;
	@JsonManagedReference(value = "client_credit")
	private List<CreditAccountDTO> creditAccounts;
	@JsonManagedReference(value = "client_deposit")
	private List<DepositAccountDTO> depositAccounts;
}
