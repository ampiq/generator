package generator.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс, представляющий контроллер вывода данных.
 */
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

    private Double[] planFromInput;

    private Double[] result;

    private int l;

    private int k;

    /**
     * Метод инициализации.
     */
    @FXML
    void initialize() {
        value1Text.setDisable(true);
        value2Text.setDisable(true);
        valueM.setDisable(true);
        valueN.setDisable(true);
        okButton.setOnAction(this::handleCloseButtonAction);
    }

    /**
     * Обработчик закрытия окна.
     * @param  event  Событие.
     */
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Сгенерировать таблицы.
     */
    void compute() {
        inter1.setText("Количество временных интервалов: (" + l + " из " + value1Text.getText() + ")");
        inter2.setText("Количество решаемых задач: (" + (Integer.parseInt(value2Text.getText()) + 1 - k) + " из " + value2Text.getText() + ")");

        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vectorb1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        vectorb1.getSelectionModel().setCellSelectionEnabled(true);

        planp.getColumns().setAll(createColumns());
        planp.setItems(receiveDataForResult(planFromInput));

        matrixb.getColumns().setAll(createColumns());
        matrixb.setItems(receiveData(matricesB.get(k - 1)));
        vectorb1.getColumns().setAll(createColumnsForB());
        vectorb1.setItems(receiveDataForResult(result));
    }

    /**
     * Создать столбцы для вектора B.
     * @return List<TableColumn<double[], String>>
     */
    private List<TableColumn<Double[], String>> createColumnsForB() {
        return IntStream.range(0, Integer.parseInt(valueM.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    /**
     * Создать столбцы.
     * @return List<TableColumn<double[], String>>
     */
    private List<TableColumn<Double[], String>> createColumns() {
        return IntStream.range(0, Integer.parseInt(valueN.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    /**
     * Создать столбец.
     * @param  c  Название столбца.
     * @return TableColumn<double[], String>
     */
    private TableColumn<Double[], String> createColumn(int c) {
        TableColumn<Double[], String> col = new TableColumn<>("C" + (c + 1));
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue()[c])));
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        return col;
    }

    /**
     * Преобразовать вектор в данные для таблицы.
     * @param  arr Вектор.
     * @return ObservableList<double[]>
     */
    private ObservableList<Double[]> receiveDataForResult(Double[] arr) {
        ObservableList<Double[]> list = FXCollections.observableArrayList();
        list.add(arr);
        return list;
    }

    /**
     * Преобразовать матрицу в данные для таблицы.
     * @param  arr Матрица.
     * @return ObservableList<double[]>
     */
    public static ObservableList<Double[]> receiveData(Double[][] arr) {
        ObservableList<Double[]> lst = FXCollections.observableArrayList();
        for (int i = 0; i < arr.length; i++) {
            lst.add(arr[i]);
        }
        return lst;
    }

    public void setMatricesB(List<Double[][]> matricesB) {
        this.matricesB = matricesB;
    }

    public void setPlansP(Double[] plansP) {
        this.planFromInput = plansP;
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
