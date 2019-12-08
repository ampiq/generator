package generator.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OutputController {

    @FXML
    private Label inter1;

    @FXML
    private Label inter2;

    @FXML
    private Button okButton;

    @FXML
    private TableView<Double[]> matrixb;

    @FXML
    private TableView<Double[]> vectorb1;

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

    private List<Double[][]> matricesB;

    private List<Double[][]> plansP;

    private Double[] result;

    private int l;

    private int k;

    @FXML
    void initialize() {
        value1Text.setDisable(true);
        value2Text.setDisable(true);
        valueM.setDisable(true);
        valueN.setDisable(true);
        okButton.setOnAction(this::handleCloseButtonAction);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    void compute() {
        inter1.setText("Количество временных интервалов: (" + l + " из " + value1Text.getText() + ")");
        inter2.setText("Количество решаемых задач: (" + k + " из " + value2Text.getText() + ")");

        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vectorb1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        vectorb1.getSelectionModel().setCellSelectionEnabled(true);

        planp.getColumns().setAll(createColumns());
        if(k == 1) {
            planp.setItems(receiveData(plansP.get(l - 1)));
        } else {

        }
        matrixb.getColumns().setAll(createColumns());
        matrixb.setItems(receiveData(matricesB.get(k - 1)));
        vectorb1.getColumns().setAll(createColumnsForB());
        vectorb1.setItems(receiveDataForResult(result));
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

    private List<TableColumn<Double[], String>> createColumnsForB() {
        return IntStream.range(0, Integer.parseInt(valueM.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
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

    public void setResult(Double[] result) {
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

    public void setL(int l) {
        this.l = l;
    }

    public void setK(int k) {
        this.k = k;
    }
}
