import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static org.opencv.core.Core.flip;
import static org.opencv.imgproc.Imgproc.*;

/**
 * Created by aakashkataria on 01/01/17.
 */
public class main_class {
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;
    public static double IMAGE_WIDTH;
    public static double IMAGE_HEIGHT;
    public static float IMAGE_ASPACT_RATIO;
    public static float SCREEN_ASPACT_RATIO;
    public static float SCREEN_TO_IMAGE_X_RATIO;
    public static float SCREEN_TO_IMAGE_Y_RATIO;
    public static int WIDTH_FRAME = 660;
    public static int HEIGHT_FRAME = 520;
    private static JFrame orignal_image;
    private static JFrame mousemoveconfigurationmanager;
    private static JFrame clickconfigurationmanager;
    private static JFrame hsv_image;
    private static JFrame mousemove_binary;
    private static JFrame click_binary;
    private static VideoCapture webcamera;
    private static boolean webcampresent = true;
    private static Mat orignal_matrix_image;
    private static Mat webcamimage;
    private static Mat hsvimage;
    private static Mat mousemovebinaryimage;
    private static Mat clickbinaryimage;
    private static Mat finalmousemovebinary;
    private static Mat finalclickbinary;
    private static BufferedImage converted_image;
    private static BufferedImage converted_hsv;
    private static BufferedImage converted_mousemovebinary;
    private static BufferedImage converted_clickbinary;
    private static Robot mouse_robot;
    private static image_panel orignal_panel, hsv_panel, mousemovebinary_panel, clickbinary_panel;
    private static JPanel mousemove_configurationpanel, click_configurationpanel;
    private static new_hsv_sliders mousemove_sliders;
    private static hsv_sliders click_sliders;
    private static int erosion_size = 2;
    private static int dilation_size = 4;
    private static Point point0, point1;
    private static boolean mouse_pressed;
    public static void main(String[] args) throws AWTException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        mouse_robot = new Robot();
        setupframes();
        setupwebcam();
        finalmousemovebinary = new Mat();
        finalclickbinary = new Mat();
        webcamimage = new Mat();
        hsvimage = new Mat();
        mousemovebinaryimage = new Mat();
        clickbinaryimage = new Mat();
        matrix_bufferimage matimg = new matrix_bufferimage();
        webcamera.set(Videoio.CAP_PROP_FPS, 30);
        webcamera.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        webcamera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
        IMAGE_WIDTH = webcamera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        IMAGE_HEIGHT = webcamera.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
        SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
        IMAGE_ASPACT_RATIO = ((float)IMAGE_WIDTH / (float)IMAGE_HEIGHT);
        SCREEN_ASPACT_RATIO = ((float)SCREEN_WIDTH/(float)SCREEN_HEIGHT);
        SCREEN_TO_IMAGE_X_RATIO = ((float)SCREEN_WIDTH/(float)IMAGE_WIDTH);
        SCREEN_TO_IMAGE_Y_RATIO = ((float)SCREEN_HEIGHT/(float)IMAGE_HEIGHT);
        mouse_pressed = true;
        while(true){
            webcamera.read(webcamimage);
            orignal_matrix_image = webcamimage;
            flip(webcamimage, webcamimage, 1);
            if(!webcamimage.empty()){
                Imgproc.cvtColor(webcamimage, hsvimage, COLOR_BGR2HSV);

                matimg.setMatrix(hsvimage, ".jpg");
                converted_hsv = matimg.getbufferedimage();

                Core.inRange(hsvimage, new Scalar(click_sliders.H_min.getValue(), click_sliders.S_min.getValue(), click_sliders.V_min.getValue()), new Scalar(click_sliders.H_max.getValue(), click_sliders.S_max.getValue(), click_sliders.V_max.getValue()), clickbinaryimage);
                Core.inRange(hsvimage, new Scalar(mousemove_sliders.H_min.getValue(), mousemove_sliders.S_min.getValue(), mousemove_sliders.V_min.getValue()), new Scalar(mousemove_sliders.H_max.getValue(), mousemove_sliders.S_max.getValue(), mousemove_sliders.V_max.getValue()), mousemovebinaryimage);

                removewhitedots();
                finalmousemovebinary = manage_contours(mousemovebinaryimage, orignal_matrix_image, 0);
                finalclickbinary = manage_contours(clickbinaryimage, orignal_matrix_image, 1);

                matimg.setMatrix(mousemovebinaryimage, ".jpg");
                converted_mousemovebinary = matimg.getbufferedimage();

                matimg.setMatrix(clickbinaryimage, ".jpg");
                converted_clickbinary = matimg.getbufferedimage();

                matimg.setMatrix(orignal_matrix_image, ".jpg");
                converted_image = matimg.getbufferedimage();

                orignal_panel.setimage(converted_image);
                mousemovebinary_panel.setimage(converted_mousemovebinary);
                clickbinary_panel.setimage(converted_clickbinary);
                hsv_panel.setimage(converted_hsv);

                repaintframes();

                handlesliders();
            }
            else{
                System.out.println("Image not captured");
                break;
            }
        }
        webcamera.release();
    }

    private static Mat manage_contours(Mat binary_image, Mat orignal_image, int val){
        return findallcontors(binary_image, orignal_image, val);
    }

    private static void setupwebcam(){
        webcamera = new VideoCapture(0);
        if(!webcamera.isOpened()){
            System.out.println("Web Cam not found");
            webcampresent = false;
        }
        else{
            System.out.println(webcamera.toString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setupframes(){
        orignal_image = new JFrame("Orignal Image");
        mousemoveconfigurationmanager = new JFrame("Mouse Move Configuration Maager");
        clickconfigurationmanager = new JFrame("Click Configuration Maager");
        hsv_image = new JFrame("HSV Image");
        mousemove_binary = new JFrame("Mouse Move Binary Image");
        click_binary = new JFrame("Click Binary Image");
        orignal_image.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        mousemoveconfigurationmanager.setSize(280, 400);
        clickconfigurationmanager.setSize(280, 400);
        hsv_image.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        mousemove_binary.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        click_binary.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        orignal_image.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hsv_image.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clickconfigurationmanager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mousemoveconfigurationmanager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mousemove_binary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        click_binary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orignal_image.setVisible(true);
        mousemoveconfigurationmanager.setVisible(true);
        clickconfigurationmanager.setVisible(true);
        hsv_image.setVisible(true);
        mousemove_binary.setVisible(true);
        click_binary.setVisible(true);
        orignal_panel = new image_panel();
        mousemovebinary_panel = new image_panel();
        clickbinary_panel = new image_panel();
        hsv_panel = new image_panel();
        mousemove_configurationpanel = new JPanel();
        click_configurationpanel = new JPanel();
        orignal_image.setContentPane(orignal_panel);
        hsv_image.setContentPane(hsv_panel);
        mousemove_binary.setContentPane(mousemovebinary_panel);
        click_binary.setContentPane(clickbinary_panel);
        mousemove_sliders = new new_hsv_sliders(mousemove_configurationpanel);
        click_sliders = new hsv_sliders(click_configurationpanel);
        mousemoveconfigurationmanager.setContentPane(mousemove_configurationpanel);
        clickconfigurationmanager.setContentPane(click_configurationpanel);
    }

    private static void handlesliders(){
        click_sliders.H_min_label.setText("H_min " + click_sliders.H_min.getValue());
        click_sliders.H_max_label.setText("H_max " + click_sliders.H_max.getValue());
        click_sliders.S_min_label.setText("S_min " + click_sliders.S_min.getValue());
        click_sliders.S_max_label.setText("S_max " + click_sliders.S_max.getValue());
        click_sliders.V_min_label.setText("V_min " + click_sliders.V_min.getValue());
        click_sliders.V_max_label.setText("V_max " + click_sliders.V_max.getValue());
        click_sliders.rect_dims_label.setText("RECT_DIMS " + click_sliders.rect_dimes.getValue());
        click_sliders.rect_dims_label.setText("Threshold " + click_sliders.threshold.getValue());
        mousemove_sliders.H_min_label.setText("H_min " + mousemove_sliders.H_min.getValue());
        mousemove_sliders.H_max_label.setText("H_max " + mousemove_sliders.H_max.getValue());
        mousemove_sliders.S_min_label.setText("S_min " + mousemove_sliders.S_min.getValue());
        mousemove_sliders.S_max_label.setText("S_max " + mousemove_sliders.S_max.getValue());
        mousemove_sliders.V_min_label.setText("V_min " + mousemove_sliders.V_min.getValue());
        mousemove_sliders.V_max_label.setText("V_max " + mousemove_sliders.V_max.getValue());
        mousemove_sliders.rect_dims_label.setText("RECT_DIMS " + mousemove_sliders.rect_dimes.getValue());
        mousemove_sliders.rect_dims_label.setText("Threshold " + mousemove_sliders.threshold.getValue());
    }

    private static void repaintframes(){
        orignal_panel.repaint();
        mousemovebinary_panel.repaint();
        clickbinary_panel.repaint();
        hsv_panel.repaint();
    }

    private static void removewhitedots(){
        Mat element;
        Imgproc.medianBlur(mousemovebinaryimage, mousemovebinaryimage, 5);
        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erosion_size, erosion_size));
        Imgproc.erode(mousemovebinaryimage, mousemovebinaryimage, element);
        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilation_size, dilation_size));
        Imgproc.dilate(mousemovebinaryimage, mousemovebinaryimage, element);
        Imgproc.medianBlur(clickbinaryimage, clickbinaryimage, 5);
        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erosion_size, erosion_size));
        Imgproc.erode(clickbinaryimage, clickbinaryimage, element);
        element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilation_size, dilation_size));
        Imgproc.dilate(clickbinaryimage, clickbinaryimage, element);
    }

    private static Mat findallcontors(Mat img, Mat orignal_image, int val){
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(img, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = orignal_image;
        MatOfInt hull = new MatOfInt();
        MatOfInt4 defects = new MatOfInt4();
        ArrayList<MatOfPoint> hull_contours = new ArrayList<>();
        ArrayList<MatOfInt> hull_int_contours = new ArrayList<>();
        ArrayList<MatOfInt4> Defects = new ArrayList<>();
        for (int j = 0; j < contours.size(); j++) {
            Imgproc.convexHull(contours.get(j), hull);
            Imgproc.convexityDefects(contours.get(j), hull, defects);
            hull_int_contours.add(j, hull);
            Defects.add(j, defects);
            MatOfPoint hullContour = hull2Points(hull, contours.get(j));
            hull_contours.add(j, hullContour);
            Rect boundingBox = boundingRect(hull_contours.get(0));
            Imgproc.rectangle(drawing, boundingBox.br(), boundingBox.tl(), new Scalar(255, 0, 0), 2);
            Point center = new Point(boundingBox.x + boundingBox.width / 2, boundingBox.y + boundingBox.height / 2);
            Imgproc.circle(drawing, center, 9, new Scalar(0, 255, 0), 2);
            if (val == 0){
                point0 = center;
                if(mousemove_sliders.toggleButton.isSelected()) {
                    mouse_robot.mouseMove((int) center.x * (int) SCREEN_TO_IMAGE_X_RATIO, (int) center.y * (int) SCREEN_TO_IMAGE_Y_RATIO);
                }
            }
            else{
                point1 = center;
                if (click_sliders.toggleButton.isSelected() && !mouse_pressed){
                    mouse_pressed = true;
                    mouse_robot.mousePress(InputEvent.BUTTON1_MASK);
                }
                else if (click_sliders.toggleButton.isSelected() && mouse_pressed && boundingBox.width <= 10 && boundingBox.height <= 10){
                    mouse_pressed = false;
                    mouse_robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }
            }
        }
        return drawing;
    }

    private static void mouse_release() throws AWTException {
        Robot r = new Robot();
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private static MatOfPoint hull2Points(MatOfInt hull, MatOfPoint contour) {
        java.util.List<Integer> indexes = hull.toList();
        java.util.List<Point> points = new ArrayList<>();
        MatOfPoint point= new MatOfPoint();
        for(Integer index:indexes) {
            points.add(contour.toList().get(index));
        }
        point.fromList(points);
        return point;
    }

    public static void click(java.awt.Point p) throws AWTException, InterruptedException {
        Robot r = new Robot();
        r.mousePress(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(100);
        } catch (Exception e) {}
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
