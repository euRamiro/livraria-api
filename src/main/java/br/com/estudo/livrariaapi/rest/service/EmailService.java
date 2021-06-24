package br.com.estudo.livrariaapi.rest.service;

import java.util.List;

public interface EmailService {

	public void enviarEmails(String menssagem, List<String> emailsList);

}
