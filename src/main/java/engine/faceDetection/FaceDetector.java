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
//    Sasha's latest histogram changes
//    public boolean faceDetected(String imageName){
//        // Load OpenCV core lib
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        // Define the cascade classifier, based on the pretrained model, provided by OpenCV
//        CascadeClassifier classifier = new CascadeClassifier(xmlFilePath());
//
//        // Histogram equalization
//        String imageNameHistEq = histogramEqualization(imageName);
//
//        // Read image, save in Mat matrix
//        String fullPathBasic = imageFilePath(imageName);
//        String fullPathHist = imageFilePath(imageNameHistEq);
//
//        Mat inputBasic = Imgcodecs.imread(fullPathBasic);
//        Mat inputHist = Imgcodecs.imread(fullPathHist);
//
//        // Detect faces, store in matrices
//        MatOfRect faceDetectionsBasic = new MatOfRect();
//        MatOfRect faceDetectionsHist = new MatOfRect();
//
//        classifier.detectMultiScale(inputBasic, faceDetectionsBasic);
//        classifier.detectMultiScale(inputHist, faceDetectionsHist);
//
//        return (faceDetectionsBasic.toArray().length > 0) || (faceDetectionsHist.toArray().length > 0);
//    }
//
//    private String histogramEqualization(String imageName){
//        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//        String fullPath = imageFilePath(imageName);
//
//        Mat input = Imgcodecs.imread(fullPath, Imgcodecs.IMREAD_GRAYSCALE);
//        Mat output = new Mat(input.rows(), input.cols(), input.type());
//
//        // 1. Split image into channels
//        // 2. Equalize each
//        // 3. Merge into a single Mat
//        // 4. Save Mat as jpg
//
//        Imgproc.equalizeHist(input, output);
//
//        String newImageName = imageName + "_histogram_equalized";
//
//        String newFullPath = imageFilePath(newImageName);
//        Imgcodecs.imwrite(newFullPath, output);
//
//        return newImageName;
//    }
