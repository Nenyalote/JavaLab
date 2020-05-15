import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class JImageDisplay extends javax.swing.JComponent {
  private java.awt.image.BufferedImage image;

  public JImageDisplay(int width, int heigth){
    this.image = new java.awt.image.BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
    setPreferredSize(new Dimension(width, heigth));
  }

  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
  }

  public void clearImage(){
    for (int i = 0; i<image.getHeight(); i++){ //Проходим по ширине и высоте картинки
      for (int j = 0; j<image.getWidth(); i++){
        image.setRGB(i,j,0); //устанавливаем цвет каждого пикселя на 0
      }
    }
  }

  public void drawPixel(int x, int y, int rgbColor){
    image.setRGB(x,y,rgbColor); //устанавливаем цвет пикселя на заданный
  }

}
