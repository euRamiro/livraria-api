package br.com.estudo.livrariaapi.rest.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.rest.service.EmailService;
import br.com.estudo.livrariaapi.rest.service.EmprestimoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	private static final String CRON_DATE_EMPRESTIMOS = "0 0 0 1/1 * ?";
		
	private String menssagem = "ATenção!  Você tem empréstimo atrasado. Por favor, devolver livro assim que possível.";
	
	private final EmprestimoService emprestimosService;
	private final EmailService emailService;

	@Scheduled(cron = CRON_DATE_EMPRESTIMOS)
	public void enviarEmailParaEmprestimosAtrasados() {
		List<EmprestimoEntity> emprestimosAtrasadosList = emprestimosService.buscarTodosEmprestimosAtrasados();
		List<String> emailsList = emprestimosAtrasadosList.stream()
				.map(emprestimo -> 
					emprestimo.getEmailCliente())
				.collect(Collectors.toList());
		
		emailService.enviarEmails(menssagem, emailsList);
	}
}
