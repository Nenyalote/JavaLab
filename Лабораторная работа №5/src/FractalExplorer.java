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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;


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
    listener listener = new listener();
    button.addActionListener(listener);

    JComboBox box = new JComboBox();
    FractalGenerator mandelbrotFractal = new Mandelbrot();
    box.addItem(mandelbrotFractal);
    FractalGenerator tricornFractal = new Tricorn();
    box.addItem(tricornFractal);
    FractalGenerator burningShipFractal = new BurningShip();
    box.addItem(burningShipFractal);

    listener fractalChooser = new listener();
    box.addActionListener(fractalChooser);

    JPanel panel = new JPanel();
    JLabel label = new JLabel("Fractal");
    panel.add(label);
    panel.add(box);
    display.add(panel);
    jImageDisplay.add(panel, BorderLayout.NORTH);

    JPanel southPanel = new JPanel();
    southPanel.add(button);
    JButton saveButton = new JButton("Save Image");
    listener saveListener =new listener();
    saveButton.addActionListener(saveListener);
    southPanel.add(saveButton);

    jImageDisplay.add(southPanel, BorderLayout.SOUTH); //сдвигаем ее в южную часть экрана

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

  private class listener implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      if (e.getSource() instanceof JComboBox){
        JComboBox source = (JComboBox) e.getSource();
        fractal = (FractalGenerator) source.getSelectedItem();
        fractal.getInitialRange(range);
        drawFractal();
      }
      if (action.equals("Reset")){
        fractal.getInitialRange(range);
        drawFractal();
      }
      if (action.equals("Save Image")){
        JFileChooser chooser = new JFileChooser();
        FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
        chooser.setFileFilter(extensionFilter);
        chooser.setAcceptAllFileFilterUsed(false);

        int selection = chooser.showSaveDialog(display);

        if (selection == JFileChooser.APPROVE_OPTION) {

          //Класс javax.imageio.ImageIO обеспечивает простые операции загрузки и сохранения изображения
          java.io.File file = chooser.getSelectedFile();
          String file_name = file.toString();
          // Попытка сохранить фрактал на диск
          try {
            BufferedImage displayImage = display.getImage();
            javax.imageio.ImageIO.write(displayImage, "png", file);
          }
          // Проинформировать пользователя об ошибке через диалоговое окно
          catch (Exception exception) {
            JOptionPane.showMessageDialog(display, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
          }
        }
        // Если операция сохранения файла не APPROVE_OPTION
        else return;
      }
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
