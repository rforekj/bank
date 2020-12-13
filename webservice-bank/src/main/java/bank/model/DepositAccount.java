package bank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "deposit_account")
public class DepositAccount {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String type;
	private double balance;
	private Date createAt;
	private float rate;
	private double minBalance;
	
	@PrePersist
	void placedAt() {
		this.createAt = new Date();
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_client", referencedColumnName = "id")
	private Client client;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_employee", referencedColumnName = "id")
	private Employee employeeCreate;
	@OneToMany(mappedBy = "depositAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Transaction> listTransaction;
}
