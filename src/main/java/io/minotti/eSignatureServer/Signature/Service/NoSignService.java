package io.minotti.eSignatureServer.Signature.Service;

// TEST ONLY

import io.minotti.eSignatureServer.Application;
import io.minotti.eSignatureServer.Signature.Document;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class NoSignService implements SignService {
  public Document sign (Document document) throws IOException {
    File file = Application.storageManager.getNewFile("out");
    FileUtils.copyFile(document.getFile(), file);
    return new Document(file, document.getFormat(), document.getWebhook());
  }
}
