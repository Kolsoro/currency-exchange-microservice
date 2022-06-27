package com.samer.microservice.currencyexchange;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {
	@Autowired
	private CurrencyExchangeRepository repo;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retriveExchangeValue(@PathVariable String from,@PathVariable String to) {
		
		CurrencyExchange c=repo.findByFromAndTo(from, to);
//		CurrencyExchange c= new CurrencyExchange(1000L,from,to,BigDecimal.valueOf(50));
		if(c==null) {
			throw new RuntimeException(String.format("There is no data found from %s to %s", from, to));
		}
		c.setEnvironment(env.getProperty("local.server.port"));
		return c;
		
	}

}
