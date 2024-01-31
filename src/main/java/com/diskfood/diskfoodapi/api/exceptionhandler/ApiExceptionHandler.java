package com.diskfood.diskfoodapi.api.exceptionhandler;

import com.diskfood.diskfoodapi.core.validation.ValidacaoException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeEmUsoException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String USER_MESSAGE = "Ocorreu um erro interno inesperado no sistema. "
            + "Tente novamente e se o problema persistir, entre em contato "
            + "com o administrador do sistema.";
    public static final String CAMPOS_INVALIDOS = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }
        if (rootCause instanceof PropertyBindingException){
            return handlePropertyBindException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.CORPO_REQUISICAO_INVALIDO;
        String detail = "Existe algum erro no corpo da requisição";
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = CAMPOS_INVALIDOS;
        Problem problem = getProblemBindingResult(ex.getBindingResult(), status, detail);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }



    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException){
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
        String detail = String.format("O parâmetro de URL '%S' recebeu o valor '%s', que é de um tipo invalido." +
                "Corrija e informe um valor compatível com o tipo '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        System.out.println(ex);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindException(PropertyBindingException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.CORPO_REQUISICAO_INVALIDO;
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' não existe. Corrija ou remova essa propriedade e tente novamente", path);
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.CORPO_REQUISICAO_INVALIDO;
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é de um tipo invalido. Corrija" +
                " e informe um valor compatível com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(USER_MESSAGE).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private static String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> tratarEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
        Problem problem = createProblemBuilder(HttpStatus.CONFLICT, ProblemType.ENTIDADE_EM_USO, ex.getMessage())
                .userMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> tratarValidacaoException(ValidacaoException ex, WebRequest request){
        String detail = CAMPOS_INVALIDOS;
        Problem problem = getProblemBindingResult(ex.getBindingResult(), HttpStatus.BAD_REQUEST, detail);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException ex, WebRequest request){
        Problem problem = createProblemBuilder(HttpStatus.BAD_REQUEST, ProblemType.ERRO_NEGOCIO, ex.getMessage())
                .userMessage(USER_MESSAGE)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = USER_MESSAGE;
        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();
        Problem problem = createProblemBuilder(status, problemType, detail).userMessage(USER_MESSAGE).build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null){
            body = new Problem.ProblemBuilder().title(status.getReasonPhrase()).status(status.value()).userMessage(USER_MESSAGE).build();
        }else if (body instanceof String){
            body = new Problem.ProblemBuilder().title((String) body).status(status.value()).userMessage(USER_MESSAGE).build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
        return new Problem.ProblemBuilder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

    private Problem getProblemBindingResult(BindingResult ex, HttpStatus status, String detail) {
        BindingResult bindingResult = ex;
        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError){
                        name = ((FieldError) objectError).getField();
                    }

                    return new Problem.Object.FieldBuilder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());
        Problem problem = createProblemBuilder(status, ProblemType.DADOS_INVALIDOS, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();
        return problem;
    }
}

//    Vamos criar um método genérico que poderá ser usado em outros pontos da nossa classe.
//
//private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
//        HttpStatus status, WebRequest request) {
//
//        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
//        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
//
//        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
//        .map(objectError -> {
//        String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
//
//        String name = objectError.getObjectName();
//
//        if (objectError instanceof FieldError) {
//        name = ((FieldError) objectError).getField();
//        }
//
//        return Problem.Object.builder()
//        .name(name)
//        .userMessage(message)
//        .build();
//        })
//        .collect(Collectors.toList());
//
//        Problem problem = createProblemBuilder(status, problemType, detail)
//        .userMessage(detail)
//        .objects(problemObjects)
//        .build();
//
//        return handleExceptionInternal(ex, problem, headers, status, request);
//        }
//
//        2 -Adicionando método handleValidacaoException na classe ApiExcpetionHandler
//
//        Adicionamos o método que irá tratar ValidacaoException.
//
//
//@ExceptionHandler({ ValidacaoException.class })
//public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {
//        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(),
//        HttpStatus.BAD_REQUEST, request);
//        }
//
//        3 - Alterando o método handleMethodArgumentNotValid
//
//@Override
//protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//        HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
//        }

