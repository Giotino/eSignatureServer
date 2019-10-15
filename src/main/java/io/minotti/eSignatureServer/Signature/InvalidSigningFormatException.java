package io.minotti.eSignatureServer.Signature;

public class InvalidSigningFormatException extends SignatureException {
  private String format;

  InvalidSigningFormatException (Document document, String format) {
    super(document);

    this.format = format;
  }

  public String getFormat () {
    return format;
  }

  @Override
  public String toString () {
    return "Invalid signing format \"" + format + "\"";
  }
}
