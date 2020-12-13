package bank.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.dto.TransactionDTO;
import bank.model.Transaction;
import bank.model.CreditAccount;
import bank.model.DepositAccount;
import bank.repository.TransactionRepository;
import bank.repository.CreditAccountRepository;
import bank.repository.DepositAccountRepository;

@RestController
@RequestMapping(path = "transaction", produces = "application/json")
@CrossOrigin(origins = "*")
public class TransactionController {
	private TransactionRepository transactionRepository;
	private CreditAccountRepository creditAccountRepository;
	private DepositAccountRepository depositAccountRepository;
	@Autowired
	private ModelMapper modelMapper;

	public TransactionController(TransactionRepository transactionRepository,
			CreditAccountRepository creditAccountRepository, DepositAccountRepository depositAccountRepository) {
		this.transactionRepository = transactionRepository;
		this.creditAccountRepository = creditAccountRepository;
		this.depositAccountRepository = depositAccountRepository;
	}

	@GetMapping
	public Iterable<TransactionDTO> getAllTransaction() {
		List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
		return transactions.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public TransactionDTO findById(@PathVariable("id") int id) {
		Optional<Transaction> optTransaction = transactionRepository.findById(id);
		if (optTransaction.isPresent()) {
			return convertToTransactionDTO(optTransaction.get());
		}
		return null;
	}
	
	@Transactional
	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> addTransaction(@RequestBody TransactionDTO transactionDTO) {
		Transaction transaction = this.convertToTransaction(transactionDTO);
		if (transactionDTO.getCreditAccount() != null) {
			Optional<CreditAccount> creditAccount = creditAccountRepository
					.findById(transactionDTO.getCreditAccount().getId());
			if (creditAccount.get().getBalance() + transactionDTO.getAmount() >= creditAccount.get().getCreditLimit()) {
				transactionRepository.save(transaction);
				creditAccountRepository.transaction(transactionDTO.getAmount(),
						transactionDTO.getCreditAccount().getId());
				return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			Optional<DepositAccount> depositAccount = depositAccountRepository
					.findById(transactionDTO.getDepositAccount().getId());
			if (depositAccount.get().getBalance() + transactionDTO.getAmount() >= depositAccount.get().getMinBalance()) {
				transactionRepository.save(transaction);
				depositAccountRepository.transaction(transactionDTO.getAmount(),
						transactionDTO.getDepositAccount().getId());
				return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}
	
	@Transactional
	@PostMapping("/depositToCredit")
	public ResponseEntity<?> addTransactionDepositToCredit(@RequestParam("amount") double amount, 
			@RequestParam("id_deposit") int idDeposit, @RequestParam("id_credit") int idCredit) {	
		
		Optional<DepositAccount> depositAccount = depositAccountRepository.findById(idDeposit);
		Transaction depositSub = new Transaction();
		depositSub.setAmount(-amount);
		depositSub.setContent("to credit id "+idCredit);
		depositSub.setDepositAccount(depositAccount.get());
		if (depositAccount.get().getBalance() + depositSub.getAmount() >= depositAccount.get().getMinBalance()) {
			transactionRepository.save(depositSub);
			depositAccountRepository.transaction(-amount,idDeposit);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Optional<CreditAccount> creditAccount = creditAccountRepository.findById(idCredit);
		Transaction creditAdd = new Transaction();
		creditAdd.setAmount(amount);
		creditAdd.setContent("from deposit id "+idDeposit);
		creditAdd.setCreditAccount(creditAccount.get());	
		creditAdd.setId(depositSub.getId()+1);
	    transactionRepository.save(creditAdd);
	    creditAccountRepository.transaction(amount,idCredit);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteTransaction(@PathVariable("id") int id) {
		transactionRepository.deleteById(id);
		return true;
	}

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
