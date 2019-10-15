package io.minotti.eSignatureServer.server;

import io.minotti.eSignatureServer.Application;
import io.minotti.eSignatureServer.signature.Document;
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
@RequestMapping("/validate")
public class ValidateController {
  private static final Logger logger = LoggerFactory.getLogger(ValidateController.class);

  @RequestMapping(value = "/isSignedBy", method = {RequestMethod.POST, RequestMethod.PUT})
  public boolean signImmediate (@RequestParam("file") MultipartFile file, @RequestParam("format") String format, @RequestParam("fiscal_code") String fiscal_code) {
    try {
      File inFile = Application.storageManager.getNewFile("in");
      file.transferTo(inFile);

      Document inDocument = new Document(inFile, format);
      boolean verified = Application.validator.isSignedBy(inDocument, fiscal_code);

      Files.delete(inFile.toPath());

      return verified; //TODO: normalize responses
    } catch (IOException e) {
      logger.error(e.toString());
      return false;
    }
  }

  @RequestMapping(value = "/extractSignatures", method = {RequestMethod.POST, RequestMethod.PUT})
  public void signDeferred (@RequestParam("file") MultipartFile file, @RequestParam("format") String format) {
    //TODO /extractSignatures
  }
}
