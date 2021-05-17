import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * ================================================
 * Author: Maverick G. Fabroa
 * ================================================
 * Date: May 14, 2021
 * ================================================
 * Java SDK Version: 11
 * ================================================
 */

/**
 * Custom Properties
 */
public class MavyDataEntryProps {
    /**
     * Colors
     */
    public static final Color DARK_COLOR = new Color(0xFF1C212E);
    public static final Color SURFACE_COLOR = new Color(0xFF272B39);
    public static final Color FORE_COLOR = new Color(0xFF8A98B6);
    public static final Color DISABLED_COLOR = new Color(0xFF3C414E);

    public static final Color LIGHTRED_COLOR = new Color(0xFFF08070);
    public static final Color LIGHTYELLOW_COLOR = new Color(0xFFFBC02D);
    public static final Color LIGHTGREEN_COLOR = new Color(0xFF5EC479);
    public static final Color LIGHTBLUE_COLOR = new Color(0xFF3B8EEA);

    public static final Color RED_COLOR = new Color(0xFFDB4437);
    public static final Color YELLOW_COLOR = new Color(0xFFF4B400);
    public static final Color GREEN_COLOR = new Color(0xFF0F9D58);
    public static final Color BLUE_COLOR = new Color(0xFF4285F4);

    public static final Color WHITE_COLOR = new Color(255, 255, 255, 225);

    public static final Color HIGHLIGHT_COLOR = new Color(0xFF9d0f79);
    
    public static final Border PADDING = BorderFactory.createEmptyBorder(16, 16, 16, 16);

    /**
     * Create custom font with size
     * 
     * @param size
     * @return Font
     */
    public static Font createFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    /**
     * Check if one of string argument is empty
     * 
     * @param fields
     * @return boolean
     */
    public static boolean isBlank(String ...fields) {
        // Iterate through arguments
        for (String field : fields) {
            // Check if an argument string is blank
            if (field.isBlank()) {
                return true;
            }
        }

        // All of arguments are not blank
        return false;
    }
}