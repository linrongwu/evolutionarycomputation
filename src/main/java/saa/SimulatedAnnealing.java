package saa;

import fitness.FitnessFunction;
import swarmIntelligence.SwarmIntelligence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealing implements SwarmIntelligence {
    private final FitnessFunction fitnessFunction;
    private final Integer dim;//问题维度
    private final List<BigDecimal> up;//上界
    private final List<BigDecimal> down;//下界
    private final BigDecimal ts;
    private final BigDecimal d;
    private final BigDecimal te;
    private final Integer maxIt;
    private BigDecimal t;
    private List<BigDecimal> x;
    private final List<BigDecimal> bestX;

    public SimulatedAnnealing(Integer maxIt, FitnessFunction fitnessFunction,
                              Integer dim, List<BigDecimal> up, List<BigDecimal> down,
                              BigDecimal ts, BigDecimal d, BigDecimal te) {
        this.fitnessFunction = fitnessFunction;
        this.dim = dim;
        this.up = up;
        this.down = down;
        this.ts = ts;
        this.d = d;
        this.te = te;
        this.maxIt = maxIt;
        this.bestX = new ArrayList<>();
        this.x = new ArrayList<>();
    }

    public void init() {
        for (int i = 0; i < dim; i++) {
            BigDecimal data = up.get(i).subtract(down.get(i))
                    .multiply(BigDecimal.valueOf(Math.random()))
                    .add(down.get(i));
            x.add(data);
            bestX.add(data);
        }
        t = ts;
    }

    public List<BigDecimal> iterative() {
        List<BigDecimal> data = new ArrayList<>();
        while (t.compareTo(te) > 0) {
            for (int i = 0; i < maxIt; i++) {
                List<BigDecimal> tempx = random(x, up, down);
                BigDecimal bestY = fitnessFunction.fitnessFunction(bestX);
                BigDecimal tempY = fitnessFunction.fitnessFunction(tempx);
                BigDecimal currY = fitnessFunction.fitnessFunction(x);
                if (tempY.compareTo(bestY) >= 0) {
                    x = tempx;
                    setBestx();
                } else if (tempY.compareTo(currY) >= 0) {
                    x = tempx;
                } else {
                    double p = Math.exp(-(bestY.doubleValue() - tempY.doubleValue()) / t.doubleValue());
                    if (p > Math.random()) {
                        x = tempx;
                    }
                }
            }
            t = t.multiply(d);
            data.add(fitnessFunction.fitnessFunction(bestX));
        }
        return data;
    }

    private List<BigDecimal> random(List<BigDecimal> x, List<BigDecimal> up, List<BigDecimal> down) {
        List<BigDecimal> tempx = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            BigDecimal step = up.get(i).subtract(down.get(i)).divide(BigDecimal.valueOf(maxIt), 2, RoundingMode.DOWN);
            BigDecimal data = x.get(i).add(step.multiply(BigDecimal.valueOf(Math.random() * 2 - 1)));
            if (data.compareTo(up.get(i)) >= 0) {
                data = up.get(i);
            } else if (data.compareTo(down.get(i)) <= 0) {
                data = down.get(i);
            }
            tempx.add(data);
        }
        return tempx;
    }


    private void setBestx() {
        for (int i = 0; i < dim; i++) {
            bestX.set(i, x.get(i));
        }
    }

}
