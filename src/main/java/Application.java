import fitness.FitnessFunction;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import saa.SimulatedAnnealing;
import window.AppTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    public FitnessFunction fitnessFunction() {
        /*sphere*/
        return params -> {
            BigDecimal result = BigDecimal.ZERO;
            for (BigDecimal param : params) {
                result = result.add(param.pow(2));
            }
            return BigDecimal.ZERO.subtract(result);
        };
    }

    @Override
    public void start(Stage stage) throws ExecutionException, InterruptedException {
        FitnessFunction fitnessFunction = fitnessFunction();
        FutureTask<List<BigDecimal>> simulatedAnnealingTask = getListFutureTask(fitnessFunction);
        new Thread(simulatedAnnealingTask).start();


        List<BigDecimal> data1 = simulatedAnnealingTask.get();
        XYChart.Series<Double, Double> series1 = new XYChart.Series<>();
        buildSeries(series1, data1);
        double maxData = data1.getLast().doubleValue();
        double minData = data1.getFirst().doubleValue();

        LineChart lc = new LineChart(
                new NumberAxis("迭代次数", 0.0, data1.size(), 1),
                new NumberAxis("目标函数值", -maxData, -minData, (maxData - minData) / data1.size())
        );
        lc.getData().addAll(series1);
        lc.setTitle("sphere函数");
        lc.setStyle("-fx-background-color: lightgray");
        lc.setCreateSymbols(false);
        lc.setLegendVisible(false);
        VBox vbox = new VBox(lc);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("模拟退火算法");
        stage.show();
    }

    private static FutureTask<List<BigDecimal>> getListFutureTask(FitnessFunction fitnessFunction) {
        int dim = 50;
        List<BigDecimal> positionUp = new ArrayList<>();
        List<BigDecimal> positionDown = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            positionUp.add(new BigDecimal(100));
        }
        for (int i = 0; i < dim; i++) {
            positionDown.add(new BigDecimal(-100));
        }
        int maxIt = 4000;
        BigDecimal ts = new BigDecimal("10000");
        BigDecimal d = new BigDecimal("0.9");
        BigDecimal te = new BigDecimal("1");
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(maxIt, fitnessFunction,
                dim, positionUp, positionDown, ts, d, te);
        simulatedAnnealing.init();
        return new FutureTask<>(new AppTask(simulatedAnnealing));
    }

    private void buildSeries(XYChart.Series<Double, Double> series, List<BigDecimal> data) {
        int i = 0;
        for (BigDecimal value : data) {
            series.getData().add(new XYChart.Data<>((double) i, -value.doubleValue()));
            i++;
        }
    }
}
