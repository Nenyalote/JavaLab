import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class Crawler {
  public static void main(String args[]) throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите адрес сайта: ");
    String host = scanner.nextLine();
    System.out.println("Введите максимальную глубину поиска: ");
    int maxSearchDepth = scanner.nextInt();
    new Crawler(host, maxSearchDepth);
  }

  public static final String URL_PREFIX = "https://";

  Crawler(String host, int maxDepth) throws IOException {
    Socket socket = new Socket(host, 80); //создаем новый сокет из строки - имени хоста и числа - номера порта, устанавливаем соединение
    URLDepthPair pair = new URLDepthPair("https://" + host + "/"); // новая пара URL и глубина поиска
    String host1 = host;
    LinkedList<URLDepthPair> checkedURL = new LinkedList<>(); // просмотренные ссылки
    LinkedList<URLDepthPair> uncheckedURL = new LinkedList<>(); // непросмотренные ссылки
    uncheckedURL.add(pair);
    socket.setSoTimeout(2000); // время ожидания нового сокета
    while ((!uncheckedURL.isEmpty())) { // пока есть непросмотренные ссылки
      try {
        URL url = new URL(uncheckedURL.getFirst().getUrl()); //получаем URL первого непросмотренного элемента
        try {

          LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openStream()));
          String string= reader.readLine();
          while (string != null) {
            for (String content : string.split("href=\"")) //для каждой подстроки, идущей после href
              try { // Если строка содержит href и префикс, а также строка content содержит префикс
                if (string.contains("href=\"" + URL_PREFIX) && content.startsWith(URL_PREFIX)) {
                  pair = new URLDepthPair((content.substring(0, content.indexOf("\"")).split("/").length - 3), content.substring(0, content.indexOf("\"")));
                  if (pair.getDepth() <= maxDepth && pair.getUrl().contains(host1)) { // Если полученная глубина меньше максимальной и полученный URL содержится в хосте
                    int sizeUnchecked = uncheckedURL.size();  // Кол-во непросмотренных ссылок
                    int sizeChecked = checkedURL.size(); // Кол-во просмотренных ссылок
                    boolean uncheckedState = false;
                    boolean checkedState = false;
                    int count1 = 0;
                    int count2 = 0;
                    // Пока состояние непросмотренных false и счетчик меньше кол-ва непросмотренных или состояние просмотренных false и счетчик меньше кол-ва просмотренных
                    while ((uncheckedState == false && count1 < sizeUnchecked) || (checkedState == false && count2 < sizeChecked)) {
                      if (count1 < sizeUnchecked) {  // Если счетчик меньше кол-ва непросмотренных ссылок
                        if (uncheckedURL.get(count1).getUrl().contains(pair.getUrl())) {
                          uncheckedState = true; // Изменяем статус на true
                        }
                        count1++;
                      }
                      if (count2 < sizeChecked) { // Если счетчик меньше кол-ва просмотренных ссылок
                        if (checkedURL.get(count2).getUrl().contains(pair.getUrl())) {
                          checkedState = true; // Изменяем статус на true
                        }
                        count2++;
                      }
                    }
                    if (uncheckedState == false && checkedState == false) {
                      uncheckedURL.add(pair); // Добавляем пару в список непросмотренных
                    }
                  }
                }
              } catch (StringIndexOutOfBoundsException e) {
              }

            string = reader.readLine(); // Читаем следующую строку
          }
          reader.close(); // закрываем чтение
        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (MalformedURLException ex) {
        ex.printStackTrace();
      }
      socket.close(); // закрываем сокет
      checkedURL.add(uncheckedURL.getFirst());
      uncheckedURL.removeFirst(); // Удаляем ссылку из списка непросмотренных
      System.out.println("Просмотренная cсылка: " + checkedURL.getLast().getUrl()); // Вывод последней просмотренной ссылки
      System.out.println("Всего проверено ссылок: " + checkedURL.size());
    }
  }
}