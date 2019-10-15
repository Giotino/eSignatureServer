package io.minotti.eSignatureServer.validation;

import io.minotti.eSignatureServer.signature.Document;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ValidatorTest {

  @Test
  public void isSignedBy () {
    Validator v = new Validator();
    Document d = new Document(new File("test_files/signed.pdf"),"pades");

    boolean r1 = v.isSignedBy(d, "IT:RBASFN64T30H501H");
    assertTrue(r1);

    boolean r2 = v.isSignedBy(d, "INVALID");
    assertFalse(r2);
  }
}