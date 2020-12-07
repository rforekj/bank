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
import bank.dto.DepositAccountDTO;
import bank.model.Client;
import bank.model.CreditAccount;
import bank.model.DepositAccount;
import bank.repository.ClientRepository;
import bank.repository.DepositAccountRepository;
import bank.repository.DepositAccountRepository;

@RestController
@RequestMapping(path = "depositAccount", produces = "application/json")
@CrossOrigin(origins = "*")
public class DepositAccountController {
	private DepositAccountRepository depositAccountRepository;
	@Autowired
	private ModelMapper modelMapper;
	public DepositAccountController(DepositAccountRepository depositAccountRepository) {
		this.depositAccountRepository = depositAccountRepository;
	}
//	@Bean
//	public ModelMapper depositAccountModelMapper() {
//	    return new ModelMapper();
//	}
	@GetMapping
	public Iterable<DepositAccountDTO> getAllDepositAccount(){
		List<DepositAccount> depositAccounts = (List<DepositAccount>) depositAccountRepository.findAll();
		return depositAccounts.stream().map(this::convertToDepositAccountDTO).collect(Collectors.toList());
	}
	@GetMapping("/{id}")
	public DepositAccountDTO creditById(@PathVariable("id") int id) {
		Optional<DepositAccount> optDeposit = depositAccountRepository.findById(id);
		if (optDeposit.isPresent()) {
			return convertToDepositAccountDTO(optDeposit.get());
		}
		return null;
	}
	
//	@GetMapping("/{key}")
//	public Iterable<DepositAccountDTO> searchDepositAccount(@PathVariable("key") int key) {
//		Optional<Client> client = clientRepository.findById(key);
//		System.out.println(client.get());
//		List<DepositAccount> depositAccounts = (List<DepositAccount>) depositAccountRepository.findByClient(client.get());
//		//return depositAccounts;
//		return depositAccounts.stream().map(this::convertToDepositAccountDTO).collect(Collectors.toList());
//	}

//	@GetMapping("/{id}")
//	public DepositAccount depositAccountById(@PathVariable("id") int id) {
//		Optional<DepositAccount> optDepositAccount = depositAccountRepository.findById(id);
//		if (optDepositAccount.isPresent()) {
//			return optDepositAccount.get();
//		}
//		return null;
//	}
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public DepositAccount addDepositAccount(@RequestBody DepositAccountDTO depositAccountDTO) {
		DepositAccount depositAccount = this.convertToDepositAccount(depositAccountDTO);
		return depositAccountRepository.save(depositAccount);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteDepositAccount(@PathVariable("id") int id) {
		depositAccountRepository.deleteById(id);
		return true;
	}
	@PutMapping("/{id}")
	public DepositAccount updateDepositAccount(@PathVariable("id") int id, @RequestBody DepositAccountDTO depositAccount) {
		Optional<DepositAccount> depositAccountOptional = depositAccountRepository.findById(id);
		if(depositAccountOptional.isPresent()) {
			DepositAccount _depositAccount = depositAccountOptional.get();
			_depositAccount.setType(depositAccount.getType());
			_depositAccount.setBalance(depositAccount.getBalance());
			_depositAccount.setRate(depositAccount.getRate());
			_depositAccount.setMinBalance(depositAccount.getMinBalance());
			return depositAccountRepository.save(_depositAccount);
		}
		return null;
	}
	private DepositAccountDTO convertToDepositAccountDTO(DepositAccount depositAccount) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		DepositAccountDTO depositAccountDTO = modelMapper.map(depositAccount, DepositAccountDTO.class);
		return depositAccountDTO;
	}
	private DepositAccount convertToDepositAccount(DepositAccountDTO depositAccountDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		DepositAccount depositAccount = modelMapper.map(depositAccountDTO, DepositAccount.class);
		return depositAccount;
	}
}
