import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageConvertTest {
    @Test
    void test() throws IOException {
        File original = new File("/Users/kim-onji/IdeaProjects/helloworld.png");
        File output = new File("/Users/kim-onji/IdeaProjects/helloworld2.jpg");
        try (InputStream in = new FileInputStream(original)){
            BufferedImage originalImage = ImageIO.read(in);
            BufferedImage newImage = convert(originalImage, 500);
            ImageIO.write(newImage, "jpg", output);
        }
    }

    public BufferedImage convert(BufferedImage originalImage, int length) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Determine the ratio to maintain aspect ratio
        double ratio = (double) length / Math.min(originalWidth, originalHeight);

        // Calculate the new dimensions while maintaining the aspect ratio
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        // Crop to the specified length x length
        int newLength = Math.min(newWidth, newHeight);
        int x = (newWidth - newLength) / 2;
        int y = (newHeight - newLength) / 2;
        BufferedImage croppedImage = resizedImage.getSubimage(x, y, newLength, newLength);

        return croppedImage;
    }


}
