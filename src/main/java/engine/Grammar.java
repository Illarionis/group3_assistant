package engine;

import io.Generator;
import io.Reader;
import io.Writer;

import java.io.File;
import java.io.IOException;

public final class Grammar {
    private final File input, output, predict, terminate;
    private final Generator generator;
    private final ProcessBuilder builder;
    private final Reader reader;
    private final Writer writer;
    private Process pythonScript;

    public Grammar() {
        final File model = new File("src/main/python/grammar-learner.py");
        final File data  = new File("src/main/resources/nlp/data.csv");
        input     = new File("src/main/resources/nlp/model.input");
        output    = new File("src/main/resources/nlp/model.output");
        predict   = new File("src/main/resources/flags/model.predict");
        terminate = new File("src/main/resources/flags/model.terminate");

        builder = new ProcessBuilder(
                "python",
                model.getAbsolutePath(),
                data.getAbsolutePath(),
                input.getAbsolutePath(),
                output.getAbsolutePath(),
                predict.getAbsolutePath(),
                terminate.getAbsolutePath()
        );
        generator = new Generator();
        reader    = new Reader();
        writer    = new Writer();
    }

    public boolean isRunning() {
        return pythonScript != null && pythonScript.isAlive();
    }

    public boolean recognize(String in) {
        writer.write(input, in);
        generator.generate(predict);
        final long startTime = System.currentTimeMillis();
        while (predict.exists()) {
            final long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 10000) {
                System.out.println("Warning: Failed to complete prediction within allocated time.");
                return false;
            }
        }
        final String out = reader.read(output);
        return out.equals("1");
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

    public boolean terminate() {
        if (terminate.exists()) return true;
        else return generator.generate(terminate);
    }
}
