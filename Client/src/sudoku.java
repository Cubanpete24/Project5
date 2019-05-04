import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//made a sudoku class for more organization
public class sudoku {
    //Upper left cells
    TextField e11; //upper left textfield
    TextField e12; //upper right textfield
    TextField e21; //lower left textfield
    TextField e22; //lower right textfield
    int e11Ans, e12Ans, e21Ans, e22Ans;

    //UpperRight cells
    TextField f11;
    TextField f12;
    TextField f21;
    TextField f22;
    int f11Ans, f12Ans, f21Ans, f22Ans;

    //Lower left cells
    TextField g11;
    TextField g12;
    TextField g21;
    TextField g22;
    int g11Ans, g12Ans, g21Ans, g22Ans;

    //Lower right cells
    TextField h11;
    TextField h12;
    TextField h21;
    TextField h22;
    int h11Ans, h12Ans, h21Ans, h22Ans;



    sudoku(){
        //Upper Left
        e11 = new TextField();
        e12 = new TextField();
        e21 = new TextField();
        e22 = new TextField();
        e11.setEditable(true); //making sure some cells aren't editable
        e12.setEditable(true);
        e21.setEditable(true);
        e22.setEditable(true);
        e11.setPrefWidth(30);
        e12.setPrefWidth(30);
        e21.setPrefWidth(30);
        e22.setPrefWidth(30);
        e11.setPrefHeight(30);
        e12.setPrefHeight(30);
        e21.setPrefHeight(30);
        e22.setPrefHeight(30);

        //Upper Right
        f11 = new TextField();
        f12 = new TextField();
        f21 = new TextField();
        f22 = new TextField();
        f11.setEditable(true); //making sure some cells aren't editable
        f12.setEditable(true);
        f21.setEditable(true);
        f22.setEditable(true);
        f11.setPrefWidth(30);
        f12.setPrefWidth(30);
        f21.setPrefWidth(30);
        f22.setPrefWidth(30);
        f11.setPrefHeight(30);
        f12.setPrefHeight(30);
        f21.setPrefHeight(30);
        f22.setPrefHeight(30);

        //Lower Left
        g11 = new TextField();
        g12 = new TextField();
        g21 = new TextField();
        g22 = new TextField();
        g11.setEditable(true); //making sure some cells aren't editable
        g12.setEditable(true);
        g21.setEditable(true);
        g22.setEditable(true);
        g11.setPrefWidth(30);
        g12.setPrefWidth(30);
        g21.setPrefWidth(30);
        g22.setPrefWidth(30);
        g11.setPrefHeight(30);
        g12.setPrefHeight(30);
        g21.setPrefHeight(30);
        g22.setPrefHeight(30);

        //Lower Right
        h11 = new TextField();
        h12 = new TextField();
        h21 = new TextField();
        h22 = new TextField();
        h11.setEditable(true); //making sure some cells aren't editable
        h12.setEditable(true);
        h21.setEditable(true);
        h22.setEditable(true);
        h11.setPrefWidth(30);
        h12.setPrefWidth(30);
        h21.setPrefWidth(30);
        h22.setPrefWidth(30);
        h11.setPrefHeight(30);
        h12.setPrefHeight(30);
        h21.setPrefHeight(30);
        h22.setPrefHeight(30);

    }

