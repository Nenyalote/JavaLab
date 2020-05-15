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
  private int rowsRemaining;
  private JComboBox box;
  private JButton saveButton;
  private JButton button;


  public FractalExplorer(int size){
    displaySize = size;
    fractal = new Mandelbrot();
    range = new Rectangle2D.Double();
    fractal.getInitialRange(range);
    display = new JImageDisplay(displaySize,displaySize);
  }


  public void createAndShowGUI() {
    display.setLayout(new BorderLayout());
    JFrame jImageDisplay = new JFrame("Fractal explorer"); //создаем дисплей
    jImageDisplay.add(display, BorderLayout.CENTER); //помещаем дисплей посередине
    button = new JButton("Reset"); //создаем кнопку с надписью Reset
    listener listener = new listener();
    button.addActionListener(listener);

    mouse mouse = new mouse();
    display.addMouseListener(mouse);

    jImageDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //закрытие окна по умолчанию

    box = new JComboBox();
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

    saveButton = new JButton("Save Image");
    JPanel southPanel = new JPanel();
    southPanel.add(button);
    southPanel.add(saveButton);
    jImageDisplay.add(southPanel, BorderLayout.SOUTH); //сдвигаем ее в южную часть экрана
    listener saveListener = new listener();
    saveButton.addActionListener(saveListener);

    jImageDisplay.pack ();
    jImageDisplay.setVisible (true);
    jImageDisplay.setResizable (false);
  }

  private void drawFractal(){
    enableUI(false); // Установим общее количество оставшихся строк
    rowsRemaining = displaySize; // Переберём каждую строку и вызовем перерисовку
    for (int i=0; i<displaySize; i++) {
      FractalWorker drawRow = new FractalWorker(i);
      drawRow.execute();
    }
  }

  private void enableUI(boolean val) {
    box.setEnabled(val);
    button.setEnabled(val);
    saveButton.setEnabled(val);
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
      if (rowsRemaining != 0) {
        return;
      }
      // Получение координаты х области щелчка мыши
      int x = e.getX();
      double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
      // Получение координаты у области щелчка мыши
      int y = e.getY();
      double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);
      // Увеличение фрактала
      fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
      // Перерисуем фрактал
      drawFractal();
    }
  }


  private class FractalWorker extends SwingWorker<Object,Object>{
    int yCoordinate;
    int[] pixel;

    private FractalWorker (int y) {
      yCoordinate = y;
    }

    protected Object doInBackground(){
      pixel = new int[displaySize];  // Проходим по всем пикселям в строке
      for (int i = 0; i < pixel.length; i++) {// xCoord, yCoord - координата в пространстве фрактала, x и у им соответствуют
        double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, i);
        double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);
        int iteration = fractal.numIterations(xCoord, yCoord);//для координат в области отображения
        if (iteration == -1){ //если точка не за границей, окрашиваем ее в черный
          pixel[i] = 0;
        }
        else {
          float hue = 0.7f + (float) iteration / 200f; //иначе выберем цвет по кол-ву итераций
          int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
          // Добавим в массив цвет текущего пикселя
          pixel[i] = rgbColor;
        }
      }
      return null;
    }

    protected void done(){
      for (int i = 0; i < pixel.length; i++) {
        display.drawPixel(i, yCoordinate, pixel[i]);
      }
      display.repaint(0, 0, yCoordinate, displaySize, 1); // Метод, который позволяет указать область для перерисовки фрактала
      rowsRemaining=rowsRemaining-1;
      if (rowsRemaining == 0) {
        enableUI(true);
      }
    }
  }
  public static void main(String[] args){
    FractalExplorer fractal = new FractalExplorer(800);
    fractal.createAndShowGUI();
    fractal.drawFractal();
  }
}
