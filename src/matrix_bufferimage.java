import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by aakashkataria on 01/01/17.
 */
public class matrix_bufferimage {
    Mat matrix;
    MatOfByte mob;
    String ext;
    public matrix_bufferimage(){
    }
    public matrix_bufferimage(Mat matrix, String extention){
        matrix = matrix;
        ext = extention;
    }
    public void setMatrix(Mat matrix_a, String extention){
        matrix = matrix_a;
        ext = extention;
        mob = new MatOfByte();
    }
    public BufferedImage getbufferedimage(){
        Imgcodecs.imencode (ext, matrix, mob);
        byte[] byteArray = mob.toArray();
        BufferedImage bufferedImage = null;
        try{
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
