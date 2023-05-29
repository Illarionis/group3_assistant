package gui;

import engine.faceDetection.CamController;
import engine.faceDetection.FaceDetector;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.bytedeco.javacv.JavaFXFrameConverter;

import java.util.concurrent.CountDownLatch;

public final class FaceDetection {
    private final Button toggleDetection;
    private final Label titleHolder;
    private final Runnable terminationProcess;
    private final ScrollPane viewport;
    private final Thread backgroundThread;
    private final VBox faceDetectionPanel;

    public FaceDetection(Factory f) {
        // Providing constant memory addresses for the face detection panel
        final String[] faceDetectionLabelValues = new String[1];
        final Image[] faceDetectionImages = new Image[1];

        // Providing a label to display the face detection value
        titleHolder = f.createLabel("DETECTION NOT STARTED");
        titleHolder.setMaxWidth(Double.MAX_VALUE);
        titleHolder.setPrefHeight(30);

        // Providing an image view to show the picture that has been captured.
        final ImageView viewContent = new ImageView();

        // Providing a scroll pane that will function as the image viewport
        viewport = f.createScrollPane(viewContent);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        // Providing a button to start the face detection processes
        toggleDetection = new Button();
        toggleDetection.setBackground(Background.EMPTY);
        toggleDetection.setMaxWidth(Double.MAX_VALUE);

        // Providing the face detection panel
        faceDetectionPanel = f.createVBox(3, titleHolder, viewport, toggleDetection);

        // Providing engine components related to face detection
        final var camController = new CamController();
        final var faceDetector = new FaceDetector();
        final var frameConverter = new JavaFXFrameConverter();

        // Providing constant memory addresses for the face detection processes
        final boolean[] previousDetectionValues = new boolean[1];
        final boolean[] runDetectionProcesses = new boolean[1];
        final CountDownLatch[] detectionCountDownLatches = new CountDownLatch[2];

        // Defining the detection value change process
        final Runnable updatePanelProcess = () -> {
            // Updating the graphical components
            titleHolder.setText(faceDetectionLabelValues[0]);
            viewContent.setImage(faceDetectionImages[0]);

            // Update the countdown latch
            if (detectionCountDownLatches[0] != null) detectionCountDownLatches[0].countDown();
        };

        // Defining the face detection process
        final Runnable faceDetectionProcess = () -> {
            // Capturing a picture through the webcam
            final var frame = camController.takePicture();

            // Performing face detection
            final boolean detected = faceDetector.detectFace(frame);

            // Manual confirmation of the detection
            System.out.println("Face detected: " + detected);

            // Update the face detection panel to show the detection state and the captured picture
            if (detected) faceDetectionLabelValues[0] = "USER HAS BEEN DETECTED";
            else faceDetectionLabelValues[0] = "USER NOT DETECTED";
            faceDetectionImages[0] = frameConverter.convert(faceDetector.getFrameBasic());

            // Request the FX thread to update the GUI
            Platform.runLater(updatePanelProcess);

            // Updating the detection history
            previousDetectionValues[0] = detected;
        };

        // Defining a continuous face detection process
        final Runnable continuousDetectionProcess = () -> {
            long startTime = System.currentTimeMillis();
            while (runDetectionProcesses[0]) {
                try {
                    // Await the countdown latches to hit 0 (i.e., the face detection thread is still working or the process has not been started yet)
                    for (CountDownLatch latch : detectionCountDownLatches) {
                        if (latch != null) latch.await();
                    }

                    // Early break check to prevent accessing the face detection process on application quit
                    if (!runDetectionProcesses[0]) break;

                    // Get the current time
                    final long currentTime = System.currentTimeMillis();

                    // Calculate the elapsed time
                    final long elapsedTime = currentTime - startTime;

                    // Wait till enough time has passed since the last process
                    if (elapsedTime <= 10000) continue;

                    // Perform the face detection process
                    faceDetectionProcess.run();

                    // Restart the timer
                    startTime = currentTime;
                } catch (InterruptedException e) {
                    System.out.println("Detection countdown latch got interrupted due to exception " + e);
                }
            }
        };

        // Defining the cleanup process to make sure all threads can cease operating on quitting the application
        terminationProcess = () -> {
            runDetectionProcesses[0] = false;
            for (var latch : detectionCountDownLatches) {
                if (latch != null) latch.countDown();
            }
        };

        // Defining the thread responsible for the continuous detection process
        backgroundThread = new Thread(continuousDetectionProcess);

        // Defining the toggle face detection button functionality
        toggleDetection.setOnAction(clickEvent -> {
            if (detectionCountDownLatches[1] != null && detectionCountDownLatches[1].getCount() != 0) {
                detectionCountDownLatches[1].countDown();
                toggleDetection.setText("STOP");
                toggleDetection.setOnMouseEntered(enterEvent -> toggleDetection.setBackground(Effects.RED_OVERLAY_BACKGROUND));
                titleHolder.setText("Detecting...");
            } else {
                detectionCountDownLatches[1] = new CountDownLatch(1);
                toggleDetection.setText("START");
                toggleDetection.setOnMouseEntered(enterEvent -> toggleDetection.setBackground(Effects.GREEN_OVERLAY_BACKGROUND));
            }
        });

        // Selecting the initial state of the face detection toggle
        detectionCountDownLatches[1] = new CountDownLatch(1);
        toggleDetection.setText("START");
        toggleDetection.setOnMouseEntered(enterEvent -> toggleDetection.setBackground(Effects.GREEN_OVERLAY_BACKGROUND));
        toggleDetection.setOnMouseExited(exitEvent -> toggleDetection.setBackground(Background.EMPTY));

        // Selecting the initial state of the run detection process flag
        runDetectionProcesses[0] = true;
    }

    public Thread getBackgroundThread() {
        return backgroundThread;
    }

    public Region getPanel() {
        return faceDetectionPanel;
    }

    public Runnable getTerminationProcess() {
        return terminationProcess;
    }

    public void setBorder(Border b) {
        toggleDetection.setBorder(b);
        viewport.setBorder(b);
        titleHolder.setBorder(b);
    }

    public void setStyle(String s) {
        toggleDetection.setStyle(s);
        titleHolder.setStyle(s);
    }
}
