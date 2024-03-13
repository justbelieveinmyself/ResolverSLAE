package com.justbelieveinmyself.solvingslae;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Random;

public class MainController {

    @FXML
    private GridPane leftGridPane;
    @FXML
    private GridPane rightGridPane;
    @FXML
    private GridPane resultGridPane;
    @FXML
    private TextField rowsTextField;
    @FXML
    private TextField columnsTextField;
    private double[][] coefficientsMatrix;
    private double[] rightHandVector;

    public void createGrid() {
        leftGridPane.getChildren().clear();
        rightGridPane.getChildren().clear();
        resultGridPane.getChildren().clear();
        int rowCount = Integer.parseInt(rowsTextField.getText());
        int columnCount = Integer.parseInt(columnsTextField.getText());

        for (int i = 0; i < rowCount; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            rowConstraints.setMaxHeight(100);
            leftGridPane.getRowConstraints().add(rowConstraints);
            rightGridPane.getRowConstraints().add(rowConstraints);
            resultGridPane.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                TextField textFieldLeft = new TextField();
                leftGridPane.add(textFieldLeft, j, i);
            }
        }


        for (int i = 0; i < rowCount; i++) {
            TextField textFieldRight = new TextField();
            resultGridPane.add(textFieldRight, 0, i);
        }

        for (int i = 0; i < rowCount; i++) {
            TextField textFieldRight = new TextField();
            rightGridPane.add(textFieldRight, 0, i);
        }
    }

    public void fillGridWithRandomNumbers() {
        int rowCount = Integer.parseInt(rowsTextField.getText());
        int columnCount = Integer.parseInt(columnsTextField.getText());
        coefficientsMatrix = new double[rowCount][columnCount];
        Random random = new Random();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                coefficientsMatrix[i][j] = random.nextInt(20) - 10;
                TextField textFieldLeft = (TextField) leftGridPane.getChildren().get(i * columnCount + j);
                textFieldLeft.setText(Double.toString(coefficientsMatrix[i][j]));
            }
        }

        for (Node node : rightGridPane.getChildren()) {
            if (node instanceof TextField) {
                TextField textFieldRight = (TextField) node;
                textFieldRight.setText(Integer.toString(random.nextInt(20) - 10));
            }
        }
    }

    public void solveUsingGaussianElimination() {
        int rowCount = Integer.parseInt(rowsTextField.getText());
        int columnCount = Integer.parseInt(columnsTextField.getText());

        coefficientsMatrix = new double[rowCount][columnCount];
        rightHandVector = new double[rowCount];
        int index = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                TextField textField = (TextField) leftGridPane.getChildren().get(index++);
                coefficientsMatrix[i][j] = Double.parseDouble(textField.getText());
            }
        }

        index = 0;
        for (int i = 0; i < rowCount; i++) {
            TextField textField = (TextField) rightGridPane.getChildren().get(index++);
            rightHandVector[i] = Double.parseDouble(textField.getText());
        }

        for (int k = 0; k < rowCount - 1; k++) {
            for (int i = k + 1; i < rowCount; i++) {
                double factor = coefficientsMatrix[i][k] / coefficientsMatrix[k][k];
                for (int j = k; j < columnCount; j++) {
                    coefficientsMatrix[i][j] -= factor * coefficientsMatrix[k][j];
                }
                rightHandVector[i] -= factor * rightHandVector[k];
            }
        }

        double[] solutions = new double[rowCount];
        for (int i = rowCount - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < rowCount; j++) {
                sum += coefficientsMatrix[i][j] * solutions[j];
            }
            solutions[i] = (rightHandVector[i] - sum) / coefficientsMatrix[i][i];
        }

        for (int i = 0; i < rowCount; i++) {
            Node node = resultGridPane.getChildren().get(i);
            if (node instanceof TextField) {
                TextField textFieldRight = (TextField) node;
                textFieldRight.setText(Double.toString(solutions[i]));
            }

        }
    }


    public void gradientDescent() {
        int rowCount = Integer.parseInt(rowsTextField.getText());
        int columnCount = Integer.parseInt(columnsTextField.getText());

        coefficientsMatrix = new double[rowCount][columnCount];
        rightHandVector = new double[rowCount];
        int index = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                TextField textField = (TextField) leftGridPane.getChildren().get(index++);
                coefficientsMatrix[i][j] = Double.parseDouble(textField.getText());
            }
            TextField textField = (TextField) rightGridPane.getChildren().get(i);
            rightHandVector[i] = Double.parseDouble(textField.getText());
        }

        double[] x = new double[columnCount];

        double alpha = 0.0001;

        int maxIterations = 1000000;

        for (int iter = 0; iter < maxIterations; iter++) {
            double[] gradient = calculateGradient(coefficientsMatrix, rightHandVector, x);

            for (int i = 0; i < columnCount; i++) {
                if (Math.abs(gradient[i]) < 1e-5) {
                    x[i] -= 1e-5 * Math.signum(gradient[i]);
                } else {
                    x[i] -= alpha * gradient[i];
                }
            }

        }

        for (int i = 0; i < rowCount; i++) {
            Node node = resultGridPane.getChildren().get(i);
            if (node instanceof TextField) {
                TextField textFieldRight = (TextField) node;
                textFieldRight.setText(Double.toString(x[i]));
            }

        }
    }

    // Метод для вычисления градиента функции потерь
    private double[] calculateGradient(double[][] coefficientsMatrix, double[] rightHandVector, double[] x) {
        int rowCount = coefficientsMatrix.length;
        int columnCount = coefficientsMatrix[0].length;
        double[] gradient = new double[columnCount];

        for (int i = 0; i < rowCount; i++) {
            double error = 0.0;
            for (int j = 0; j < columnCount; j++) {
                error += coefficientsMatrix[i][j] * x[j];
            }
            error -= rightHandVector[i];
            for (int j = 0; j < columnCount; j++) {
                gradient[j] += 2 * coefficientsMatrix[i][j] * error;
            }
        }

        return gradient;
    }
}
