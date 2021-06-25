package br.com.zup.proposta.proposta;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class Criptografador {

    @Value(value = "${api.secret}")
    private String secret;
    @Value(value = "${api.salt}")
    private String salt;


    public String criptografarDocumento(String documento) {
        TextEncryptor encryptor = Encryptors.queryableText(secret, salt);
        String documentoLimpo = documento.replaceAll("\\.|-", "");
        return encryptor.encrypt(documentoLimpo);
    }

    public String descriptografarDocumento(String documento) {
        TextEncryptor encryptor = Encryptors.queryableText(secret, salt);
        return encryptor.decrypt(documento);
    }
}
