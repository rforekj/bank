package bank.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.dto.CreditAccountDTO;
import bank.model.CreditAccount;
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
	public Iterable<CreditAccountDTO> getAllCreditAccount() {
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
	@GetMapping("/getByTimeTransaction")
	public Iterable<CreditAccountDTO> getByTimeTransaction(
			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam("start") Date start,
			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam("end") Date end,
			@RequestParam("id_client") int id_client) {
		List<CreditAccount> creditAccounts = (List<CreditAccount>) creditAccountRepository.getByTimeTransaction(id_client, start, end);
		return creditAccounts.stream().map(this::convertToCreditAccountDTO).collect(Collectors.toList());
	}
	@GetMapping("/getByBalance")
	public Iterable<CreditAccountDTO> getByBalance(){
		List<CreditAccount> creditAccounts = (List<CreditAccount>) creditAccountRepository.getByBalance();
		return creditAccounts.stream().map(this::convertToCreditAccountDTO).collect(Collectors.toList());
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> addCreditAccount(@RequestBody CreditAccountDTO creditAccountDTO) {
		CreditAccount creditAccount = this.convertToCreditAccount(creditAccountDTO);
		if (creditAccountRepository.checkNumberOfCredit(creditAccount.getClient().getId()) < 2) {
			creditAccountRepository.save(creditAccount);
			return new ResponseEntity<>(creditAccountDTO, HttpStatus.OK);
		} else
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{id}")
	public boolean deleteCreditAccount(@PathVariable("id") int id) {
		creditAccountRepository.deleteById(id);
		return true;
	}

	@PutMapping("/{id}")
	public CreditAccountDTO updateCreditAccount(@PathVariable("id") int id,
			@RequestBody CreditAccountDTO creditAccountDTO) {
		Optional<CreditAccount> creditAccountOptional = creditAccountRepository.findById(id);
		if (creditAccountOptional.isPresent()) {
			CreditAccount creditAccount = creditAccountOptional.get();
			creditAccount.setType(creditAccountDTO.getType());
			creditAccount.setBalance(creditAccountDTO.getBalance());
			creditAccount.setCreditLimit(creditAccountDTO.getCreditLimit());
			creditAccountRepository.save(creditAccount);
			return creditAccountDTO;
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
