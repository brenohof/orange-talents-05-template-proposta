package br.com.zup.proposta.core.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collection;

@RestControllerAdvice
public class ApiErroExceptionHandler {

    @ExceptionHandler(ApiErroException.class)
    public ResponseEntity<ErroResponse> handle(ApiErroException apiErroException) {
        Collection<String> mensagens = new ArrayList<>();
        mensagens.add(apiErroException.getReason());

        ErroResponse erroResponse = new ErroResponse(mensagens);
        return ResponseEntity.status(apiErroException.getHttpStatus()).body(erroResponse);
    }
}
