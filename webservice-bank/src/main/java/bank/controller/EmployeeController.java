package bank.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bank.dto.CreditAccountDTO;
import bank.dto.DepositAccountDTO;
import bank.dto.EmployeeDTO;
import bank.model.CreditAccount;
import bank.model.DepositAccount;
import bank.model.Employee;
import bank.model.Transaction;
import bank.repository.CreditAccountRepository;
import bank.repository.DepositAccountRepository;
import bank.repository.EmployeeRepository;

@RestController
@RequestMapping(path = "employee", produces = "application/json")
@CrossOrigin(origins = "*")
public class EmployeeController {
	private EmployeeRepository employeeRepository;
	private CreditAccountRepository creditAccountRepository;
	private DepositAccountRepository depositAccountRepository;
	@Autowired
	private ModelMapper modelMapper;
	public EmployeeController(EmployeeRepository employeeRepository,CreditAccountRepository creditAccountRepository,DepositAccountRepository depositAccountRepository) {
		this.employeeRepository = employeeRepository;
		this.creditAccountRepository = creditAccountRepository;
		this.depositAccountRepository = depositAccountRepository;
	}

	@GetMapping
	public Iterable<EmployeeDTO> getAllEmployee(){
		List<Employee> employees = (List<Employee>) employeeRepository.findAll();
		return employees.stream().map(this::convertToEmployeeDTO).collect(Collectors.toList());
	}
	
	@GetMapping("search/{key}")
	public Iterable<EmployeeDTO> searchEmployee(@PathVariable("key") String key) {
		List<Employee> employees = (List<Employee>) employeeRepository.search(key);
		return employees.stream().map(this::convertToEmployeeDTO).collect(Collectors.toList());
	}
	@GetMapping("/{id}")
	public EmployeeDTO creditById(@PathVariable("id") int id) {
		Optional<Employee> optEmployee = employeeRepository.findById(id);
		if (optEmployee.isPresent()) {
			return convertToEmployeeDTO(optEmployee.get());
		}
		return null;
	}
	
	@GetMapping("/calculateSalary")
	public Map<String,Object> calculateSalary(
			@RequestParam("month") String month,
			@RequestParam("id_employee") int id_employee) throws ParseException {
		double salary = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int thang = Integer.parseInt(month.substring(0,2));
		int nam = Integer.parseInt(month.substring(3));
		int thangsau = thang,namsau = nam;
		if(thang+1>12) {
			thangsau = 1;
			namsau = nam + 1;
		} else thangsau++;
		Date start = formatter.parse(nam+"-"+thang+"-01 00:00:00");
		Date end = formatter.parse(namsau+"-"+thangsau+"-01 00:00:00");
		List<CreditAccount> creditAccounts = (List<CreditAccount>)creditAccountRepository.getByTimeAndIdEmployee(id_employee, start, end);
		List<DepositAccount> depositAccounts = (List<DepositAccount>)depositAccountRepository.getByTimeAndIdEmployee(id_employee, start, end);
		
		salary += creditAccounts.size()*500000;
		for (int i = 0; i < depositAccounts.size(); i++) {
			List<Transaction> listTransactions = depositAccounts.get(i).getListTransaction();
			if(listTransactions.size()>0) salary+=listTransactions.get(0).getAmount()*2/100;
		}
		Map<String,Object> map = new HashMap<>();
		map.put("list credit created", creditAccounts.stream().map(this::convertToCreditAccountDTO).collect(Collectors.toList()));
		map.put("list deposit created", depositAccounts.stream().map(this::convertToDepositAccountDTO).collect(Collectors.toList()));
		map.put("salary", salary);
		return map;
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeDTO addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Employee employee = this.convertToEmployee(employeeDTO);
		employeeRepository.save(employee);
		return employeeDTO;
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteEmployee(@PathVariable("id") int id) {
		employeeRepository.deleteById(id);
		return true;
	}
	@PutMapping("/{id}")
	public EmployeeDTO updateEmployee(@PathVariable("id") int id, @RequestBody EmployeeDTO employeeDTO) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if(employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();
			employee.setIdentityCard(employeeDTO.getIdentityCard());
			employee.setAddress(employeeDTO.getAddress());
			employee.setDateOfBirth(employeeDTO.getDateOfBirth());
			employee.setExperience(employeeDTO.getExperience());
			employee.setName(employeeDTO.getName());
			employee.setPosition(employeeDTO.getPosition());
			employeeRepository.save(employee);
			return employeeDTO;
		}
		return null;
	}
	private EmployeeDTO convertToEmployeeDTO(Employee employee) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDTO;
	}
	private Employee convertToEmployee(EmployeeDTO employeeDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		Employee employee = modelMapper.map(employeeDTO, Employee.class);
		return employee;
	}
	private CreditAccountDTO convertToCreditAccountDTO(CreditAccount creditAccount) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		CreditAccountDTO creditAccountDTO = modelMapper.map(creditAccount, CreditAccountDTO.class);
		return creditAccountDTO;
	}
	private DepositAccountDTO convertToDepositAccountDTO(DepositAccount depositAccount) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		DepositAccountDTO depositAccountDTO = modelMapper.map(depositAccount, DepositAccountDTO.class);
		return depositAccountDTO;
	}
}
