package ivp;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public final class FrameSaver {

    public void save(File file, Frame frame) {
        final var converter = new OpenCVFrameConverter.ToIplImage();
        final IplImage image = converter.convert(frame);
        cvSaveImage(file.getPath(), image);
    }
}
