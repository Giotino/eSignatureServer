package io.minotti.eSignatureServer.Signature;

import io.minotti.eSignatureServer.Signature.Service.SignService;

import java.io.IOException;
import java.util.HashMap;

public class SignServicePool {
  private HashMap<String, SignService> pool = new HashMap<>();

  public void register (String name, SignService service) {
    pool.put(name, service);
  }

  private SignService get (String name) {
    return pool.get(name);
  }

  public Document sign (Document inDocument) throws IOException, SignatureException {
    SignService service = this.get(inDocument.getFormat());

    if (service == null) {
      throw new InvalidSigningFormatException(inDocument, inDocument.getFormat());
    }

    Document outDocument = service.sign(inDocument);

    inDocument.getFile().delete();

    return outDocument;
  }
}
