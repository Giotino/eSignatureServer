package io.minotti.eSignatureServer.Signature;

public class SignatureException extends Exception {
  private Document document;

  SignatureException (Document document) {
    super("");

    this.document = document;
  }

  public Document getDocument () {
    return document;
  }
}
