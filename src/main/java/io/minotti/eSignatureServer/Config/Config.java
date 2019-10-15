package io.minotti.eSignatureServer.Config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {
  public static Config fromFile (File configFile) throws IOException {
    Gson g = new Gson();

    return g.fromJson(FileUtils.readFileToString(configFile, "utf-8"), Config.class);
  }

  public static class Server {
    public String address = "0.0.0.0";
    public Integer port = 8080;
  }
  public Server server = new Server();

  public static class Pkcs11Provider {
    public String driverPath;
    public String pin;
    public String keyName;
  }
  public Pkcs11Provider pkcs11Provider;
}
