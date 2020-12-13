package bank.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
import bank.model.Client;
import bank.repository.ClientRepository;

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
	@GetMapping("/topTen")
	public List<ClientDTO> getTopTenClient() {
		List<Client> clients = (List<Client>) clientRepository.findTopTen();
		return clients.stream().map(this::convertToClientDTO).collect(Collectors.toList());
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
	public ClientDTO updateClient(@PathVariable("id") int id, @RequestBody ClientDTO clientDTO) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		if(clientOptional.isPresent()) {
			Client client = clientOptional.get();
			client.setIdentityCard(clientDTO.getIdentityCard());
			client.setAddress(clientDTO.getAddress());
			client.setDateOfBirth(clientDTO.getDateOfBirth());
			client.setName(clientDTO.getName());
			clientRepository.save(client);
			return clientDTO;
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
