package generator.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import generator.GeneratingProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputController {

    @FXML
    private Label inter1;

    @FXML
    private Label inter2;

    @FXML
    private Button nextButton;

    @FXML
    private Button saveDataButton;

    @FXML
    private Button showTableButton;

    @FXML
    private TableView<double[]> matrixb;

    @FXML
    private TextField valueN;

    @FXML
    private TextField valueM;

    @FXML
    private TableView<double[]> planp;

    @FXML
    private TextField value1Text;

    @FXML
    private TextField value2Text;

    private static final int IMAX = 50;
    private static final int TMAX = 10;

    private double [][] plan = new double[IMAX + 1][IMAX + 1];
    private double [][][] bMatrix = new double[TMAX + 1][TMAX + 1][TMAX + 1];
    private int [] arrN = new int[IMAX + 1];
    private int [] arrM = new int[IMAX + 1];

    private int currentInterval = 1;
    private int currentZadacha = 1;

    private List<Double[][]> plansP = new ArrayList<>();
    private List<Double[][]> matricesB = new ArrayList<>();
    private List<Double[]> resultB = new ArrayList<>();

    private List<Dimension> dimensions = new ArrayList<>();

    @FXML
    void initialize() {

        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.setEditable(true);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.setEditable(true);

        nextButton.setOnAction(event -> {
            if (currentZadacha == Integer.parseInt(value2Text.getText()) && currentInterval == Integer.parseInt(value1Text.getText())) {
                showSpecificAlert(Alert.AlertType.INFORMATION, "Завершение работы", "Программа успешно завершила работу");
                nextButton.getScene().getWindow().hide();
            }

            try {
                if(currentZadacha + 1 > Integer.parseInt(value2Text.getText())) {
                    ++currentInterval;
                    currentZadacha = 1;
                } else {
                    ++currentZadacha;
                }
                if(currentInterval == 1) {
                    valueN.setText(Integer.toString(dimensions.get(currentZadacha - 2).mValue));
                    valueM.setDisable(false);
                } else {
                    valueN.setText(Integer.toString(dimensions.get(currentZadacha - 1).nValue));
                    valueM.setText(Integer.toString(dimensions.get(currentZadacha - 1).mValue));
                }
            } catch (IndexOutOfBoundsException e) {
                --currentZadacha; //TODO 
                System.out.println("Press Show table");
                showSpecificAlert(Alert.AlertType.ERROR, "Ошибка ввода", "Сгенерируйте таблицу, нажав Show table");
            }
            planp.getColumns().clear();
            matrixb.getColumns().clear();

            inter1.setText("Количество временных интервалов: (" + currentInterval + " из " + value1Text.getText() + ")");
            inter2.setText("Количество решаемых задач: (" + currentZadacha + " из " + value2Text.getText() + ")");
        });

        showTableButton.setOnAction(event -> {
            int problemsToBeSolved = Integer.parseInt(value2Text.getText());
            int timeIntervals = Integer.parseInt(value1Text.getText());
            int mValue = Integer.parseInt(valueM.getText());
            int nValue = Integer.parseInt(valueN.getText());

            if(currentInterval == 1) {
                dimensions.add(new Dimension(mValue, nValue));
            }

            if(currentZadacha == 1 && currentInterval == 1) {
                inter1.setText("Количество временных интервалов: (" + currentInterval + " из " + timeIntervals + ")");
                inter2.setText("Количество решаемых задач: (" + (currentZadacha) + " из " + problemsToBeSolved + ")");
            }

            planp.getColumns().setAll(createColumns());
            matrixb.getColumns().setAll(createColumns());

            planp.setItems(generateDataInitial(1, nValue));
            matrixb.setItems(generateDataInitial(mValue, nValue));

            value1Text.setDisable(true);
            value2Text.setDisable(true);
            valueM.setDisable(true);
            valueN.setDisable(true);
        });

        saveDataButton.setOnAction(event -> {
            System.out.println('\n' + "saveDataButton");

            try {
            if (currentInterval == 1) {
                double[][] tableAsArray = getTableAsArray(matrixb);
                for (int i = 0; i < tableAsArray.length; i++) {
                    System.arraycopy(tableAsArray[i], 0, bMatrix[currentZadacha][i + 1], 1, tableAsArray[0].length);
                }
                matricesB.add(getTableAsDoubleArray(matrixb));
                arrN[currentZadacha] = dimensions.get(currentZadacha - 1).nValue;
                arrM[currentZadacha] = dimensions.get(currentZadacha - 1).mValue;
            }

            if (currentZadacha == 1) {
                double[] doubles = getTableAsArray(planp)[0];
                System.arraycopy(doubles, 0, plan[currentInterval], 1, doubles.length);
                plansP.add(getTableAsDoubleArray(planp));
            }

            System.out.println("bMatrix: " + Arrays.toString(bMatrix[currentInterval][currentZadacha]));
            System.out.println("plan: " + Arrays.toString(plan[currentZadacha]));
            System.out.println("arrN: " + arrN[currentZadacha]);
            System.out.println("arrM: " + arrM[currentZadacha]);
            System.out.println("currentInterval: " + currentInterval);
            System.out.println("currentZadacha: " + currentZadacha);

            GeneratingProcessor generate = new GeneratingProcessor(bMatrix, plan, arrN[currentZadacha], arrM[currentZadacha], currentInterval, currentZadacha, currentZadacha == 1 ? null : resultB.get(currentZadacha - 2)); //TODO
            System.out.println(generate);
            generate.process();

            Double[] result = generate.getResult();
            resultB.add(result);
            System.out.println("resultB: " + Arrays.toString(result));

                openNewSceneWithParam("/scene/output.fxml", matricesB, plansP, result);
            } catch (IOException e) {
                System.out.println("Something went wrong with IO");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Press Show table");
                showSpecificAlert(Alert.AlertType.ERROR, "Ошибка ввода", "Сгенерируйте таблицу, нажав Show table");
            }
        });
    }

    private void showSpecificAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openNewSceneWithParam(String window, List<Double[][]> mtrcsB, List<Double[][]> mtrcsP, Double[] result) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource(window)));
        loader.load();
        Parent root = loader.getRoot();
        OutputController oc = loader.getController();
        oc.setMatricesB(mtrcsB);
        oc.setPlansP(mtrcsP);
        oc.setResult(result);
        oc.setValueN(valueN.getText());
        oc.setValueM(valueM.getText());
        oc.setValue1Text(value1Text.getText());
        oc.setValue2Text(value2Text.getText());
        oc.setL(currentInterval);
        oc.setK(currentZadacha);
        oc.compute();
        loader.setController(oc);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private ObservableList<double[]> generateDataInitial(int nValue, int mValue) {
        return FXCollections.observableArrayList(
                IntStream.range(0, nValue)
                        .mapToObj(r -> IntStream.range(0, mValue).mapToDouble(c -> 0).toArray()).collect(Collectors.toList())
        );
    }

    private List<TableColumn<double[], String>> createColumns() {
        return IntStream.range(0, Integer.parseInt(valueN.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<double[], String> createColumn(int c) {
        TableColumn<double[], String> col = new TableColumn<>("C" + (c + 1));
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(String.valueOf(param.getValue()[c])));
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(event ->
                event.getTableView().getItems().get(event.getTablePosition().getRow())[c] = Integer.parseInt(event.getNewValue()));
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

    private Double[][] getTableAsDoubleArray(TableView<double[]> matrix) {
        Double[][] arr = new Double[matrix.getItems().size()][matrix.getItems().get(0).length];
        for (int i = 0; i < matrix.getItems().size(); i++) {
            for (int j = 0; j < matrix.getItems().get(0).length; j++) {
                arr[i][j] = matrix.getItems().get(i)[j];
            }
        }
        return arr;
    }

    private class Dimension {
        public int mValue;
        public int nValue;

        public Dimension(int m, int n) {
            mValue = m;
            nValue = n;
        }
    }
}