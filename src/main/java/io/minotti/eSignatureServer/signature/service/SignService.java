package io.minotti.eSignatureServer.signature.service;

import java.io.File;
import java.io.IOException;

public interface SignService {
  void sign (File input, File output) throws IOException;
}
