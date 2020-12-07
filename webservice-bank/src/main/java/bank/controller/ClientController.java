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
import bank.model.Client;
import bank.model.CreditAccount;
import bank.repository.ClientRepository;
import bank.repository.CreditAccountRepository;

@RestController
@RequestMapping(path = "client", produces = "application/json")
@CrossOrigin(origins = "*")
public class ClientController {
	private ClientRepository clientRepository;
	@Autowired
	private ModelMapper modelMapper;
	public ClientController(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	@GetMapping
	public Iterable<ClientDTO> getAllClient(){
		List<Client> clients = (List<Client>) clientRepository.findAll();
		return clients.stream().map(this::convertToClientDTO).collect(Collectors.toList());
	}
	
	@GetMapping("search/{key}")
	public Iterable<ClientDTO> searchClient(@PathVariable("key") String key) {
		List<Client> clients = (List<Client>) clientRepository.search(key);
		return clients.stream().map(this::convertToClientDTO).collect(Collectors.toList());
	}
	@GetMapping("/{id}")
	public ClientDTO clientById(@PathVariable("id") int id) {
		Optional<Client> optClient = clientRepository.findById(id);
		if (optClient.isPresent()) {
			return convertToClientDTO(optClient.get());
		}
		return null;
	}

	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Client addClient(@RequestBody ClientDTO clientDTO) {
		Client client = this.convertToClient(clientDTO);
		return clientRepository.save(client);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteClient(@PathVariable("id") int id) {
		clientRepository.deleteById(id);
		return true;
	}
	@PutMapping("/{id}")
	public Client updateClient(@PathVariable("id") int id, @RequestBody ClientDTO client) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		if(clientOptional.isPresent()) {
			Client _client = clientOptional.get();
			_client.setIdentityCard(client.getIdentityCard());
			_client.setAddress(client.getAddress());
			_client.setDateOfBirth(client.getDateOfBirth());
			_client.setName(client.getName());
			return clientRepository.save(_client);
		}
		return null;
	}
	private ClientDTO convertToClientDTO(Client client) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
		return clientDTO;
	}
	private Client convertToClient(ClientDTO clientDTO) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		Client client = modelMapper.map(clientDTO, Client.class);
		return client;
	}
}
