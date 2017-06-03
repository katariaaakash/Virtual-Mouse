import javax.swing.*;

/**
 * Created by aakashkataria on 16/03/17.
 */
public class new_hsv_sliders {
    public static JSlider H_min = new JSlider(0, 255);
    public static JSlider H_max = new JSlider(0, 255);
    public static JSlider S_min = new JSlider(0, 255);
    public static JSlider S_max = new JSlider(0, 255);
    public static JSlider V_min = new JSlider(0, 255);
    public static JSlider V_max = new JSlider(0, 255);
    public static JSlider rect_dimes = new JSlider(0, 1000);
    public static JSlider threshold = new JSlider(0, 1000);
    public static JToggleButton toggleButton = new JToggleButton();
    public static JLabel H_min_label = new JLabel("H_min");
    public static JLabel H_max_label = new JLabel("H_max");
    public static JLabel S_min_label = new JLabel("S_min");
    public static JLabel S_max_label = new JLabel("S_max");
    public static JLabel V_min_label = new JLabel("V_min");
    public static JLabel V_max_label = new JLabel("V_max");
    public static JLabel rect_dims_label = new JLabel("Rect_DIMS");
    public static JLabel threshold_label = new JLabel("Threshold");
    public static JLabel start_tracking = new JLabel("Start");

    public new_hsv_sliders(JPanel configurationpanel){
        configurationpanel.add(H_min_label);
        configurationpanel.add(H_min);
        configurationpanel.add(H_max_label);
        configurationpanel.add(H_max);
        configurationpanel.add(S_min_label);
        configurationpanel.add(S_min);
        configurationpanel.add(S_max_label);
        configurationpanel.add(S_max);
        configurationpanel.add(V_min_label);
        configurationpanel.add(V_min);
        configurationpanel.add(V_max_label);
        configurationpanel.add(V_max);
        configurationpanel.add(rect_dims_label);
        configurationpanel.add(rect_dimes);
        configurationpanel.add(threshold_label);
        configurationpanel.add(threshold);
        configurationpanel.add(toggleButton);
    }
}
