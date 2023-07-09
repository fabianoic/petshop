package br.com.ficampos.petshop.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class PetshopExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({CPFInvalidoException.class})
    public ResponseEntity<Object> handleCadastroNacionalInvalido(CPFInvalidoException ex, WebRequest webRequest) {
        Erro erro = new Erro("Cadastro nacional inválido, informe um documento válido!", this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler({DadoJaExisteException.class})
    public ResponseEntity<Object> handleDadoJaExistente(DadoJaExisteException ex, WebRequest webRequest) {
        Erro erro = new Erro(ex.getMensagem(), this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler({DadoNaoExisteException.class})
    public ResponseEntity<Object> handleDadoNaoExistente(DadoNaoExisteException ex, WebRequest webRequest) {
        Erro erro = new Erro(ex.getMensagem(), this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler({UsuarioSemPermissaoException.class})
    public ResponseEntity<Object> handleUsuarioSemPermissao(UsuarioSemPermissaoException ex, WebRequest webRequest) {
        Erro erro = new Erro("O usuario solicitante nao tem permissao para executar a acao", this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.FORBIDDEN, webRequest);
    }

    @ExceptionHandler({DadoInformadoInvalidoException.class})
    public ResponseEntity<Object> handleDadoInformadoInvalido(DadoInformadoInvalidoException ex, WebRequest webRequest) {
        Erro erro = new Erro("Um ou mais dados informados não é(são) valido(s)", this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleDuplicidade(ConstraintViolationException ex, WebRequest webRequest) {
        if (ex.getMessage().contains("duplicate key value violates unique constraint")) {
            Erro erro = new Erro("Valor informado já existe e não pode repetir", this.getCause(ex));
            return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        }
        Erro erro = new Erro("Dados informados invalidos ou já em uso", this.getCause(ex));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @NonNull
    @Override // Para atributos não identificados no JSON
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemUsuario = "Dados incorretos";
        String mensagemDesenvolvedor = ex.getCause().toString();
        Erro erro = new Erro(mensagemUsuario, mensagemDesenvolvedor);
        return handleExceptionInternal(ex, erro, headers, status, request);
    }

    @NonNull
    @Override // Bean validation
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Erro> errosBinding = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, errosBinding, headers, status, request);
    }

    private List<Erro> criarListaDeErros(@NotNull BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String mensagemDesenvolvedor = fieldError.toString();
            erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        }
        return erros;
    }

    private String getCause(Throwable ex) {
        return ex.getStackTrace() != null ? ExceptionUtils.getStackTrace(ex) : ex.toString();
    }

    public static class Erro {
        private final String timestamp;
        private final String mensagemUsuario;
        private final String mensagemDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
            this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }

        public String getMensagemUsuario() {
            return mensagemUsuario;
        }

        public String getMensagemDesenvolvedor() {
            return mensagemDesenvolvedor;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
