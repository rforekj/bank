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

import bank.dto.ClientDTO;
import bank.dto.CreditAccountDTO;
import bank.model.Client;
import bank.model.CreditAccount;
import bank.repository.ClientRepository;
import bank.repository.CreditAccountRepository;
import bank.repository.CreditAccountRepository;

@RestController
@RequestMapping(path = "creditAccount", produces = "application/json")
@CrossOrigin(origins = "*")
public class CreditAccountController {
	private CreditAccountRepository creditAccountRepository;
	@Autowired
	private ModelMapper modelMapper;
	public CreditAccountController(CreditAccountRepository creditAccountRepository) {
		this.creditAccountRepository = creditAccountRepository;
	}

	@GetMapping
	public Iterable<CreditAccountDTO> getAllCreditAccount(){
		List<CreditAccount> creditAccounts = (List<CreditAccount>) creditAccountRepository.findAll();
		return creditAccounts.stream().map(this::convertToCreditAccountDTO).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public CreditAccountDTO creditById(@PathVariable("id") int id) {
		Optional<CreditAccount> optCredit = creditAccountRepository.findById(id);
		if (optCredit.isPresent()) {
			return convertToCreditAccountDTO(optCredit.get());
		}
		return null;
	}

	
//	@GetMapping("/{key}")
//	public Iterable<CreditAccountDTO> searchCreditAccount(@PathVariable("key") int key) {
//		Optional<Client> client = clientRepository.findById(key);
//		System.out.println(client.get());
//		List<CreditAccount> creditAccounts = (List<CreditAccount>) creditAccountRepository.findByClient(client.get());
//		//return creditAccounts;
//		return creditAccounts.stream().map(this::convertToCreditAccountDTO).collect(Collectors.toList());
//	}

//	@GetMapping("/{id}")
//	public CreditAccount creditAccountById(@PathVariable("id") int id) {
//		Optional<CreditAccount> optCreditAccount = creditAccountRepository.findById(id);
//		if (optCreditAccount.isPresent()) {
//			return optCreditAccount.get();
//		}
//		return null;
//	}
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public CreditAccount addCreditAccount(@RequestBody CreditAccountDTO creditAccountDTO) {
		System.out.println(creditAccountDTO);
		CreditAccount creditAccount = this.convertToCreditAccount(creditAccountDTO);
		return creditAccountRepository.save(creditAccount);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteCreditAccount(@PathVariable("id") int id) {
		creditAccountRepository.deleteById(id);
		return true;
	}
	@PutMapping("/{id}")
	public CreditAccount updateCreditAccount(@PathVariable("id") int id, @RequestBody CreditAccountDTO creditAccount) {
		Optional<CreditAccount> creditAccountOptional = creditAccountRepository.findById(id);
		if(creditAccountOptional.isPresent()) {
			CreditAccount _creditAccount = creditAccountOptional.get();
			_creditAccount.setType(creditAccount.getType());
			_creditAccount.setBalance(creditAccount.getBalance());
			return creditAccountRepository.save(_creditAccount);
		}
		return null;
	}
	private CreditAccountDTO convertToCreditAccountDTO(CreditAccount creditAccount) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		CreditAccountDTO creditAccountDTO = modelMapper.map(creditAccount, CreditAccountDTO.class);
		return creditAccountDTO;
	}
	private CreditAccount convertToCreditAccount(CreditAccountDTO creditAccountDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		CreditAccount creditAccount = modelMapper.map(creditAccountDTO, CreditAccount.class);
		return creditAccount;
	}
}
