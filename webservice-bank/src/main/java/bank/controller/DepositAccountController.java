package bank.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bank.dto.DepositAccountDTO;
import bank.model.DepositAccount;
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

	@GetMapping
	public Iterable<DepositAccountDTO> getAllDepositAccount() {
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

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> addDepositAccount(@RequestBody DepositAccountDTO depositAccountDTO) {
		DepositAccount depositAccount = this.convertToDepositAccount(depositAccountDTO);
		if (depositAccountRepository.checkNumberOfDeposit(depositAccount.getClient().getId()) < 3) {
			depositAccountRepository.save(depositAccount);
			return new ResponseEntity<>(depositAccountDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public boolean deleteDepositAccount(@PathVariable("id") int id) {
		depositAccountRepository.deleteById(id);
		return true;
	}

	@PutMapping("/{id}")
	public DepositAccountDTO updateDepositAccount(@PathVariable("id") int id,
			@RequestBody DepositAccountDTO depositAccountDTO) {
		Optional<DepositAccount> depositAccountOptional = depositAccountRepository.findById(id);
		if (depositAccountOptional.isPresent()) {
			DepositAccount depositAccount = depositAccountOptional.get();
			depositAccount.setType(depositAccountDTO.getType());
			depositAccount.setBalance(depositAccountDTO.getBalance());
			depositAccount.setRate(depositAccountDTO.getRate());
			depositAccount.setMinBalance(depositAccountDTO.getMinBalance());
			depositAccountRepository.save(depositAccount);
			return depositAccountDTO;
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
