package engine.faceDetection;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;
import java.nio.file.Path;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class FrameSaver {
    public String save(Frame f, String directory, String fileName) {
        final File dir = new File(directory);
        if (!dir.exists() && !dir.mkdirs()) throw new RuntimeException("Failed to create directory: " + directory);

        final Path p = Path.of(directory, fileName);

        try {
            final File imgFile = p.toFile();
            if (!imgFile.exists() && !imgFile.createNewFile()) throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final String s = p.toString();

        final var converter = new OpenCVFrameConverter.ToIplImage();
        final IplImage image = converter.convert(f);
        cvSaveImage(s, image);

        return s;
    }
}
