package io.minotti.eSignatureServer.signature;

public class SignatureException extends Exception {
  private final Document document;

  SignatureException (Document document) {
    super("");

    this.document = document;
  }

  public Document getDocument () {
    return document;
  }
}
