package bank.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name = "employee")
public class Employee {
	@Id
	private int id;
	private String identityCard;
	private String name;
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private String address;
	private String experience;
	private String position;
	private String level;
	
}
