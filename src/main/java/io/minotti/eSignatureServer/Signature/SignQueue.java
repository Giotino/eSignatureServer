package io.minotti.eSignatureServer.Signature;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SignQueue {
  private LinkedBlockingQueue<Document> queue = new LinkedBlockingQueue<>();
  private SignServicePool signServicePool;

  public SignQueue (SignServicePool signServicePool) {
    this.signServicePool = signServicePool;
  }

  public void enqueue (Document document) {
    queue.add(document);
  }

  public Document dequeue () {
    return queue.poll();
  }

  public Document getNext () {
    return queue.peek();
  }

  Document signNext () throws IOException, SignatureException {
    Document document = queue.poll();
    if (document == null)
      return null;

    return signServicePool.sign(document);
  }
}
