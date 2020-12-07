package bank.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.Data;


@Data
@Entity
@Table(name = "employee")
public class Employee {
	@Id
	private int id;
	private String identityCard;
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private String address;
	private String experience;
	private String position;
	private String level;
	
}
