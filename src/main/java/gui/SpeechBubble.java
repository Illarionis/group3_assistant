package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import static javafx.geometry.VPos.*;

public final class SpeechBubble {
    private SpeechBubble() {}

    //create a new speech bubble javafx image with the given text
    //the speech bubble is rectangular with round corners and has a tail
    //the tail is at the bottom of the speech bubble
    //the tail is at the left or right side of the speech bubble and the user can choose which side
    //method returns the speech bubble javafx image
    //define the constructor of the speech bubble class
    //public static ImageView createSpeechBubble(String text, int width, int height, int tail, boolean left, Color color)

    public static ImageView createSpeechBubble(String message, int maxWidth, int maxHeight, boolean left){
        int maxCharperRow = 40;

        Font font = Font.font("Verdana", FontWeight.NORMAL, 20);
        Text text = new Text(message);
        text.setFont(font);

        double width = text.getLayoutBounds().getWidth()+20;
        double height = text.getLayoutBounds().getHeight();

        if(message.length() > maxCharperRow){
            int rows = (int) Math.ceil(message.length() / maxCharperRow);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= rows; i++)
                sb.append(message, maxCharperRow * (i - 1), maxCharperRow * i).append("\n");
            text.setText(sb.toString());
            width = text.getLayoutBounds().getWidth()+20;
            height = text.getLayoutBounds().getHeight();
        }

        ImageView imageView = new ImageView();
        //add a canvas to draw on
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //draw the speech bubble
        //draw the speech bubble rectangle
        gc.setFill(Color.rgb(220, 248, 198));
        gc.fillRoundRect(0, 0, width, height, 20, 20);
        //draw the speech bubble tail
        /*gc.setFill(Color.RED);
        if(left){
            gc.fillPolygon(new double[]{0, 0, 20}, new double[]{height/2d, height/2d+20, height/2d}, 3);
        }else{
            gc.fillPolygon(new double[]{width, width, width-20}, new double[]{height/2d, height/2d+20, height/2d}, 3);
        }*/
        //draw the text
        gc.setFill(Color.BLACK);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(CENTER);
        gc.fillText(text.getText(), 10, height/2d);



        //add the canvas to the imageview
        imageView.setImage(canvas.snapshot(null, null));
        return imageView;
    }

}
