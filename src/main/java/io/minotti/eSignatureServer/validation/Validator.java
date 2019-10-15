package io.minotti.eSignatureServer.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.validation.PDFDocumentValidatorFactory;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import io.minotti.eSignatureServer.signature.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Validator {
  private static final Logger logger = LoggerFactory.getLogger(Validator.class);
  private final CertificateVerifier cv;

  public Validator () {
    cv = new CommonCertificateVerifier();
  }

  /**
   * Verify if a document is signed by fiscal_code
   *
   * @param input       The input document
   * @param fiscal_code The fiscal code to verify (prepend "CC:" where "CC" is the country code, eg. "IT:ABCDEF...")
   * @return true if there's at least one valid signature by fiscal_code
   */
  public boolean isSignedBy (Document input, String fiscal_code) {
    DSSDocument toValidateDocument = new FileDocument(input.getFile().getAbsolutePath());

    SignedDocumentValidator signedDocumentValidator;
    switch (input.getFormat()) {
      case "pades": {
        signedDocumentValidator = new PDFDocumentValidatorFactory().create(toValidateDocument);
        break;
      }
      default: {
        return false; //TODO: throw exception
      }
    }
    signedDocumentValidator.setCertificateVerifier(cv);

    List<AdvancedSignature> signatures = signedDocumentValidator.getSignatures();
    for (AdvancedSignature signature : signatures) {
      List<CertificateToken> certs = signature.getCertificates();
      CertificateToken cert = certs.get(0);
      String DN = cert.getCertificate().getSubjectDN().getName();
      if (DN.contains("SERIALNUMBER=" + fiscal_code.toUpperCase())) {
        return true;
      }
    }

    return false;
  }
}
