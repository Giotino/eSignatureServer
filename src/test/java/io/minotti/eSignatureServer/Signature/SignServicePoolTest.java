package io.minotti.eSignatureServer.Signature;

import io.minotti.eSignatureServer.Signature.Service.NoSignService;
import io.minotti.eSignatureServer.Signature.Service.SignService;
import io.minotti.eSignatureServer.StorageManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SignServicePoolTest {
  private StorageManager storageManager;

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  public void setUp () {
    storageManager = new StorageManager(Paths.get(temporaryFolder.getRoot().getAbsolutePath()));
  }

  @Test
  public void register () {
    SignServicePool ssp = new SignServicePool(storageManager);

    SignService ss = new NoSignService();
    ssp.register("none", ss);

    assertEquals(ssp.get("none"), ss);
  }

  @Test
  public void sign () throws IOException, SignatureException {
    File file = temporaryFolder.newFile("output.txt");
    Files.writeString(file.toPath(), "TEST");

    SignServicePool ssp = new SignServicePool(storageManager);

    SignService ss = new NoSignService();
    ssp.register("none", ss);

    Document id = new Document(file, "none");
    Document od = ssp.sign(id);

    assertEquals(Files.readString(od.getFile().toPath()), "TEST");
    assertNotEquals(id, od);
  }
}