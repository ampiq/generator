package main.java.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.File;

public class OutputController {

    @FXML
    private Button nextButton;

    @FXML
    private TableView<Integer> matrixb;

    @FXML
    private Label inter1;

    @FXML
    private TableView<Integer> vectorb1;

    @FXML
    private Label inter2;

    @FXML
    private TextField valueN;

    @FXML
    private TextField valueM;

    @FXML
    private TableView<Integer> planp;

    @FXML
    private TextField value1Text;

    @FXML
    private TextField value2Text;

    private int vp1 = 1;
    private int vp2 = 1;
    private int vpt = 1;
    private int val;
    private boolean exist = false;

    @FXML
    void initialize() {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vectorb1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        vectorb1.getSelectionModel().setCellSelectionEnabled(true);

        nextButton.setOnAction(event -> {
            String value1 = valueM.getText();
            String value2 = valueN.getText();
            String value3 = value1Text.getText();
            String value4 = value2Text.getText();

            if (exist) {
                Alert alertError2 = new Alert(Alert.AlertType.INFORMATION);
                alertError2.setTitle("Завершение");
                alertError2.setHeaderText(null);
                alertError2.setContentText("Программа успешно завершила работу");
                alertError2.showAndWait();

                nextButton.getScene().getWindow().hide();
            }

            if (value1.equals("") || value2.equals("") || value3.equals("") || value4.equals("")) {
                alertError.setTitle("Не все данные введены");
                alertError.setHeaderText(null);
                alertError.setContentText("Заполните поля интервалов и задач");
                alertError.showAndWait();
            } else {
                int v1 = Integer.parseInt(value1Text.getText());
                int v2 = Integer.parseInt(value2Text.getText());
                int a = Integer.parseInt(valueM.getText());
                int b = Integer.parseInt(valueN.getText());
                int pa = 1, pb = 1;

                planp.getColumns().clear();
                matrixb.getColumns().clear();
                vectorb1.getColumns().clear();
                planp.getItems().clear();
                matrixb.getItems().clear();
                vectorb1.getItems().clear();

                inter1.setText("Количество временных интервалов: (" + vp1 + " из " + v1 + ")");
                inter2.setText("Количество решаемых задач: (" + (vp2) + " из " + v2 + ")");

                if (vp1 == v1 && v2 == vp2) {
                    vpt++;
                    vp1 = 1;
                    vp2 = 0;
                }
                if (vp1 <= v1 && v2 == vp2) {
                    vp1++;
                    vp2 = 1;
                } else {
                    vp2++;
                }

                while (pa <= a) {
                    String str = Integer.toString(pa);
                    TableColumn tableColumn = new TableColumn(str);
                    planp.getColumns().addAll(tableColumn);
                    matrixb.getColumns().addAll(tableColumn);
                    pa++;
                }

                val = 1 + (int) (Math.random() * 6);
                for (int i = 1; i <= val; i++) {
                    String str = Integer.toString(i);
                    TableColumn tableColumn = new TableColumn(str);
                    vectorb1.getColumns().addAll(tableColumn);
                }


                planp.getItems().addAll(pb);
                while (pb <= b) {
                    matrixb.getItems().addAll(pb);
                    vectorb1.getItems().addAll(pb);
                    pb++;
                }
                if (vp1 == v1 && vp2 == v2) {
                    exist = true;
                }
                if (v1 == 1 && v2 == 1) {
                    exist = true;
                }
            }
        });
    }
}
