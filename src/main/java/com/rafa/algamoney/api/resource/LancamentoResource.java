package com.rafa.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafa.algamoney.api.event.RecursoCriadoEvent;
import com.rafa.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.rafa.algamoney.api.model.Lancamento;
import com.rafa.algamoney.api.repository.LancamentoRepository;
import com.rafa.algamoney.api.repository.filter.LancamentoFilter;
import com.rafa.algamoney.api.service.LancamentoService;
import com.rafa.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource
{
	@Autowired
	private LancamentoRepository lancamentoRepository;
	@Autowired
	private LancamentoService lanService;  
	
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private MessageSource messageSource;
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response)
	{
		Lancamento lancamentoSalvo = lanService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter filter)
	{
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo)
	{
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler( { PessoaInexistenteOuInativaException.class } )
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex)
	{
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario,mensagemDesenvolvedor) );
		
		return ResponseEntity.badRequest().body(erros);
	}

}
