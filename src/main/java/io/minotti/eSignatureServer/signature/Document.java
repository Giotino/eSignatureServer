package io.minotti.eSignatureServer.signature;

import java.io.File;

public class Document {
  private final File file;
  private final String format;
  private final String webhook;

  public Document (File file, String format) {
    this.file = file;
    this.format = format;
    this.webhook = null;
  }

  public Document (File file, String format, String webhook) {
    this.file = file;
    this.format = format;
    this.webhook = webhook;
  }

  public File getFile () {
    return this.file;
  }

  public String getFormat () {
    return this.format;
  }

  public String getWebhook () {
    return this.webhook;
  }
}
