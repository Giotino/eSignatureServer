package io.minotti.eSignatureServer.Signature.Service;

import io.minotti.eSignatureServer.Signature.Document;
import io.minotti.eSignatureServer.Signature.SignatureException;

import java.io.IOException;

public interface SignService {
  Document sign (Document document) throws IOException, SignatureException;
}
