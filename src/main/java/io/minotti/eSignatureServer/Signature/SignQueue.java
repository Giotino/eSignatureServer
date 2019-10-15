package io.minotti.eSignatureServer.Signature;

import io.minotti.eSignatureServer.Application;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SignQueue {
  private LinkedBlockingQueue<Document> queue = new LinkedBlockingQueue<>();

  public void enqueue (Document document) {
    queue.add(document);
  }

  Document signNext () throws IOException, SignatureException {
    Document document = queue.poll();
    if (document == null)
      return null;

    return Application.signServicePool.sign(document);
  }
}
