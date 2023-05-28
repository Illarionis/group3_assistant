package engine.faceDetection;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;


public class FaceDetector {
    private Frame frameBasic;
    private Frame frameHist;

    public FaceDetector(){
        this.frameBasic = null;
        this.frameHist = null;
    }

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

    public boolean detectFace(Mat input){
        // Converter for converting matrices into images
        var converter = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        // Histogram equalized image
        Mat inputHist = histogramEqualization(input);

        // Save the input image and the hist eq image as Frames
        this.setFrameBasic(converter.convert(input));
        this.setFrameHist(converter.convert(inputHist));

        // Define the cascade classifier, based on the pretrained model, provided by OpenCV
        CascadeClassifier classifier = new CascadeClassifier(xmlFilePath());

        // Matrices of rectangles to store the detected face areas
        MatOfRect faceDetectionsBasic = new MatOfRect();
        MatOfRect faceDetectionsHist = new MatOfRect();

        // Detect faces, store results in matrices
        classifier.detectMultiScale(input, faceDetectionsBasic);
        classifier.detectMultiScale(inputHist, faceDetectionsHist);

        // Boolean flags for face detection on the input and hist image
        boolean faceDetectedBasic = faceDetectionsBasic.toArray().length > 0;
        boolean faceDetectedHist = faceDetectionsHist.toArray().length > 0;

        // If face found - convert Mat to Frame
        if (faceDetectedBasic || faceDetectedHist){
            if (faceDetectedBasic){
                Mat matRectangleBasic = drawRectangle(faceDetectionsBasic, input);
                Frame frame = converter.convert(matRectangleBasic);
                this.setFrameBasic(frame);
            }
            if (faceDetectedHist){
                Mat matRectangleHist = drawRectangle(faceDetectionsHist, inputHist);
                Frame frame = converter.convert(matRectangleHist);
                this.setFrameHist(frame);
            }
        }
        return faceDetectedBasic || faceDetectedHist;
    }
    private Mat histogramEqualization(Mat input){
        Mat inputHist = input.clone();
        Mat output = new Mat(input.rows(), input.cols(), input.type());

        Imgproc.equalizeHist(input, output);
        return output;
    }

    // Draw a rectangle around the face(s)
    private Mat drawRectangle(MatOfRect rectangles, Mat input){
        for (Rect rect : rectangles.toArray()) {
            Imgproc.rectangle(
                    input,
                    new Point( rect.x, rect.y ),
                    new Point( rect.width + rect.x, rect.height + rect.y ),
                    new Scalar( 255, 0, 0 )
            );
        }
        return input;
    }

    private String xmlFilePath(){
        String targetDirectory = "opencv/sources/data/haarcascades/";
        String absolutePath = new File("").getAbsolutePath();
        return absolutePath + "/" + targetDirectory + "haarcascade_frontalface_default.xml";
    }

    public Frame getFrameBasic() {
        return this.frameBasic;
    }
    public Frame getFrameHist() {
        return this.frameHist;
    }
    public void setFrameBasic(Frame frameBasic) {
        this.frameBasic = frameBasic;
    }
    public void setFrameHist(Frame frameHist) {
        this.frameHist = frameHist;
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

