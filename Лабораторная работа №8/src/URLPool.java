import java.util.LinkedList;

public class URLPool {
  int maxSearchDepth; // Максимальная глубина поиска
  int countWaitThread; // Счетчик для значений количества потоков ожидания нового URL-адреса

  LinkedList<URLDepthPair> uncheckedURL = new LinkedList<>(); // Список для непросмотренных ссылок
  LinkedList<URLDepthPair> checkedURL = new LinkedList<>(); // Список для просмотреных ссылок

  public URLPool(int maxDepth) { // Параметризованный конструктор класса
    this.maxSearchDepth = maxDepth;
    countWaitThread = 0;
  }

  public synchronized URLDepthPair getPair() {
    while (uncheckedURL.size() == 0) { // Если ни один адрес в настоящее время недоступен
      countWaitThread++; // Увеличивается непосредственно перед вызовом wait()
      try {
        wait(); // Метод освобождает монитор и переводит вызывающий поток в состояние ожидания до тех пор, пока другой поток не вызовет метод notify()
      } catch (InterruptedException e) {
        System.out.println("InterruptedException");
      }
      countWaitThread--; // Уменьшается сразу после выхода из режима ожидания
    }


    return uncheckedURL.removeFirst();
    // получаем первую пару, возвращаем её и удаляем из списка непросмотренных
  }

  // Поточно-ориентированная операция добавления пары URL-глубина к пулу непросмотренных URL-адресов
  public synchronized void addPair(URLDepthPair pair) {
    if (checkedURL.contains(pair) == false) { // Если просмотренные ссылки не содержат пару URL-глубины
      checkedURL.add(pair); //добавляем эту пару к пулу
      if (pair.getDepth() < maxSearchDepth) { // если глубина пары URL-глубины меньше максимальной глубины поиска
        uncheckedURL.add(pair); // то добавить эту пару к непросмотренным ссылкам
        notify(); // Продолжает работу потока, у которого ранее был вызван метод wait()
      }
    }
  }

  public synchronized int getWait() {
    return countWaitThread;
  }

  public LinkedList<URLDepthPair> getChecked() {
    return checkedURL;
  }
}