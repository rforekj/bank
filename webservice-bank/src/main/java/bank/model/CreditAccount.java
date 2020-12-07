package bank.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "credit_account")
public class CreditAccount {
	@Id
	private int id;
	private String type;
	private double balance;
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss.SSS")
	private Date createAt;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_client", referencedColumnName = "id")
	private Client client;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_employee", referencedColumnName = "id")
	private Employee employeeCreate;
	@OneToMany(mappedBy = "creditAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Transaction> listTransaction;
	
	@PrePersist
	void placedAt() {
		this.createAt = new Date(); 
	}
}
