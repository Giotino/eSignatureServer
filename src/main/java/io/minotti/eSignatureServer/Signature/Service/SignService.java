package io.minotti.eSignatureServer.Signature.Service;

import io.minotti.eSignatureServer.Signature.Document;
import io.minotti.eSignatureServer.Signature.SignatureException;

import java.io.File;
import java.io.IOException;

public interface SignService {
  void sign (File input, File output) throws IOException;
}
