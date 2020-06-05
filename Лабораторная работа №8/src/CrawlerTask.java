import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class CrawlerTask implements Runnable {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите адрес сайта (например, www.google.com): ");
    String host1 = scanner.nextLine();
    String host = "https://" + host1 + "/";
    System.out.println("Введите максимальную глубину поиска: ");
    int maxSearchDepth = scanner.nextInt();
    System.out.println("Введите количество потоков (от них будет зависеть время поиска, чем больше - тем быстрее): ");
    int countThread = scanner.nextInt();

    URLPool pool = new URLPool(maxSearchDepth);
    pool.addPair(new URLDepthPair(host, 0));

    for (int m = 0; m < countThread; m++) {
      // Каждой задаче поискового робота даем ссылку на созданный пул URL-адресов
      Thread thread = new Thread(new CrawlerTask(pool)); // Пул URL-адресов - это список, содержащий в себе URL-адреса
      thread.start(); // Запуск потока исполнения
    }
    // Если общее количество потоков равно количеству потоков, которое вернул соответствующий метод, вызываем System.exit() для завершения работы
    while (pool.getWait() != countThread) {
    }
    int checkedCount = 0; // Счетчик кол-ва просмотренных ссылок
    for (URLDepthPair pair : pool.getChecked()) {
      checkedCount++;
      System.out.printf("%s%-90s%s%n", checkedCount + " " + "просмотренная ссылка:", pair.getURL(), "глубина поиска: " + pair.getDepth());
    }
    System.exit(0);
  }

  // Каждыq экземпляр CrawlerTask должен иметь ссылку на один экземпляр класса URLPool
  URLPool pool;

  public CrawlerTask(URLPool pool) { // Конструктор класса
    this.pool = pool;
  }

  public void searchURL() throws IOException {
    Socket socket;
    URLDepthPair pair = pool.getPair(); // Получим пару URL-глубина из пула
    int depth = pair.getDepth();
    try {
      socket = new Socket(pair.getHost(), 80); // Для HTTP соединений обычно используется порт 80
    } catch (UnknownHostException e) {
      System.err.println(e);
      return;
    }
    socket.setSoTimeout(2000); // Время ожидания нового сокета

    PrintWriter myWriter = new PrintWriter(socket.getOutputStream(), true);
    myWriter.println("GET " + pair.getPath() + " HTTP/1.1");
    myWriter.println("Host: " + pair.getHost());
    myWriter.println("Connection: close");
    myWriter.println();

    // Тут реализовано чтение и проверка ссылки
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String str;
    while ((str = reader.readLine()) != null) { // Пока есть ссылка
      while (str.contains("href=\"")) { // которая содержит атрибут href (путь к файлу, на который дается ссылка (URL))
        String content;
        try {
          str = str.substring(str.indexOf("href=\"") + 6);
          content = str.substring(0, str.indexOf('\"'));
          if (!content.startsWith("http")) // Если ссылка после атрибута href не начинается с "http"
            content = content.startsWith("/") ? pair.getLink() + content : pair.getLink() + "/" + content;
        } catch (StringIndexOutOfBoundsException e) {
          break;
        }
        pool.addPair(new URLDepthPair(content, depth + 1)); // Добавим пару URL-глубина к пулу
      }
    }
    reader.close(); // Закрытие чтения
    socket.close(); // Закрытие сокета
  }

  @Override
  public void run() { // Точка входа в поток исполнения
    while (true) {
      try {
        searchURL();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}