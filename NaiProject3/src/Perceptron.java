import java.util.*;

public class Perceptron {
    private final double STALA_UCZENIA = 0.5;
    private double[] wagi;
    private double prog;

    public Perceptron() {
        this.wagi = inicjalizacjaWag(26);
        this.prog = Math.random();
    }

    private double[] inicjalizacjaWag(int iloscWartosci) {
        double[] result = new double[iloscWartosci];
        for (int i = 0; i < iloscWartosci; i++) {
            result[i] = Math.random();
        }
        return result;
    }

    private double obliczSumeWazona(double[] wartosci) {
        double suma = 0;
        for (int i = 0; i < wagi.length; i++) {
            suma += wagi[i] * wartosci[i];
        }
        return suma;
    }

    private int porownanieProgu(double suma) {
        return suma < this.prog ? 0 : 1;
    }

    public void trenuj(List<double[]> dataSet, List<Integer> labels) {
        while (true) {
            int liczbaBledow = 0;
            for (int i = 0; i < dataSet.size(); i++) {
                double[] wejscie = dataSet.get(i);
                int oczekiwaneWyjscie = labels.get(i);
                double suma = obliczSumeWazona(wejscie);
                int outputUczenia = porownanieProgu(suma);
                int blad = oczekiwaneWyjscie - outputUczenia;

                if (blad != 0) {
                    liczbaBledow++;
                    for (int j = 0; j < wagi.length; j++) {
                        wagi[j] += blad * STALA_UCZENIA * wejscie[j];
                    }
                    prog -= blad * STALA_UCZENIA;
                }
            }
            if (liczbaBledow == 0) {
                break;
            }
        }
    }
    public int klasyfikacja(double[] wartosci) {
        double suma = obliczSumeWazona(wartosci);
        return porownanieProgu(suma);
    }

}
