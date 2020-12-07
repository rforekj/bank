package bank.dto;
import java.util.Date;
import lombok.Data;

@Data
public class EmployeeDTO {
	private int id;
	private String identityCard;
	private String name;
	private Date dateOfBirth;
	private String address;
	private String level;
	private String experience;
	private String position;
}
