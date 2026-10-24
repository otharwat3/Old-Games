package arcade;
import java.awt.*;
import javax.swing.*;

/** Shared palette and accessible defaults; no platform-specific font dependency. */
public final class Theme {
    public static final Color BACKGROUND = new Color(12, 15, 24);
    public static final Color SURFACE = new Color(25, 30, 44);
    public static final Color TEXT = new Color(234, 239, 250);
    public static final Color ACCENT = new Color(184, 246, 89);
    private Theme() {}
    public static void install() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) { /* Swing defaults remain usable. */ }
        for (String key : new String[]{"Panel.background", "OptionPane.background", "Button.background", "TextField.background"})
            UIManager.put(key, SURFACE);
        for (String key : new String[]{"Label.foreground", "Button.foreground", "TextField.foreground", "OptionPane.messageForeground"})
            UIManager.put(key, TEXT);
        UIManager.put("Button.font", new Font(Font.SANS_SERIF, Font.BOLD, 15));
        UIManager.put("Label.font", new Font(Font.SANS_SERIF, Font.PLAIN, 15));
    }
}
