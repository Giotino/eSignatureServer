package io.minotti.eSignatureServer.Signature;

import java.io.File;

public class Document {
  private File file;
  private String format;
  private String webhook;

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
