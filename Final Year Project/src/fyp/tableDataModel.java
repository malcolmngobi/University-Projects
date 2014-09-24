package fyp;

import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Isabirye Malcolm Ngobi
 */
public class tableDataModel {

    private Image jfxImage;
    private ImageView image;
    private BufferedImage bImage;

    public tableDataModel(ImageView image) {
        this.image = image;
    }

    public tableDataModel(BufferedImage bImage) {
        this.bImage = bImage;
        this.jfxImage = SwingUtils.convertToFxImage(bImage);
        this.image = new ImageView(jfxImage);
    }

    public ImageView getImage() {
        return image;
    }

    public BufferedImage getbImage() {
        return bImage;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
