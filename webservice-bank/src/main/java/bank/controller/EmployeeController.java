package bank.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bank.dto.CreditAccountDTO;
import bank.dto.EmployeeDTO;
import bank.model.CreditAccount;
import bank.model.Employee;
import bank.repository.EmployeeRepository;

@RestController
@RequestMapping(path = "employee", produces = "application/json")
@CrossOrigin(origins = "*")
public class EmployeeController {
	private EmployeeRepository employeeRepository;
	@Autowired
	private ModelMapper modelMapper;
	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
//	@Bean
//	public ModelMapper employeeModelMapper() {
//	    return new ModelMapper();
//	}
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

//	@GetMapping("/{id}")
//	public Employee employeeById(@PathVariable("id") int id) {
//		Optional<Employee> optEmployee = employeeRepository.findById(id);
//		if (optEmployee.isPresent()) {
//			return optEmployee.get();
//		}
//		return null;
//	}
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Employee employee = this.convertToEmployee(employeeDTO);
		return employeeRepository.save(employee);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteEmployee(@PathVariable("id") int id) {
		employeeRepository.deleteById(id);
		return true;
	}
	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable("id") int id, @RequestBody EmployeeDTO employee) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if(employeeOptional.isPresent()) {
			Employee _employee = employeeOptional.get();
			_employee.setIdentityCard(employee.getIdentityCard());
			_employee.setAddress(employee.getAddress());
			_employee.setDateOfBirth(employee.getDateOfBirth());
			_employee.setExperience(employee.getExperience());
			_employee.setName(employee.getName());
			_employee.setPosition(employee.getPosition());
			return employeeRepository.save(_employee);
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
}
