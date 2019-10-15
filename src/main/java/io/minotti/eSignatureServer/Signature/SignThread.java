package io.minotti.eSignatureServer.Signature;

import io.minotti.eSignatureServer.Application;
import io.minotti.eSignatureServer.Server.SignController;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SignThread extends Thread {
  private static final Logger logger = LoggerFactory.getLogger(SignController.class);
  private static final int CONNECTION_TIMEOUT_MS = 1000;

  private boolean running = true;
  private SignQueue signQueue;

  public SignThread (SignQueue signQueue) {
    this.signQueue = signQueue;
  }

  public void stopRunning () {
    this.running = false;
  }

  private void callback (Document document) {
    this.callback(document, null);
  }

  private void callback (Document document, String error) {
    HttpClient client = HttpClientBuilder.create().build();

    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
        .setConnectTimeout(CONNECTION_TIMEOUT_MS)
        .setSocketTimeout(CONNECTION_TIMEOUT_MS)
        .build();

    HttpPost httpPost = new HttpPost(document.getWebhook());
    httpPost.setConfig(requestConfig);

    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    if (error != null) {
      entityBuilder.addPart("status", new StringBody("false", ContentType.TEXT_PLAIN));
      entityBuilder.addPart("error", new StringBody(error, ContentType.TEXT_PLAIN));
    } else {
      entityBuilder.addPart("status", new StringBody("true", ContentType.TEXT_PLAIN));
      entityBuilder.addPart("file", new FileBody(document.getFile()));
    }

    httpPost.setEntity(entityBuilder.build());

    try {
      HttpResponse response = client.execute(httpPost);
    } catch (IOException e) {
      logger.error("Failed to connect to the webhook (" + document.getWebhook() + "), file will be deleted anyway");
    }

    document.getFile().delete();
  }

  public void run () {
    while (this.running) {
      try {
        Document document = signQueue.signNext();
        if (document == null) {
          Thread.sleep(1000);
        } else {
          this.callback(document);
        }
      } catch (IOException e) {
        logger.error(e.toString());
      } catch (SignatureException e) {
        this.callback(e.getDocument(), e.toString());
        logger.warn(e.toString());
      } catch (InterruptedException e) {
        //Don't care
      }
    }
  }
}
