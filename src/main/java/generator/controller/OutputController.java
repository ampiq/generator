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

    private int vp1 = 0;
    private int vp2 = 0;

    private List<Double[][]> matricesB;

    private List<Double[][]> plansP;

    private List<Double[]> result;

    private int l;

    private int k;

    @FXML
    void initialize() {
        value1Text.setDisable(true);
        value2Text.setDisable(true);
        valueM.setDisable(true);
        valueN.setDisable(true);
    }

    void compute() {


        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vectorb1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        vectorb1.getSelectionModel().setCellSelectionEnabled(true);

        planp.getColumns().setAll(createColumns());
        planp.setItems(receiveData(plansP.get(vp1)));
        matrixb.getColumns().setAll(createColumns());
        matrixb.setItems(receiveData(matricesB.get(vp2)));
        vectorb1.getColumns().setAll(createColumns());
        vectorb1.setItems(receiveDataForResult(result.get(vp1 + vp2)));
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
        ObservableList<Double[]> list = FXCollections.observableArrayList();
        list.add(arr);
        return list;
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

    public void setValueN(String valueN) {
        this.valueN.setText(valueN);
    }

    public void setValueM(String valueM) {
        this.valueM.setText(valueM);
    }

    public void setValue1Text(String value1Text) {
        this.value1Text.setText(value1Text);
    }

    public void setValue2Text(String value2Text) {
        this.value2Text.setText(value2Text);
    }
}
