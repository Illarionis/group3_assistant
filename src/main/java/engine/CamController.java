package engine;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class CamController {

    private FrameGrabber grabber;
    private OpenCVFrameConverter.ToIplImage converter;
    private Frame frame = null;
    private IplImage img = null;

    private void setGrabberDefault(){
        this.grabber = new OpenCVFrameGrabber(0);
    }
    private void setConverterDefault(){
        this.converter = new OpenCVFrameConverter.ToIplImage();
    }
    private void grabPicture(){
        try {
            grabber.start();
            //Thread.sleep(1000);
            frame = grabber.grab();

        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
    private void convert(){
        img = converter.convert(frame);
    }
    private void saveImage(){
        cvSaveImage("face_pic.jpg", img);
    }

    public void takePicture(){
        this.setGrabberDefault();
        this.setConverterDefault();
        this.grabPicture();
        this.convert();
        this.saveImage();
    }

    public static void main(String[] args) {
        CamController cam = new CamController();
        cam.takePicture();
    }


}
