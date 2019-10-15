package io.minotti.eSignatureServer.signature;

import io.minotti.eSignatureServer.signature.service.SignService;
import io.minotti.eSignatureServer.StorageManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SignServicePool {
  private final HashMap<String, SignService> pool = new HashMap<>();
  private final StorageManager storageManager;

  public SignServicePool (StorageManager storageManager) {
    this.storageManager = storageManager;
  }

  public void register (String name, SignService service) {
    pool.put(name, service);
  }

  public SignService get (String name) {
    return pool.get(name);
  }

  public Document sign (Document inDocument) throws IOException, SignatureException {
    SignService service = this.get(inDocument.getFormat());

    if (service == null) {
      throw new InvalidSigningFormatException(inDocument, inDocument.getFormat());
    }

    File output = storageManager.getNewFile("out");

    service.sign(inDocument.getFile(), output);

    Document outDocument = new Document(output, inDocument.getFormat(), inDocument.getWebhook());

    inDocument.getFile().delete();

    return outDocument;
  }
}
