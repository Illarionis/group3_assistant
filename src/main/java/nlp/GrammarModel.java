package nlp;

import design.Action;
import io.Generator;
import io.Reader;
import io.Writer;

import java.io.File;

public final class GrammarModel {
    private final Action prediction, termination, training;
    private final ProcessBuilder processBuilder;
    private Process process;
    private String in, out;

    public GrammarModel(Generator g, Reader r, Writer w, File model, File terminate, File train, File predict, File dataset, File input, File output) {
        processBuilder = new ProcessBuilder(
                "python",
                model.getAbsolutePath(),
                terminate.getAbsolutePath(),
                train.getAbsolutePath(),
                predict.getAbsolutePath(),
                dataset.getAbsolutePath(),
                input.getAbsolutePath(),
                output.getAbsolutePath()
        );

        prediction = () -> {
            if (in == null || in.isBlank() || predict.exists()) return;
            w.write(input, in);
            g.generate(predict);
            final long startTime = System.currentTimeMillis();
            while (predict.exists()) {
                final long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 5000) {
                    System.out.println("@GRAMMAR_MODEL: Failed to complete prediction within allocated time.");
                    out = "-1";
                    return;
                }
            }
            out = r.read(output);
        };

        termination = () -> g.generate(terminate);

        training = () -> g.generate(train);
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }

    public int predict(String s) {
        in = s;
        prediction.execute();
        return out == null ? -1 : Integer.parseInt(out);
    }

    public boolean start() {
        if (isRunning()) return true;
        try {
            process = processBuilder.start();
            System.out.println("@GRAMMAR_MODEL: Successfully started python process.");
            return true;
        } catch (Exception e) {
            System.out.println("@GRAMMAR_MODEL: Failed to start python process due to exception " + e);
            return false;
        }
    }

    public void terminate() {
        termination.execute();
        final long startTime = System.currentTimeMillis();
        while (isRunning()) {
            final long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 5000) {
                System.out.println("@GRAMMAR_MODEL: FAiled to terminate naturally within allocated time.");
                process.destroy();
                break;
            }
        }
    }

    public void train() {
        training.execute();
    }
}
