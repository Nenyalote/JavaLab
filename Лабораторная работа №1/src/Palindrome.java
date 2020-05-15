import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Palindrome {

  public static void main(String[] args) throws IOException {
    for (int i=0; i<args.length; i++){
      String s = args[i];
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Введите число слов");
    int n = Integer.parseInt(reader.readLine());
    String [] array = new String[n];   //Создаем массив для слов, которые нужно проверить на принадлежность к палиндромам
    System.out.println("Введите слова");
    for (int i=0; i<n; i++){
      array[i] = reader.readLine();   //Заполняем словами с клавиатуры
    }

    for (int i=0; i<n; i++){
      boolean res = isPalindrome(array[i]);  //получаем результат работы isPalindrome
      if (res==true){
        System.out.println(array[i]+" палиндром");
      }
      if (res==false){
        System.out.println(array[i]+" не палиндром");
      }
    }

  }

  public static String reverseString(String s){
    int len = s.length();     //Считываем длину строки
    String reverse="";        //Обнуляем строку
    for (int i=0; i<len; i++){  //От 0 до 3 символа строки
      reverse = reverse+s.charAt(len-i-1); //Прибавляем к нулевой строке последний, предпоследний и т.д. символы данной строки
    }
    return reverse;
  }

  public static boolean isPalindrome(String s){
    boolean res = false;              //Результат - ложь
    String reverse = reverseString(s); //Переворачиваем строку s
    if (s.equals(reverse)){        //Если s совпадает с перевернутой строкой
      res = true;                  //Результат - истина
    }
    return res;
  }
}
