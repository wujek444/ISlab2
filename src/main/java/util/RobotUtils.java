package util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RobotUtils {
    public static void click(int x, int y) throws AWTException {
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void type(String keys) throws AWTException {
        Robot robot = new Robot();

        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                        "Key code not found for character '" + c + "'");
            }
            robot.keyPress(keyCode);
            robot.delay(100);
            robot.keyRelease(keyCode);
            robot.delay(100);
        }
    }

    public static void pressEnter() throws AWTException {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void pressBackspace() throws AWTException {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_BACK_SPACE);
        r.keyRelease(KeyEvent.VK_BACK_SPACE);
    }

    public static void takeScreenshot(String screenshotPath) throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = new Robot().createScreenCapture(screenRect);
        ImageIO.write(capture, "bmp", new File(screenshotPath));
    }

    public static void setFocusToWindow(String applicationTitle, int... windowState) {
        int state = User32.SW_SHOWNORMAL; //default window state (Normal)
        if (windowState.length > 0) {
            state = windowState[0];
            switch (state) {
                default:
                case 0:
                    state = User32.SW_SHOWNORMAL;
                    break;
                case 1:
                    state = User32.SW_SHOWMAXIMIZED;
                    break;
                case 2:
                    state = User32.SW_SHOWMINIMIZED;
                    break;
            }
        }

        User32 user32 = User32.INSTANCE;
        WinDef.HWND hWnd = user32.FindWindow(null, applicationTitle);
        if (user32.IsWindowVisible(hWnd)) {
            user32.ShowWindow(hWnd, state); //.SW_SHOW);
            user32.SetForegroundWindow(hWnd);
            user32.SetFocus(hWnd);
        }
    }
}
