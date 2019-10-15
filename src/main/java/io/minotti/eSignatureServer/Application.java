package io.minotti.eSignatureServer;

import io.minotti.eSignatureServer.config.Config;
import io.minotti.eSignatureServer.signature.service.NoSignService;
import io.minotti.eSignatureServer.signature.service.PAdESSignService;
import io.minotti.eSignatureServer.signature.service.Pkcs11Provider;
import io.minotti.eSignatureServer.signature.SignQueue;
import io.minotti.eSignatureServer.signature.SignServicePool;
import io.minotti.eSignatureServer.signature.SignThread;
import io.minotti.eSignatureServer.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

@SpringBootApplication
public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private static Config config;
  public static StorageManager storageManager;
  public static SignServicePool signServicePool;
  public static SignQueue signQueue;
  private static Pkcs11Provider pkcs11Provider;
  public static Validator validator;

  public static void main (String[] args) {
    try {
      config = Config.fromFile(new File("./config.json"));
    } catch (IOException e) {
      logger.error("Cannot find config.json");
      System.exit(1);
    }

    storageManager = new StorageManager(Paths.get(System.getProperty("user.dir"), "files"));

    if (config.pkcs11Provider != null) { // If there's a Pkcs11 provider
      try {
        pkcs11Provider = new Pkcs11Provider(config.pkcs11Provider.driverPath, config.pkcs11Provider.pin);
      } catch (Error e) {
        logger.error(e.toString());
        System.exit(1);
      }

      try {
        signServicePool.register("pades", new PAdESSignService(pkcs11Provider, config.pkcs11Provider.keyName));
      } catch (Error e) {
        logger.error(e.toString());
        System.exit(1);
      }
    }

    signServicePool = new SignServicePool(storageManager);
    signServicePool.register("none", new NoSignService());

    signQueue = new SignQueue(signServicePool);

    validator = new Validator();

    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(Collections.singletonMap("server.address", config.server.address));
    app.setDefaultProperties(Collections.singletonMap("server.port", config.server.port.toString()));
    app.run(args); // Start the web server

    SignThread signThread = new SignThread(signQueue);
    signThread.start();  // Start the signing thread
  }
}
