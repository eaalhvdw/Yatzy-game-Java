package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Yatzy;

// This class models a GUI for the game yatzy.

public class MainApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    // Initializes a stage to hold grid panes for the game.
    @Override
    public void start(Stage stage) {
        stage.setTitle("Yatzy");
        GridPane pane = new GridPane();
        initContent(pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // -------------------------------------------------------------------------
    // Shows the face values of the 5 dice.
    private TextField[] txfValues = new TextField[5];
    // Shows the hold status of the 5 dice.
    private CheckBox[] chbHolds = new CheckBox[5];
    // Shows the results previously selected.
    // For free results (results not set yet), the results.
    // Corresponding to the actual face values of the 5 dice are shown.
    private TextField[] txfResults = new TextField[15];
    // Shows points in sums, bonus and total.
    private TextField txfSumSame, txfBonus, txfSumOther, txfTotal;
    // Shows the number of times the dice has been rolled.
    private Label lblRolled;
    // Roll the dice.
    private Button btnRoll;
    // Holds the sum of points for 1-s, 2-s.. 4-s etc.
    private int sumSame = 0;
    // Holds the points for bonus.
    private int sumBonus = 0;
    // Holds the sum of points for all the combination options.
    private int sumOther = 0;
    // Holds the total points.
    private int sumTotal = 0;
    // Holds the number of disabled txfResults.
    private int disabledCounter = 0;

    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(5);

        // ---------------------------------------------------------------------
        // Grid pane, the visual cup that contains the dice to throw in the game.
        GridPane dicePane = new GridPane();
        pane.add(dicePane, 0, 0);
        dicePane.setGridLinesVisible(false);
        dicePane.setPadding(new Insets(10));
        dicePane.setHgap(10);
        dicePane.setVgap(10);
        dicePane.setStyle("-fx-border-color: black");

        // Initialize txfValues to hold the values of the scores.
        for (int i = 0; i < txfValues.length; i++) {
            txfValues[i] = new TextField();
            txfValues[i].setEditable(false);
            txfValues[i].setPrefWidth(80);
            txfValues[i].setPrefHeight(100);
            txfValues[i].setFont(Font.font(25));
            txfValues[i].setAlignment(Pos.CENTER);
            dicePane.add(txfValues[i], 1 * (i * 1), 0);

            // Initializes check boxes 'Holds' and add them to the dice pane.
            chbHolds[i] = new CheckBox("Hold");
            dicePane.add(chbHolds[i], 1 * (i * 1), 1);
        }

        // Initialize lblRolled with a string value and adds it to the pane dicePane.
        lblRolled = new Label("Rolled: ");
        lblRolled.setText("Rolled: " + yatzy.getThrowCount() + " times.");
        dicePane.add(lblRolled, 4, 2);

        // Initialize btnRoll with a value of type string and adds it to the pane.
        btnRoll = new Button("Roll");
        btnRoll.setFont(Font.font(15));
        btnRoll.setPrefSize(55, 40);
        dicePane.add(btnRoll, 3, 2);

        // Connect a method to the button roll.
        btnRoll.setOnAction(event -> rollAction());

        // ---------------------------------------------------------------------

        // New grid pane to the visual writing block, which contains the scores of the
        // results gained throughout the game.
        GridPane scorePane = new GridPane();
        pane.add(scorePane, 0, 1);
        scorePane.setGridLinesVisible(false);
        scorePane.setPadding(new Insets(10));
        scorePane.setVgap(2);
        scorePane.setHgap(10);
        scorePane.setStyle("-fx-border-color: black");

        // Labels and text fields for sums, bonus and total.
        // Initialize labels results.
        Label[] lblResults = new Label[txfResults.length];
        lblResults[0] = new Label("1-s");
        lblResults[1] = new Label("2-s");
        lblResults[2] = new Label("3-s");
        lblResults[3] = new Label("4-s");
        lblResults[4] = new Label("5-s");
        lblResults[5] = new Label("6-s");
        lblResults[6] = new Label("One pair");
        lblResults[7] = new Label("Two pairs");
        lblResults[8] = new Label("Three same");
        lblResults[9] = new Label("Four same");
        lblResults[10] = new Label("Full House");
        lblResults[11] = new Label("Small Straight");
        lblResults[12] = new Label("Large Straight");
        lblResults[13] = new Label("Chance");
        lblResults[14] = new Label("Yatzy");

        // Initialize txfResults and adds labels result to the score pane.
        for (int i = 0; i < txfResults.length; i++) {
            txfResults[i] = new TextField();
            txfResults[i].setAlignment(Pos.BASELINE_RIGHT);
            txfResults[i].setMaxWidth(50);
            txfResults[i].setEditable(false);
            txfResults[i].setText("0");
            txfResults[i].setStyle("-fx-text-inner-color: blue;");
            final int indeks = i;
            scorePane.add(txfResults[i], 1, 2 * i);
            scorePane.add(lblResults[i], 0, 2 * i);

            // Connects a method to the event of a click on a text field.
            txfResults[i].setOnMouseClicked(event -> chooseFieldAction(indeks));

        }

        // Initializes and adds lblSumSame and txfSumSame to the pane.
        Label lblSumSame = new Label("Sum: ");
        scorePane.add(lblSumSame, 2, 10);

        txfSumSame = new TextField();
        txfSumSame.setAlignment(Pos.BASELINE_RIGHT);
        txfSumSame.setMaxWidth(50);
        txfSumSame.setEditable(false);
        txfSumSame.setText("0");
        txfSumSame.setStyle("-fx-text-inner-color: blue;");
        scorePane.add(txfSumSame, 3, 10);

        // Initializes and adds lblBonus and txfBonus to the pane.
        Label lblBonus = new Label("Bonus: ");
        scorePane.add(lblBonus, 4, 10);

        txfBonus = new TextField();
        txfBonus.setAlignment(Pos.BASELINE_RIGHT);
        txfBonus.setMaxWidth(50);
        txfBonus.setEditable(false);
        txfBonus.setText("0");
        txfBonus.setStyle("-fx-text-inner-color: blue;");
        scorePane.add(txfBonus, 5, 10);

        // Initializes and adds lblSumOther and txfSumOther to the scorePane.
        Label lblSumOther = new Label("Sum: ");
        scorePane.add(lblSumOther, 2, 28);

        txfSumOther = new TextField();
        txfSumOther.setAlignment(Pos.BASELINE_RIGHT);
        txfSumOther.setMaxWidth(50);
        txfSumOther.setEditable(false);
        txfSumOther.setText("0");
        txfSumOther.setStyle("-fx-text-inner-color: blue;");
        scorePane.add(txfSumOther, 3, 28);

        // Initializes and adds lblTotal and txfTotal to the pane scorePane.
        Label lblTotal = new Label("Total: ");
        scorePane.add(lblTotal, 4, 28);

        txfTotal = new TextField();
        txfTotal.setAlignment(Pos.BASELINE_RIGHT);
        txfTotal.setMaxWidth(50);
        txfTotal.setEditable(false);
        txfTotal.setText("0");
        txfTotal.setStyle("-fx-text-inner-color: blue;");
        scorePane.add(txfTotal, 5, 28);
    }

    // -------------------------------------------------------------------------

    private Yatzy yatzy = new Yatzy();

    // A method for btnRoll's action.
    private void rollAction() {

        if (yatzy.getThrowCount() < 3) {
            yatzy.throwDice(getHolds());
            setResults();

            for (int i = 0; i < txfValues.length; i++) {
                txfValues[i].setText("" + yatzy.getValues()[i]);
            }
            lblRolled.setText("Rolled: " + yatzy.getThrowCount() + " times.");

            if (yatzy.getThrowCount() == 3) {
                btnRoll.setDisable(true);
            }
        }

    }

    // Return the booleans for the checked boxes.
    private boolean[] getHolds() {
        boolean[] holds = new boolean[5];

        for (int i = 0; i < holds.length; i++) {
            if (chbHolds[i].isSelected()) {
                holds[i] = true;
            }
        }

        return holds;
    }

    // Registers the text results to match the dice shown.
    private void setResults() {

        for (int i = 0; i < txfResults.length; i++) {
            if (!txfResults[i].isDisabled()) {
                txfResults[i].setText("" + yatzy.getResults()[i]);
            }
        }

    }

    // -------------------------------------------------------------------------

    // A method for mouse click on one of the text fields in txfResults.
    private void chooseFieldAction(int indeks) {

        if (yatzy.getThrowCount() > 0) {
            TextField textField = txfResults[indeks];
            int result = yatzy.getResults()[indeks];

            if (indeks < 6) {
                sumSame = sumSame + result;
                txfSumSame.setText("" + sumSame);
            }

            if (indeks > 5) {
                sumOther = sumOther + result;
                txfSumOther.setText("" + sumOther);
            }

            bonus();

            sumTotal = sumBonus + sumOther + sumSame;
            txfTotal.setText("" + sumTotal);

            disabledCounter++;
            textField.setDisable(true);
            endTurn();
        }
    }

    // This method calculates whether or not it is achieved and shows the bonus if
    // it is achieved.
    private void bonus() {
        if (sumSame >= 63) {
            sumBonus = 50;
            txfBonus.setText("" + sumBonus);
        }
    }

    // At the end of each round, this method resets the roll button and the dice to
    // play a new round.
    private void endTurn() {

        yatzy.resetThrowCount();
        lblRolled.setText("Rolled: 0 times.");
        btnRoll.setDisable(false);
        yatzy.resetValues();
        resetTxfResults();
        resetCheckBox();

        // Resets values of the dices.
        for (int i = 0; i < txfValues.length; i++) {
            txfValues[i].setText("");
        }

        // Checks when the game is at its end.
        if (disabledCounter >= txfResults.length) {
            endGame();
        }
    }

    // Resets the text fields for all the results.
    private void resetTxfResults() {
        for (int i = 0; i < txfResults.length; i++) {
            if (!txfResults[i].isDisabled()) {
                txfResults[i].setText("");
            }
        }
    }

    // Resets the check boxes so all booleans are false and ready for a new round.
    private void resetCheckBox() {
        for (int i = 0; i < chbHolds.length; i++) {
            chbHolds[i].setSelected(false);
        }
    }

    // This method shows a informational window at the end of each game.
    private void endGame() {
        Alert gameEnd = new Alert(AlertType.INFORMATION);
        gameEnd.setTitle("Game over");
        gameEnd.setHeaderText("The game is over. You got " + sumTotal + " points.");
        gameEnd.setContentText("Click OK to play again.");
        gameEnd.showAndWait();
        restartGame();
    }

    // This method resets the game for the user to replay the game.
    private void restartGame() {
        // Resets the values.
        yatzy.resetValues();
        for (int i = 0; i < txfResults.length; i++) {
            txfResults[i].setDisable(false);
            txfResults[i].setText("");
        }

        // Resets the sum of the results for all the combinations of same eyes.
        this.sumSame = 0;
        txfSumSame.setText("0");

        // Resets the total sum of all the results.
        this.sumTotal = 0;
        txfTotal.setText("0");

        // Resets the sum of all the different combinations apart from the combinations
        // of same eyes.
        this.sumOther = 0;
        txfSumOther.setText("0");

        // Resets the bonus.
        this.sumBonus = 0;
        txfBonus.setText("0");

        // Closes the informational window of endGame().
        disabledCounter = 0;

        endTurn();
    }
}
