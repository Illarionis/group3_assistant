package engine.faceDetection;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

public class FaceDetector {
    public boolean detectFace(String filePath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat m = Imgcodecs.imread(filePath);
        return detectFace(m);
    }

    public boolean detectFace(Frame f) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        var converter = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
        Mat m = converter.convert(f);
        return detectFace(m);
    }

    public boolean detectFace(Mat m){
        // Define the cascade classifier, based on the pretrained model, provided by OpenCV
        CascadeClassifier classifier = new CascadeClassifier(xmlFilePath());

        // Detect faces, store in matrices if so.
        MatOfRect faceDetections = new MatOfRect();
        classifier.detectMultiScale(m, faceDetections);

        return faceDetections.toArray().length > 0;
    }

    private String xmlFilePath(){
        String targetDirectory = "opencv/sources/data/haarcascades/";
        String absolutePath = new File("").getAbsolutePath();
        return absolutePath + "/" + targetDirectory + "haarcascade_frontalface_default.xml";
    }
}
