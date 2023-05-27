package engine.faceDetection;

import org.bytedeco.javacv.*;

public class CamController {
    private final FrameGrabber grabber;

    public CamController() {
        grabber = new OpenCVFrameGrabber(0);
    }

    public Frame takePicture(){
        try {
            grabber.start();

            Thread.sleep(1000);

            final var frame = grabber.grab();

            grabber.close();

            return frame;

        } catch (FrameGrabber.Exception | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}