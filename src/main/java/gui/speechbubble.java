package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class speechbubble {


    //create a new speechbubble javafx image with the given text
    //the speechbubble is rectangular with round corners and has a tail
    //the tail is at the bottom of the speechbubble
    //the tail is at the left or right side of the speechbubble and the user can choose which side
    //method returns the speechbubble javafx image
    //define the constructur of the speechbubble class
    //public static ImageView createSpeechBubble(String text, int width, int height, int tail, boolean left, Color color)

    public static ImageView createSpeechBubble(String text, int width, int height, boolean left, Color color){

        ImageView imageView = new ImageView();
        //add a canvas to draw on
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //draw the speechbubble
        //draw the speechbubble rectangle
        gc.setFill(color);
        gc.fillRoundRect(0, 0, width, height, 20, 20);
        //draw the speechbubble tail
        /*gc.setFill(Color.RED);
        if(left){
            gc.fillPolygon(new double[]{0, 0, 20}, new double[]{height/2d, height/2d+20, height/2d}, 3);
        }else{
            gc.fillPolygon(new double[]{width, width, width-20}, new double[]{height/2d, height/2d+20, height/2d}, 3);
        }*/
        //draw the text
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, width/2d, height/2d);
        //add the canvas to the imageview
        imageView.setImage(canvas.snapshot(null, null));
        //return the imageview
        return imageView;
    }

}
