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

    //method returns the speech bubble javafx image
    //define the constructor of the speech bubble class

    public static ImageView createSpeechBubble(String message, boolean stringIsUserMessage){
        int maxCharPerRow = 20;
        //if string is user message, then the speech bubble is on the right side and the color is Color.rgb(220, 248, 198); (light green) otherwise it is on the left side and the color is Color.rgb(255, 255, 255); (white)
        Color color = stringIsUserMessage ? Color.rgb(220, 248, 198) : Color.rgb(255, 255, 255);
        Font font = Font.font("Verdana", FontWeight.NORMAL, 20);
        Text text = new Text(message);
        text.setFont(font);

        double width = text.getLayoutBounds().getWidth()+20;
        double height = text.getLayoutBounds().getHeight();

        if(message.length() > maxCharPerRow){
            int rows = (int) Math.ceil(message.length() / maxCharPerRow);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= rows; i++)
                sb.append(message, maxCharPerRow * (i - 1), maxCharPerRow * i).append("\n");
            text.setText(sb.toString());
            width = text.getLayoutBounds().getWidth()+20;
            height = text.getLayoutBounds().getHeight();
        }

        ImageView imageView = new ImageView();
        //add a canvas to draw on
        Canvas canvas = new Canvas(width+10, height+5);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //draw the background the size of the canvas
        gc.setFill(Color.rgb(244, 244, 244));
        gc.fillRect(0, 0, width+10, height+5);
        //draw the speech bubble
        gc.setFill(color);
        gc.fillRoundRect(5, 0, width, height, 20, 20);

        //draw a tail at the stringIsUserMessage or right side
        //the tail has the shape of a triangle
        //the tail is at the bottom of the speech bubble
        //if the tail is at the stringIsUserMessage it points to the stringIsUserMessage
        //if the tail is at the right it points to the right
        gc.setFill(color);
        if(stringIsUserMessage){
            gc.fillPolygon(new double[]{width, width-5, width-15}, new double[]{height+15, height-20, height}, 3);
        }else{
            gc.fillPolygon(new double[]{-5, 5, 15}, new double[]{height+15, height-20, height}, 3);
        }
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
