package ivp;

import design.Action;
import io.Generator;
import io.Reader;

import java.io.File;

public final class FaceRecognition {
    private final Action prediction, termination;
    private final ProcessBuilder processBuilder;
    private Process process;
    private String out;

    public FaceRecognition(Generator g, Reader r, File model, File terminate, File predict, File dataset, File input, File output) {
        processBuilder = new ProcessBuilder(
                "python",
                model.getAbsolutePath(),
                terminate.getAbsolutePath(),
                predict.getAbsolutePath(),
                dataset.getAbsolutePath(),
                input.getAbsolutePath(),
                output.getAbsolutePath()
        );

        prediction = () -> {
            if (predict.exists()) return;
            g.generate(predict);
            final long startTime = System.currentTimeMillis();
            while (predict.exists()) {
                final long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 5000) {
                    System.out.println("@FACE_RECOGNITION: Failed to complete prediction within allocated time.");
                    out = null;
                    return;
                }
            }
            out = r.read(output);
        };

        termination = () -> {
            if (terminate.exists()) return;
            g.generate(terminate);
        };
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }

    public String predict() {
        prediction.execute();
        return out;
    }

    public boolean start() {
        if (isRunning()) return true;
        try {
            process = processBuilder.start();
            System.out.println("@FACE_RECOGNITION: Successfully started python process.");
            return true;
        } catch (Exception e) {
            System.out.println("@FACE_RECOGNITION: Failed to start python process due to exception " + e);
            return false;
        }
    }

    public void terminate() {
        termination.execute();
        final long startTime = System.currentTimeMillis();
        while (isRunning()) {
            final long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 5000) {
                System.out.println("@FACE_RECOGNITION: FAiled to terminate naturally within allocated time.");
                process.destroy();
                break;
            }
        }
    }
}
