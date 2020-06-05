import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {

  private String host;
  private int depth;
  private URL url;

  public URLDepthPair(String host, int depth) {
    this.host = host;
    this.depth = depth;
    try {
      url = new URL(host);
    } catch (MalformedURLException e) {
      System.err.println("MalformedURLException: " + e.getMessage());
      return;
    }
  }

  // Получение URL-адреса
  public String getURL() {
    return host;
  }

  // Получение глубины
  public int getDepth() {
    return depth;
  }

  // Получение хоста
  public String getHost() {
    return url.getHost();
  }

  // Получение пути
  public String getPath() {
    return url.getPath();
  }

  // Получение ссылки
  public String getLink() {
    return url.getProtocol() + "://" + url.getHost();
  }
}