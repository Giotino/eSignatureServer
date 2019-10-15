package io.minotti.eSignatureServer;

import java.io.File;
import java.nio.file.Path;

public class StorageManager {
  private final Path basePath;

  public StorageManager (Path basePath) {
    this.basePath = basePath;
  }

  public File getNewFile (String context) {
    String contextPath = this.basePath.resolve(context).toString();
    new File(contextPath).mkdirs();

    synchronized (this.basePath) {
      File file;
      do {
        file = new File(contextPath, genName());
      } while (file.exists());

      return file;
    }
  }

  public File getNewFile () {
    return this.getNewFile("base");
  }

  private String genName () {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder(24);
    for (int i = 0; i < 24; i++) {
      int index = (int) (AlphaNumericString.length() * Math.random());
      sb.append(AlphaNumericString.charAt(index));
    }
    return sb.toString();
  }
}
