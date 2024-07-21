import fitness.FitnessFunction;
import saa.SimulatedAnnealing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        FitnessFunction fitnessFunction = params -> {
            BigDecimal result = BigDecimal.ZERO;
            for (BigDecimal param : params) {
                result = result.add(param.pow(2));
            }
            return BigDecimal.ZERO.subtract(result);
        };
        int dim = 50;
        List<BigDecimal> positionUp = new ArrayList<>();
        List<BigDecimal> positionDown = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            positionUp.add(new BigDecimal(1000));
        }
        for (int i = 0; i < dim; i++) {
            positionDown.add(new BigDecimal(-1000));
        }
        int maxIt = 4000;
        BigDecimal ts = new BigDecimal("10000");
        BigDecimal d = new BigDecimal("0.9");
        BigDecimal te = new BigDecimal("1");
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(maxIt, fitnessFunction,
                dim, positionUp, positionDown, ts, d, te);
        simulatedAnnealing.init();
        simulatedAnnealing.iterative();


    }

}
