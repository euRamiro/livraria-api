package br.com.estudo.livrariaapi.rest.service.impl;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.estudo.livrariaapi.rest.service.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	
	private String defaultRemetente = "mail@library-api.com"; 
		
	private final JavaMailSender javaMailSernder;
	
	@Override
	public void enviarEmails(String menssagem, List<String> emailsList) {
		String[] emails =emailsList.toArray(new String[emailsList.size()]); 
				
		SimpleMailMessage emailMessage = new SimpleMailMessage();
		emailMessage.setFrom(defaultRemetente);
		emailMessage.setSubject("livro com empréstimo atrasado.");
		emailMessage.setText(menssagem);
		emailMessage.setTo(emails);		
				
		javaMailSernder.send(emailMessage);
	}

}
