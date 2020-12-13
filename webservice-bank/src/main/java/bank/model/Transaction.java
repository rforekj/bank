package bank.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.Data;

@Entity
@Data
public class Transaction {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String content;
	private Date createAt;
	private double amount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_deposit", referencedColumnName = "id")
	private DepositAccount depositAccount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_credit", referencedColumnName = "id")
	private CreditAccount creditAccount;
	@PrePersist
	void placedAt() {
		this.createAt = new Date();
	}
}
