package com.ijdan.backendas.authorization.model;

import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

@ConfigurationProperties(prefix = "access_token.certificate")
@Component
public class CertificateManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateManager.class);

    private final static String DEFAULT_PASSWORD = "default_pass";
    @Value("${password}")
    private static String password;

    private final static String DEFAULT_CERTIFICATE_PATH = "certificates/default_sa_certificate.p12";
    @Value("${path}")
    private static String path;

    private final static String DEFAULT_TYPE_KS = "default_PKCS12";
    @Value("${type}")
    private static String type;


    public CertificateManager() {
    }

    public Keys keys () throws ExceptionsHandller {
        CertificateManager cm = new CertificateManager();

        password = password == null ? DEFAULT_PASSWORD : password;
        path = path == null ? DEFAULT_CERTIFICATE_PATH : path;
        type = type == null ? DEFAULT_TYPE_KS : type;

        LOGGER.info("password:{}    certPath:{}     type:{}", password, path, type);

        try (FileInputStream fis = new FileInputStream(path)) {
            KeyStore ks = KeyStore.getInstance(type);

            ks.load(fis, password.toCharArray());
            Enumeration<String> aliases = ks.aliases();

            if (aliases == null || !aliases.hasMoreElements()) {
                LOGGER.error("Certificate not found !!!! ");
                throw new ExceptionsHandller("Certificate loading failed !");
            }

            String alias = aliases.nextElement();

            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
                    new KeyStore.PasswordProtection(password.toCharArray()));

            return new Keys((RSAPrivateKey) keyEntry.getPrivateKey(),
                            (RSAPublicKey) keyEntry.getCertificate().getPublicKey());

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
                | UnrecoverableEntryException ex) {
            LOGGER.error("Exception !!!! {}", ex.getMessage());
            throw new ExceptionsHandller("Could not initialize OAuth Service", ex);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        CertificateManager.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class Keys {
        private RSAPrivateKey privateKey;
        private RSAPublicKey publicKey;

        public Keys(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        public RSAPublicKey getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(RSAPublicKey publicKey) {
            this.publicKey = publicKey;
        }
    }
}
