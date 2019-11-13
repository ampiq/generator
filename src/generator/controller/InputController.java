package generator.controller;

import generator.GeneratingProcessor;
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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
    private int inter;
    private int iter;
    private int vp1 = 1;
    private int vp2 = 1;
    private boolean exist = false;

    @FXML
    void initialize() {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        planp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.setEditable(true);
        matrixb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        planp.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.getSelectionModel().setCellSelectionEnabled(true);
        matrixb.setEditable(true);

        nextButton.setOnAction(event -> {
            String value1 = valueM.getText();
            String value2 = valueN.getText();
            String value3 = value1Text.getText();
            String value4 = value2Text.getText();
            iter = Integer.parseInt(value1Text.getText());
            inter = Integer.parseInt(value2Text.getText());

            if (exist) {
                GeneratingProcessor generate = new GeneratingProcessor(iter, inter, bMatrix, plan, arrN, arrM);
                try {
                    generate.process();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                openNewScene("/generator/scene/output.fxml");
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

                planp.getColumns().clear();
                matrixb.getColumns().clear();
                planp.getItems().clear();
                matrixb.getItems().clear();

                inter1.setText("Количество временных интервалов: (" + vp1 + " из " + v1 + ")");
                inter2.setText("Количество решаемых задач: (" + (vp2) + " из " + v2 + ")");
                if (vp1 <= v1 && v2 == vp2) {
                    vp1++;
                    vp2 = 1;
                } else {
                    vp2++;
                }

                planp.getColumns().setAll(createColumns());
                planp.setItems(generateData(1, Integer.parseInt(valueN.getText())));
                matrixb.getColumns().setAll(createColumns());
                matrixb.setItems(generateData(Integer.parseInt(valueM.getText()), Integer.parseInt(valueN.getText())));

                double[][] tableAsArray = getTableAsArray(matrixb);
                for (int i = 0; i < tableAsArray.length; i++) {
                    System.arraycopy(tableAsArray[i], 0, bMatrix[vp2][i + 1], 1, tableAsArray[0].length);
                }

                double[] doubles = getTableAsArray(planp)[0];
                System.arraycopy(doubles, 0, plan[vp1], 1, doubles.length);

                arrN[vp2] = a;
                arrM[vp2] = b;
                value1Text.setDisable(true);
                value2Text.setDisable(true);
                valueN.setDisable(true);
                valueN.setText(valueM.getText());
                valueM.clear();

                if (vp1 == v1 && vp2 == v2) {
                    exist = true;
                }
                if (v1 == 1 && v2 == 1) {
                    openNewScene("/generator/scene/output.fxml");
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

    private List<TableColumn<double[], String>> createColumns() {
        return IntStream.range(0, Integer.parseInt(valueN.getText()))
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<double[], String> createColumn(int c) {
        TableColumn<double[], String> col = new TableColumn<>("C" + (c + 1));
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

    private void openNewScene(String window) {
        nextButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource(window)));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}