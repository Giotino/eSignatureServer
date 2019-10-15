package io.minotti.eSignatureServer.Signature.Service;

// TEST ONLY

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class NoSignService implements SignService {
  public void sign (File input, File output) throws IOException {
    FileUtils.copyFile(input, output);
  }
}
