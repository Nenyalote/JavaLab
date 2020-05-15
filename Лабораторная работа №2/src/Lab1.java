import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lab1 {

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Введите координаты трех точек в пространстве");
    double xa = Double.parseDouble(reader.readLine());
    double ya = Double.parseDouble(reader.readLine());
    double za = Double.parseDouble(reader.readLine());
    double xb = Double.parseDouble(reader.readLine());
    double yb = Double.parseDouble(reader.readLine());
    double zb = Double.parseDouble(reader.readLine());
    double xc = Double.parseDouble(reader.readLine());
    double yc = Double.parseDouble(reader.readLine());
    double zc = Double.parseDouble(reader.readLine());

    Point3d a = new Point3d(xa,ya,za);
    Point3d b = new Point3d(xb,yb,zb);
    Point3d c = new Point3d(xc,yc,zc);

    //Проверяю, не совпадают ли точки, и если не свопадают, вычисляю площадь
    if ((Point3d.pointCompare(a,b)==false)&&(Point3d.pointCompare(b,c)==false)&&(Point3d.pointCompare(a,c)==false)){
      double s = computeArea(a,b,c);
      System.out.println("Площадь треугольника равна "+s);
    } else {
      System.out.println("Некоторые точки совпадают");
    }

  }

  public static double computeArea(Point3d a, Point3d b, Point3d c){

    double ab = Point3d.distanceTo(a,b);  //Получаю стороны треугольника
    double bc = Point3d.distanceTo(b,c);
    double ac = Point3d.distanceTo(a,c);

    double p = (ab+bc+ac)/2;
    double s = Math.sqrt(p*(p-ab)*(p-bc)*(p-ac));

    return s;
  }

}
