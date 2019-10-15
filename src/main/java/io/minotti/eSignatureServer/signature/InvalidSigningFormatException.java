package io.minotti.eSignatureServer.signature;

class InvalidSigningFormatException extends SignatureException {
  private final String format;

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
