package engine.faceDetection;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

public class FaceDetect {

    public boolean userPresent(){
        CamController camController = new CamController();
        FaceDetect faceDetect = new FaceDetect();
        // Picture snap. imageName - the name for the image when saving
        camController.takePicture("image");
        // Facial detection. imageName - the name of the image being scanned
        return faceDetect.faceDetected("image");
    }

    public static void main(String[] args) {
        try{
            System.out.println("Face detected: " + new FaceDetect().userPresent());
        }
        catch (Throwable e){
            e.printStackTrace();
        }
    }

    public boolean faceDetected(String imageName){
        // Load OpenCV core lib
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Define the cascade classifier, based on the pretrained model, provided by OpenCV
        CascadeClassifier classifier = new CascadeClassifier(xmlFilePath());

        // Read image, save in Mat matrix
        String fullPath = imageFilePath(imageName);
        Mat input = Imgcodecs.imread(fullPath);

        // Detect faces, store in matrices if so.
        MatOfRect faceDetections = new MatOfRect();
        classifier.detectMultiScale(input, faceDetections);

        return faceDetections.toArray().length > 0;

    }

    private String imageFilePath(String imageName){
        String targetDirectory = "images/face/";
        String absolutePath = new File("").getAbsolutePath();
        return absolutePath + "/" + targetDirectory + imageName + ".jpg";
    }

    private String xmlFilePath(){
        String targetDirectory = "opencv/sources/data/haarcascades/";
        String absolutePath = new File("").getAbsolutePath();
        return absolutePath + "/" + targetDirectory + "haarcascade_frontalface_default.xml";
    }
}
