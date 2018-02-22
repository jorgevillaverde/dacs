/*
 * Copyright (C) 2015-2018 UTN-FRRe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.edu.utn.frre.dacs.loan.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frre.dacs.loan.client.dao.ClientRepository;
import ar.edu.utn.frre.dacs.loan.client.model.Client;

@RestController
public class Api {

	protected Logger logger = LoggerFactory.getLogger(Api.class.getName());
	
	@Autowired
	private ClientRepository repository;
	
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	@ResponseBody
	public List<Client> findAll() {
		logger.info("Returning all clients");
		
		return repository.findAll();
	}
	
	@RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET) 
	public ResponseEntity<?> findOne(
			@PathVariable("clientId") Long id) {
		logger.info("Returning client with id: " + id);
		
		Client client = repository.findOne(id);
		if(client == null) {
			logger.info("Returning client with id: " + id + " not found!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(client, HttpStatus.FOUND);
	}
		
	@RequestMapping(value = "/client", method = RequestMethod.POST)
	public ResponseEntity<?> createClient(@RequestBody Client client) {
		logger.info("Creating cliente: " + client);
		
		Client c = repository.save(client);
		
		return new ResponseEntity<>(c, HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/client", method = RequestMethod.PUT)
	public ResponseEntity<?> updateClient(@RequestBody Client client) {
		logger.info("Updating cliente with id: " + client.getId());
		
		Client c = null;
		
		if(client.getId() != null)  {
			c = repository.findOne(client.getId());
		}
		
		if(c == null) {
			logger.info("Client: "+ client + " not found!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		c.setFirstName(client.getFirstName());
		c.setLastName(client.getLastName());
		c.setDateOfBirth(client.getDateOfBirth());
		
		repository.save(c);
		
		return new ResponseEntity<>(c, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/client", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteClient(@RequestBody Client client) {
		logger.info("Deleting cliente: " + client);

		Client c = repository.findOne(client.getId());
		
		if(c == null) {
			logger.info("Client: "+ client + " not found!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		repository.delete(c);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/client/{clientId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteClientById(@PathVariable("clientId") Long id) {
		logger.info("Deleting cliente: " + id);
		
		if(!repository.exists(id)) {
			logger.info("Client with id: "+ id + " not found!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Client client = repository.findOne(id);
		repository.delete(client);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	
}
