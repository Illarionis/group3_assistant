package engine;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class CamController {
    private final FrameGrabber frameGrabber;

    public CamController() {
        frameGrabber = new OpenCVFrameGrabber(0);
    }

    public Frame takePicture(){
        try {
            frameGrabber.start();

            Thread.sleep(1000);

            final var frame = frameGrabber.grab();

            frameGrabber.close();

            return frame;

        } catch (FrameGrabber.Exception | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewFace(String personName){
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabber.start();
            for(int i = 0; i < 20; i++){
                IplImage image = converter.convert(grabber.grab());
                cvSaveImage("face recognition data/" + personName + " (" + i + ")" + ".jpg", image);
                Thread.sleep(50);
            }
            grabber.close();

        } catch (FrameGrabber.Exception | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}