import sun.awt.image.BufferedImageDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Created by aakashkataria on 01/01/17.
 */
public class image_panel extends JPanel {
    private BufferedImage image;
    public image_panel(){
        super();
    }
    public void setimage(BufferedImage img){
        image = img;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(this.image == null){
            return;
        }
        g.drawImage(this.image, 10, 10, this.image.getWidth(), this.image.getHeight(), null);
        g.dispose();
    }
}