    void setUpperLeft(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            e11.setText(Integer.toString(_11));
            e11.setEditable(false);
            e11Ans = _11;
        }
        if(_12 != 99) {
            e12.setText(Integer.toString(_12));
            e12.setEditable(false);
            e12Ans = _12;
        }
        if(_21 != 99) {
            e21.setText(Integer.toString(_21));
            e21.setEditable(false);
            e21Ans = _21;
        }
        if(_22 != 99) {
            e22.setText(Integer.toString(_22));
            e22.setEditable(false);
            e22Ans = _22;
        }
    }

    void setUpperRight(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            f11.setText(Integer.toString(_11));
            f11.setEditable(false);
            f11Ans = _11;
        }
        if(_12 != 99) {
            f12.setText(Integer.toString(_12));
            f12.setEditable(false);
            f12Ans = _12;
        }
        if(_21 != 99) {
            f21.setText(Integer.toString(_21));
            f21.setEditable(false);
            f21Ans = _21;
        }
        if(_22 != 99) {
            f22.setText(Integer.toString(_22));
            f22.setEditable(false);
            f22Ans = _22;
        }
    }

    void setLowerLeft(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            g11.setText(Integer.toString(_11));
            g11.setEditable(false);
            g11Ans = _11;
        }
        if(_12 != 99) {
            g12.setText(Integer.toString(_12));
            g12.setEditable(false);
            g12Ans = _12;
        }
        if(_21 != 99) {
            g21.setText(Integer.toString(_21));
            g21.setEditable(false);
            g21Ans = _21;
        }
        if(_22 != 99) {
            g22.setText(Integer.toString(_22));
            g22.setEditable(false);
            g22Ans = _22;
        }
    }

    void setLowerRight(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            h11.setText(Integer.toString(_11));
            h11.setEditable(false);
            h11Ans = _11;
        }
        if(_12 != 99) {
            h12.setText(Integer.toString(_12));
            h12.setEditable(false);
            h12Ans = _12;
        }
        if(_21 != 99) {
            h21.setText(Integer.toString(_21));
            h21.setEditable(false);
            h21Ans = _21;
        }
        if(_22 != 99) {
            h22.setText(Integer.toString(_22));
            h22.setEditable(false);
            h22Ans = _22;
        }
    }

    void setUpperLeftAnswers(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            e11.setEditable(true);
            e11Ans = _11;
        }
        if(_12 != 99) {
            e12.setEditable(true);
            e12Ans = _12;
        }
        if(_21 != 99) {
            e21.setEditable(true);
            e21Ans = _21;
        }
        if(_22 != 99) {
            e22.setEditable(true);
            e22Ans = _22;
        }
    }

    void setUpperRightAnswers(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            f11.setEditable(true);
            f11Ans = _11;
        }
        if(_12 != 99) {
            f12.setEditable(true);
            f12Ans = _12;
        }
        if(_21 != 99) {
            f21.setEditable(true);
            f21Ans = _21;
        }
        if(_22 != 99) {
            f22.setEditable(true);
            f22Ans = _22;
        }
    }

    void setLowerLeftAnswers(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            g11.setEditable(true);
            g11Ans = _11;
        }
        if(_12 != 99) {
            g12.setEditable(true);
            g12Ans = _12;
        }
        if(_21 != 99) {
            g21.setEditable(true);
            g21Ans = _21;
        }
        if(_22 != 99) {
            g22.setEditable(true);
            g22Ans = _22;
        }
    }

    void setLowerRightAnswers(int _11, int _12, int _21, int _22){
        if(_11 != 99) {
            h11.setEditable(true);
            h11Ans = _11;
        }
        if(_12 != 99) {
            h12.setEditable(true);
            h12Ans = _12;
        }
        if(_21 != 99) {
            h21.setEditable(true);
            h21Ans = _21;
        }
        if(_22 != 99) {
            h22.setEditable(true);
            h22Ans = _22;

        }
    }

    boolean checkAnswers(){
        if( e11.getText().equals(Integer.toString(e11Ans)) &&
                e12.getText().equals(Integer.toString(e12Ans)) &&
                e21.getText().equals(Integer.toString(e21Ans)) &&
                e22.getText().equals(Integer.toString(e22Ans)) &&
                f11.getText().equals(Integer.toString(f11Ans)) &&
                f12.getText().equals(Integer.toString(f12Ans)) &&
                f21.getText().equals(Integer.toString(f21Ans)) &&
                f22.getText().equals(Integer.toString(f22Ans)) &&
                g11.getText().equals(Integer.toString(g11Ans)) &&
                g12.getText().equals(Integer.toString(g12Ans)) &&
                g21.getText().equals(Integer.toString(g21Ans)) &&
                g22.getText().equals(Integer.toString(g22Ans)) &&
                h11.getText().equals(Integer.toString(h11Ans)) &&
                h12.getText().equals(Integer.toString(h12Ans)) &&
                h21.getText().equals(Integer.toString(h21Ans)) &&
                h22.getText().equals(Integer.toString(h22Ans)))
            return true;
        else
            return false;
    }

    void cheat(){
        e11.setText(Integer.toString(e11Ans));
        e12.setText(Integer.toString(e12Ans));
        e21.setText(Integer.toString(e21Ans));
        e22.setText(Integer.toString(e22Ans));
        f11.setText(Integer.toString(f11Ans));
        f12.setText(Integer.toString(f12Ans));
        f21.setText(Integer.toString(f21Ans));
        f22.setText(Integer.toString(f22Ans));
        g11.setText(Integer.toString(g11Ans));
        g12.setText(Integer.toString(g12Ans));
        g21.setText(Integer.toString(g21Ans));
        g22.setText(Integer.toString(g22Ans));
        h11.setText(Integer.toString(h11Ans));
        h12.setText(Integer.toString(h12Ans));
        h21.setText(Integer.toString(h21Ans));
        h22.setText(Integer.toString(h22Ans));
    }

    VBox makePuzzle(){
        //Here we make each 1x2 row
        //Upper Left
        HBox puzzleRow1UpperLeft = new HBox(e11, e12);
        HBox puzzleRow2UpperLeft = new HBox(e21, e22);
        //Upper Right
        HBox puzzleRow1UpperRight = new HBox(f11, f12);
        HBox puzzleRow2UpperRight = new HBox(f21, f22);
        //Lower Left
        HBox puzzleRow1Lowerleft = new HBox(g11, g12);
        HBox puzzleRow2LowerLeft = new HBox(g21, g22);
        //Lower Right
        HBox puzzleRow1LowerRight = new HBox(h11, h12);
        HBox puzzleRow2LowerRight = new HBox(h21, h22);

        //compiles the rows into 2x2 boxes
        VBox puzzleUpperLeft = new VBox(puzzleRow1UpperLeft, puzzleRow2UpperLeft);
        VBox puzzleUpperRight = new VBox(puzzleRow1UpperRight, puzzleRow2UpperRight);
        VBox puzzleLowerLeft = new VBox(puzzleRow1Lowerleft, puzzleRow2LowerLeft);
        VBox puzzleLowerRight = new VBox(puzzleRow1LowerRight, puzzleRow2LowerRight);

        //Brings it all together
        HBox puzzleRow1 = new HBox(5, puzzleUpperLeft, puzzleUpperRight);
        HBox puzzleRow2 = new HBox(5, puzzleLowerLeft, puzzleLowerRight);

        VBox puzzle = new VBox(5,puzzleRow1, puzzleRow2);

        return puzzle;

    }

    TextField getCheat(){
        return f12;
    }
}
