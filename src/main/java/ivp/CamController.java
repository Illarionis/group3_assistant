package ivp;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class CamController {
    private final FrameGrabber frameGrabber;

    public CamController() {
        frameGrabber = new OpenCVFrameGrabber(0);
    }

    public Frame takePicture(){
        try {
            frameGrabber.start();
            Thread.sleep(10);
            final var frame = frameGrabber.grab();
            frameGrabber.close();
            return frame;

        } catch (FrameGrabber.Exception | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




    public void addNewFace(File dir, String nameBasis){
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabber.start();
            for(int i = 0; i < 20; i++){
                final File f = new File(dir.getPath() + "/" + nameBasis + "(" + i + ")");
                if (f.exists()) continue;
                IplImage image = converter.convert(grabber.grab());
                cvSaveImage(f.getPath(), image);
                Thread.sleep(50);
            }
            grabber.close();

        } catch (FrameGrabber.Exception | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}