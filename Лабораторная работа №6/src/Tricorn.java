import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class Tricorn extends FractalGenerator {

  public void getInitialRange(Rectangle2D.Double range) {
    range.x = -2;
    range.y = -2;
    range.width = 4;
    range.height = 4;
  }

  public static final int MAX_ITERATIONS = 2000;

  public int numIterations(double x, double y) {
    int iterations = 0;
    double real = 0;
    double im = 0;

    while ((iterations < MAX_ITERATIONS) && (real * real + im * im) < 4) {

      double complex_realUpdated = real * real - im * im + x; //для реальной составляющей
      double complex_imaginaryUpdated = -2 * real * im + y; //для комплексной составляющей
      real = complex_realUpdated;
      im = complex_imaginaryUpdated;
      iterations += 1; // После обновления координат x и y, итерация +1
    }
    if (iterations == MAX_ITERATIONS)
    {
      return -1;
    }
    return iterations;
  }

  public String toString(){
    return "Tricorn";
  }
}
