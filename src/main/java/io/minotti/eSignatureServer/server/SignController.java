package io.minotti.eSignatureServer.server;

import io.minotti.eSignatureServer.Application;
import io.minotti.eSignatureServer.signature.Document;
import io.minotti.eSignatureServer.signature.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/sign")
public class SignController {
  private static final Logger logger = LoggerFactory.getLogger(SignController.class);

  @RequestMapping(value = "/deferred", method = {RequestMethod.POST, RequestMethod.PUT})
  public boolean signDeferred (@RequestParam("file") MultipartFile file, @RequestParam("format") String format, @RequestParam("webhook") String webhook) {
    try {
      File inFile = Application.storageManager.getNewFile("in");
      file.transferTo(inFile);

      Document document = new Document(inFile, format, webhook);
      Application.signQueue.enqueue(document);

      return true; //TODO: normalize responses
    } catch (IOException e) {
      logger.error(e.toString());
      return false;
    }
  }

  @RequestMapping(value = "/immediate", method = {RequestMethod.POST, RequestMethod.PUT})
  public byte[] signImmediate (@RequestParam("file") MultipartFile file, @RequestParam("format") String format) throws SignatureException {
    try {
      File inFile = Application.storageManager.getNewFile("in");
      file.transferTo(inFile);

      Document inDocument = new Document(inFile, format);
      Document outDocument = Application.signServicePool.sign(inDocument);

      //TODO: delete files
      return Files.readAllBytes(outDocument.getFile().toPath());
    } catch (IOException e) {
      logger.error(e.toString());
      return null;
    }
  }
}
