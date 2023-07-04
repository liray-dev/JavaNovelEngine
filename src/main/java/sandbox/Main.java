package sandbox;

import java.awt.*;
import java.net.URI;

public class Main {

    public static void main(String[] args) {
        String url = "https://krisha.kz/a/show/675841776";

        for (int i = 0; i < 200; i++) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(String url) {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new URI(url);
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
