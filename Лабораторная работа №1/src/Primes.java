public class Primes {

  public static void main(String[] args) {
    int i;
    for(i=1; i<100; i++){
      boolean y=isPrime(i);     //Если результат - простое число
      if (y==true){
        System.out.println(i);  //Печатаем число
      }
    }
  }
  public static boolean isPrime(int n){
    int k=0;
    boolean res=false;           //Результат - не простое число
    for (int i=1; i<100; i++) {  //перебираем делимые
      if (n % i == 0) {          //Если число делится без остатка, увеличиваем счетчик делений
        k = k + 1;
      }
    }
    if (k<3){                   //Если число делится на менее, чем 2 числа от 1 до 100, т.е. на 1 и на самое себя
      res=true;                 //Результат - простое число
    }
    return res;
  }
}

