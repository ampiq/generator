package main.java.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
    private TextField valueT;

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

    @FXML
    private TextField valueK;

    int vp1 = 1, vp2 = 1, vpt = 1, val;
    boolean exist = false;

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
            String value5 = valueT.getText();

            if (exist) {
                Alert alertError2 = new Alert(Alert.AlertType.INFORMATION);
                alertError2.setTitle("Завершение");
                alertError2.setHeaderText(null);
                alertError2.setContentText("Программа успешно завершила работу");
                alertError2.showAndWait();

                nextButton.getScene().getWindow().hide();
            }

            if (value1.equals("") || value2.equals("") || value3.equals("") || value4.equals("") || value5.equals((""))) {
                alertError.setTitle("Не все данные введены");
                alertError.setHeaderText(null);
                alertError.setContentText("Заполните поля интервалов и задач");
                alertError.showAndWait();
            } else {
                int v1 = Integer.parseInt(value1Text.getText());
                int v2 = Integer.parseInt(value2Text.getText());
                int v3 = Integer.parseInt(valueT.getText());
                int a = Integer.parseInt(valueM.getText());
                int b = Integer.parseInt(valueN.getText());
                int pa = 1, pb = 1;

                String fileName = "OUT_t" + vpt + "___" + vp1 + "_" + vp2 + ".txt";
                File file = new File(fileName);

                planp.getColumns().clear();
                matrixb.getColumns().clear();
                vectorb1.getColumns().clear();
                planp.getItems().clear();
                matrixb.getItems().clear();
                vectorb1.getItems().clear();

                inter1.setText("Количество временных интервалов: (" + vp1 + " из " + v1 + ")");
                inter2.setText("Количество решаемых задач: (" + (vp2) + " из " + v2 + ")");

                if (vpt <= v3 && vp1 == v1 && v2 == vp2) {
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
                if (v3 != 1 || !exist) {

                    try {
                        //проверяем, что если файл не существует то создаем его
                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        //PrintWriter обеспечит возможности записи в файл
                        PrintWriter out = new PrintWriter(file.getAbsoluteFile());

                        try {
                            float randnum;
                            //Записываем текст в файл
                            out.println(pa + " <== N");
                            out.println(pb + " <== M");
                            out.println("C, CX =   " + (float) ((int) (Math.random() * 10000)) / 100);
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < a; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nDN");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < a; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nDV");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < a; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nBN");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < b; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nBV");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < b; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nA");
                            for (int i = 0; i < a; i++) {
                                for (int j = 0; j < b; j++) {
                                    randnum = (float) ((int) (Math.random() * 1000)) / 100;
                                    out.print("\t" + randnum);
                                }
                                out.println("");
                            }
                            out.println("X");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < a; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nXopt");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < a; i++) {
                                out.print("\t" + randnum);
                            }
                            out.println("\nB");
                            randnum = (float) ((int) (Math.random() * 1000)) / 100;
                            for (int i = 0; i < b; i++) {
                                out.print("\t" + randnum);
                            }

                        } finally {
                            //После чего мы должны закрыть файл
                            //Иначе файл не запишется
                            out.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (vp1 == v1 && vp2 == v2 && vpt == v3) {
                    exist = true;
                }
                if (v1 == 1 && v2 == 1 && v3 == 1) {
                    exist = true;
                }
            }
        });
    }
}
