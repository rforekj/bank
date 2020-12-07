package bank.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
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
import bank.dto.TransactionDTO;
import bank.model.Transaction;
import bank.model.Client;
import bank.model.CreditAccount;
import bank.repository.TransactionRepository;
import bank.repository.CreditAccountRepository;

@RestController
@RequestMapping(path = "transaction", produces = "application/json")
@CrossOrigin(origins = "*")
public class TransactionController {
	private TransactionRepository transactionRepository;
	@Autowired
	private ModelMapper modelMapper;
	public TransactionController(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@GetMapping
	public Iterable<TransactionDTO> getAllTransaction(){
		List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
		return transactions.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());
	}
	@GetMapping("/{id}")
	public TransactionDTO creditById(@PathVariable("id") int id) {
		Optional<Transaction> optTransaction = transactionRepository.findById(id);
		if (optTransaction.isPresent()) {
			return convertToTransactionDTO(optTransaction.get());
		}
		return null;
	}
	@GetMapping("/")
	public Iterable<TransactionDTO> transactionByTime(@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")@RequestParam("start") Date start,@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")@RequestParam("end") Date end) {
		System.out.println(start + "    " + end);
		List<Transaction> transactions = (List<Transaction>) transactionRepository.searchTime(start,end);
		return transactions.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());
	}
	
//	@GetMapping("/{key}")
//	public Iterable<TransactionDTO> searchTransaction(@PathVariable("key") String key) {
//		List<Transaction> transactions = (List<Transaction>) transactionRepository.search(key);
//		return transactions.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());
//	}

//	@GetMapping("/{id}")
//	public Transaction transactionById(@PathVariable("id") int id) {
//		Optional<Transaction> optTransaction = transactionRepository.findById(id);
//		if (optTransaction.isPresent()) {
//			return optTransaction.get();
//		}
//		return null;
//	}
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Transaction addTransaction(@RequestBody TransactionDTO transactionDTO) {
		Transaction transaction = this.convertToTransaction(transactionDTO);
		return transactionRepository.save(transaction);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteTransaction(@PathVariable("id") int id) {
		transactionRepository.deleteById(id);
		return true;
	}
//	@PutMapping("/{id}")
//	public Transaction updateTransaction(@PathVariable("id") int id, @RequestBody Transaction transaction) {
//		Optional<Transaction> transactionOptional = transactionRepository.findById(id);
//		if(transactionOptional.isPresent()) {
//			Transaction _transaction = transactionOptional.get();
//			_transaction.setIdentityCard(transaction.getIdentityCard());
//			_transaction.setAddress(transaction.getAddress());
//			_transaction.setDateOfBirth(transaction.getDateOfBirth());
//			_transaction.setName(transaction.getName());
//			return transactionRepository.save(_transaction);
//		}
//		return null;
//	}
	private TransactionDTO convertToTransactionDTO(Transaction transaction) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
		return transactionDTO;
	}
	private Transaction convertToTransaction(TransactionDTO transactionDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
		return transaction;
	}
}
