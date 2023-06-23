package engine;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;
import java.nio.file.Path;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class FrameSaver {
    private final String imagePath = "src/main/resources/ivp/temp.jpg";
    public boolean save(Frame f) {
        try {
            final File imgFile = new File(imagePath);
            if (!imgFile.exists() && !imgFile.createNewFile()) throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final var converter = new OpenCVFrameConverter.ToIplImage();
        final IplImage image = converter.convert(f);
        cvSaveImage(imagePath, image);

        return true;
    }
}
