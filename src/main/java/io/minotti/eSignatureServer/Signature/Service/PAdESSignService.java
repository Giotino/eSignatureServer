package io.minotti.eSignatureServer.Signature.Service;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.KSPrivateKeyEntry;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import io.minotti.eSignatureServer.Application;
import io.minotti.eSignatureServer.Signature.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PAdESSignService implements SignService {
  private Pkcs11Provider pkcs11Provider;
  private DSSPrivateKeyEntry privateKey;
  private PAdESSignatureParameters signParameters;
  private PAdESService signService;

  public PAdESSignService (Pkcs11Provider pkcs11Provider, String keyName) {
    this.pkcs11Provider = pkcs11Provider;

    this.signParameters = new PAdESSignatureParameters();
    this.signParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
    this.signParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
    this.signParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);

    CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
    this.signService = new PAdESService(commonCertificateVerifier);

    List<DSSPrivateKeyEntry> keys = this.pkcs11Provider.getKeys();

    for (DSSPrivateKeyEntry entry : keys) {
      if (((KSPrivateKeyEntry) entry).getAlias().equals(keyName)) {
        this.privateKey = keys.get(0);
        break;
      }
    }

    if (this.privateKey == null) {
      throw new Error(keyName + " not found");
    }

    this.signParameters.setSigningCertificate(privateKey.getCertificate());
    this.signParameters.setCertificateChain(privateKey.getCertificateChain());
  }

  public Document sign (Document document) throws IOException {
    DSSDocument toSignDocument = new FileDocument(document.getFile().getAbsolutePath());

    ToBeSigned dataToSign = this.signService.getDataToSign(toSignDocument, this.signParameters);

    DigestAlgorithm digestAlgorithm = this.signParameters.getDigestAlgorithm();
    SignatureValue signatureValue = this.pkcs11Provider.sign(dataToSign, digestAlgorithm, this.privateKey);

    DSSDocument signedDSSDocument = this.signService.signDocument(toSignDocument, this.signParameters, signatureValue);

    File file = Application.storageManager.getNewFile("out");
    signedDSSDocument.save(file.getAbsolutePath());

    return new Document(file, document.getFormat(), document.getWebhook());
  }
}