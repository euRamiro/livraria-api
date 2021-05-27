package br.com.estudo.livrariaapi.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.estudo.livrariaapi.exception.handler.validation.StandardError;
import br.com.estudo.livrariaapi.exception.handler.validation.ValidationError;
import br.com.estudo.livrariaapi.exception.model.ObjetoNaoEncontradoException;
import br.com.estudo.livrariaapi.exception.model.RegraDeNegocioException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		ValidationError erro = new ValidationError();

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			erro.addError(x.getField(), x.getDefaultMessage());
		}

		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(422);
		erro.setError("Erro de Validação");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		erro.setMensagemDesenvolvedor("");

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
	}

	@ExceptionHandler(ObjetoNaoEncontradoException.class)
	public ResponseEntity<StandardError> handlerObjectNotFoundException(ObjetoNaoEncontradoException e,
			HttpServletRequest req) {
		StandardError erro = new StandardError();
		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(404);
		erro.setError("Não existem dados.");
		erro.setMessage(e.getMessage());
		erro.setPath(req.getRequestURI());
		erro.setMensagemDesenvolvedor("");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(RegraDeNegocioException.class)
	public ResponseEntity<StandardError> handlerRegraDeNegocioException(RegraDeNegocioException e,
			HttpServletRequest req) {
		StandardError erro = new StandardError();
		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(400);
		erro.setError("Regra de negócio não atendida.");
		erro.setMessage(e.getMessage());
		erro.setPath(req.getRequestURI());
		erro.setMensagemDesenvolvedor("");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> DataIntegrityViolationException(DataIntegrityViolationException e,
			HttpServletRequest req) {
		StandardError erro = new StandardError();
		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(500);
		erro.setError(e.getMessage());
		erro.setMessage(e.getMessage());
		erro.setPath(req.getRequestURI());
		erro.setMensagemDesenvolvedor("");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

}
