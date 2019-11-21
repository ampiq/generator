package generator.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OutputController {

    @FXML
    private Button nextButton;

    @FXML
    private TableView<Double[]> matrixb;

    @FXML
    private Label inter1;

    @FXML
    private TableView<Double[]> vectorb1;

    @FXML
    private Label inter2;

    @FXML
    private TextField valueN;

    @FXML
    private TextField valueM;

    @FXML
    private TableView<Double[]> planp;

    @FXML
    private TextField value1Text;

    @FXML
    private TextField value2Text;

    private int vp1 = 1;
    private int vp2 = 1;
    private int vpt = 1;
    private int val;
    private boolean exist = false;

    private List<Double[][]> matricesB;

    private List<Double[][]> plansP;

    private List<Double[]> result;

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
            System.out.println(vp1);
            System.out.println(vp2);
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

                planp.getColumns().setAll(createColumns());
                planp.setItems(receiveData(plansP.get(vp1)));
                matrixb.getColumns().setAll(createColumns());
                matrixb.setItems(receiveData(matricesB.get(vp2)));
                vectorb1.getColumns().setAll(createColumns());
                vectorb1.setItems(receiveDataForResult(result.get(vp1 +vp2 - 2)));

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

                if (vp1 == v1 && vp2 == v2) {
                    exist = true;
                }
                if (v1 == 1 && v2 == 1) {
                    exist = true;
                }
            }
        });
    }

    private ObservableList<double[]> generateData(int nValue, int mValue) {
        return FXCollections.observableArrayList(
                IntStream.range(0, nValue)
                        .mapToObj(r ->
                                        IntStream.range(0, mValue)
//                                        .mapToDouble(c -> ThreadLocalRandom.current().nextInt(1, 20))
                                                .mapToDouble(c -> 3)
                                                .toArray()
                        ).collect(Collectors.toList())
        );
    }

    private List<TableColumn<Double[], String>> createColumns() {
        return IntStream.range(0, Integer.parseInt(valueN.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<Double[], String> createColumn(int c) {
        TableColumn<Double[], String> col = new TableColumn<>("C" + (c + 1));
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue()[c])));
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        return col;
    }

    private double[][] getTableAsArray(TableView<double[]> matrix) {
        double[][] arr = new double[matrix.getItems().size()][matrix.getItems().get(0).length];
        for (int i = 0; i < matrix.getItems().size(); i++) {
            for (int j = 0; j < matrix.getItems().get(0).length; j++) {
                arr[i][j] = matrix.getItems().get(i)[j];
            }
        }
        return arr;
    }

    private TableView<Double[]> getArrayAsTable(Double[][] matrix) {
        return new TableView<>(receiveData(matrix));
    }

    private ObservableList<Double[]> receiveDataForResult(Double[] arr) {
        List<Double[]> list = new ArrayList<>();
        list.add(arr);
        return FXCollections.observableArrayList(list);
    }

    private ObservableList<Double[]> receiveData(Double[][] arr) {
        ObservableList<Double[]> lst = FXCollections.observableArrayList();
        for (int i = 0; i < arr.length; i++) {
            lst.add(arr[i]);
        }
        return lst;
    }

    public void setMatricesB(List<Double[][]> matricesB) {
        this.matricesB = matricesB;
    }

    public void setPlansP(List<Double[][]> plansP) {
        this.plansP = plansP;
    }

    public void setResult(List<Double[]> result) {
        this.result = result;
    }
}
