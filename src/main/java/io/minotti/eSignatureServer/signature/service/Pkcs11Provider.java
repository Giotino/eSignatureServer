package io.minotti.eSignatureServer.signature.service;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs11SignatureToken;

import java.security.KeyStore;
import java.util.List;

public class Pkcs11Provider {
  private final Pkcs11SignatureToken token;

  public Pkcs11Provider (String driverPath, String pin) {
    this.token = new Pkcs11SignatureToken(driverPath, new KeyStore.PasswordProtection(pin.toCharArray()));

    this.token.getKeys(); //Test if it's working (may be not needed)

    System.out.println("SmartCard opened");
  }

  public Pkcs11SignatureToken getToken () {
    return this.token;
  }

  public synchronized List<DSSPrivateKeyEntry> getKeys () {
    return this.token.getKeys();
  }

  public synchronized SignatureValue sign (ToBeSigned dataToSign, DigestAlgorithm digestAlgorithm, DSSPrivateKeyEntry privateKey) {
    return this.token.sign(dataToSign, digestAlgorithm, privateKey);
  }
}
