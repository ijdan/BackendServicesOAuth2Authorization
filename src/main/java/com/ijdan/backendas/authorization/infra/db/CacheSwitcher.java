package com.ijdan.backendas.authorization.infra.db;


import com.ijdan.backendas.authorization.services.OAuth2BasicAuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class CacheSwitcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2BasicAuthenticationController.class);

    @Value("${url}")
    private static String URL;

    @Value("${username}")
    private static String USERNAME;

    @Value("${password}")
    private static String PASSWORD;

    private static boolean SWITCH = false;

    public CacheSwitcher() throws SQLException {
        LOGGER.info("URL : {}     USERNAME : {}     PASSWORD : {}", URL, USERNAME, PASSWORD);

        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            this.setSWITCH(false);
            LOGGER.info("Connexion OK");
        } catch (Exception e) {
            System.out.println("Cannot connect the database");
        } finally {
            if (c != null) {
                c.close();
            }
        }
        this.setSWITCH(true);
        LOGGER.info("Connexion KO!!!!!");
    }

    public static boolean isSWITCH() {
        return SWITCH;
    }

    public static void setSWITCH(boolean SWITCH) {
        CacheSwitcher.SWITCH = SWITCH;
    }

    public boolean isNominalEnabled() throws SQLException {
        return false;
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        CacheSwitcher.URL = URL;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        CacheSwitcher.USERNAME = USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        CacheSwitcher.PASSWORD = PASSWORD;
    }
}
