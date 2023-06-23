package engine;

import io.Generator;
import io.Reader;

import java.io.File;
import java.io.IOException;

public final class FaceRecognition {
    private final File img, result, predict, terminate;
    private final Generator generator;
    private final ProcessBuilder builder;
    private final Reader reader;
    private Process pythonScript;

    public FaceRecognition() {
        final File model  = new File("src/main/python/FaceRecognition.py");
        final File data   = new File("src/main/resources/ivp/database");
        img    = new File("src/main/resources/ivp/temp.jpg");
        result = new File("src/main/resources/ivp/result.txt");
        predict = new File("src/main/resources/ivp/predict.flag");
        terminate = new File("src/main/resources/ivp/terminate.flag");

        builder = new ProcessBuilder(
                "python",
                data.getAbsolutePath(),
                img.getAbsolutePath(),
                result.getAbsolutePath(),
                predict.getAbsolutePath(),
                terminate.getAbsolutePath()
        );

        generator = new Generator();
        reader = new Reader();
    }

    public boolean isRunning() {
        return pythonScript != null && pythonScript.isAlive();
    }

    public String detect() {
        if (img.exists()) {
            generator.generate(predict);
            final long startTime = System.currentTimeMillis();
            while (predict.exists()) {
                final long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 10000) {
                    throw new IllegalStateException("Failed to complete recognition within allocated time");
                }
            }
            return reader.read(result);
        }
        throw new IllegalStateException("No image stored to attempt recognition!");
    }

    public boolean start() {
        if (terminate.exists() && !terminate.delete()) return false;
        else if (isRunning()) return true;

        try {
            pythonScript = builder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
