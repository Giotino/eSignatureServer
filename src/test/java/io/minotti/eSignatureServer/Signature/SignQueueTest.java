package io.minotti.eSignatureServer.Signature;

import io.minotti.eSignatureServer.Signature.Service.NoSignService;
import io.minotti.eSignatureServer.StorageManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SignQueueTest {
  private SignServicePool signServicePool;

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  public void setUp () {
    signServicePool = new SignServicePool(new StorageManager(Paths.get(temporaryFolder.getRoot().getAbsolutePath())));
    signServicePool.register("none", new NoSignService());
  }

  @Test
  public void enqueue () throws IOException {
    File file = temporaryFolder.newFile("output.txt");
    Files.writeString(file.toPath(), "TEST");

    SignQueue sq = new SignQueue(signServicePool);
    Document id = new Document(file, "none");
    sq.enqueue(id);

    Document d1 = sq.dequeue();
    assertEquals(d1.getFormat(), "none");

    Document d2 = sq.dequeue();
    assertNull(d2);
  }

  @Test
  public void signNext () throws IOException, SignatureException {
    File file = temporaryFolder.newFile("output.txt");
    Files.writeString(file.toPath(), "TEST");

    SignQueue sq = new SignQueue(signServicePool);
    Document id = new Document(file, "none");
    sq.enqueue(id);

    Document d1 = sq.signNext();
    assertEquals(d1.getFormat(), "none");
    assertEquals(Files.readString(d1.getFile().toPath()), "TEST");
    assertNotEquals(id, d1);

    Document d2 = sq.signNext();
    assertNull(d2);
  }
}
