import java.awt.Point;

public class Point3d {
  private double xCoord;
  private double yCoord;
  private double zCoord;

  public Point3d (double x, double y, double z){
    xCoord = x;
    yCoord = y;
    zCoord = z;
  }

  public Point3d(){
    this(0,0,0);
  }

  public double getxCoord (){
    return xCoord;
  }

  public double getyCoord (){
    return yCoord;
  }

  public double getzCoord(){
    return zCoord;
  }

  public void setyCoord(double y){
    yCoord = y;
  }

  public void setzCoord(double z){
    zCoord = z;
  }

  public static boolean pointCompare (Point3d a, Point3d b){
    boolean res = false;
    double xa = a.getxCoord();  // Получаю координаты точек
    double ya = a.getyCoord();
    double za = a.getzCoord();
    double xb = b.getxCoord();
    double yb = b.getyCoord();
    double zb = b.getzCoord();
    if ((xa==xb)&&(ya==yb)&&(za==zb)){  //Если координаты совпадают
      res = true;                       //Точки равны
    }
    return res;
  }

  public static double distanceTo(Point3d a, Point3d b){
    double xa = a.getxCoord();
    double ya = a.getyCoord();
    double za = a.getzCoord();
    double xb = b.getxCoord();
    double yb = b.getyCoord();
    double zb = b.getzCoord();

    double distance = Math.sqrt((Math.pow((xa-xb),2))+(Math.pow((ya-yb),2))+(Math.pow((za-zb),2)));

    return distance;
  }
}
