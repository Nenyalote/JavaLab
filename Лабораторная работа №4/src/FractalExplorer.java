import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;

public class FractalExplorer {
  private int displaySize;
  private JImageDisplay display;
  private FractalGenerator fractal;
  private Rectangle2D.Double range;

  public FractalExplorer(int size){
    displaySize = size;
    display = new JImageDisplay(displaySize,displaySize);
    range = new Rectangle2D.Double();
    fractal = new Mandelbrot();
    fractal.getInitialRange(range);
  }

  public void createAndShowGUI() {
    display.setLayout(new BorderLayout());
    JFrame jImageDisplay = new JFrame("Fractal explorer"); //создаем дисплей
    jImageDisplay.add(display, BorderLayout.CENTER); //помещаем дисплей посередине
    JButton button = new JButton("Reset"); //создаем кнопку с надписью Reset
    reset reset = new reset();
    button.addActionListener(reset);
    jImageDisplay.add(button, BorderLayout.SOUTH); //сдвигаем ее в южную часть экрана

    mouse mouse = new mouse();
    display.addMouseListener(mouse);

    jImageDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //закрытие окна по умолчанию

    jImageDisplay.pack ();
    jImageDisplay.setVisible (true);
    jImageDisplay.setResizable (false);
  }

  private void drawFractal(){
    for (int i = 0; i<displaySize; i++){
      for (int j = 0; j<displaySize; j++){
        double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize,i); //считываем координаты в пространстве фрактала
        double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize,j);
        int kolIterations = fractal.numIterations(xCoord,yCoord); // считаем количество итераций

        if (kolIterations == -1){ //сли приходит -1, т.е. точка не выходит за границы
          display.drawPixel(i,j,0); //окрашиваем черным
        } else {
          float hue = 0.7f + (float) kolIterations / 200f; //ищем цвет, соответствующий количеству итераций
          int rgbColor = Color.HSBtoRGB(hue, 1f, 1f); //переводим из HSB в RGB
          display.drawPixel(i,j,rgbColor);
        }

      }
    }
    display.repaint();
  }

  private class reset implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      fractal.getInitialRange(range);
      drawFractal();
    }
  }

  private class mouse extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
      int x = e.getX(); //получаем координату точки х
      double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
      int y = e.getY();//получаем координату точки y фрактала
      double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);
      fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);// Увеличиваем фрактал
      drawFractal(); //перерисовываем фрактал
    }
  }

  public static void main(String[] args){
    FractalExplorer fractal = new FractalExplorer(800);
    fractal.createAndShowGUI();
    fractal.drawFractal();
  }
}
