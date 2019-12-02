package generator;

import java.io.FileWriter;
import java.io.IOException;

public class GeneratingProcessor {
    private static final int MMAX = 10;
    private static final int NMAX = 10;
    private static final int TMAX = 10;
    private double [][][] A = new double[TMAX + 1][MMAX + 1][NMAX + 1];
    private double[] C = new double[NMAX + 1];
    private double[] DN = new double[NMAX + 1];
    private double[] DV = new double[NMAX + 1];
    private double[] DEL = new double[NMAX + 1];
    private double[] BN = new double[MMAX + 1];
    private double[] BV = new double[MMAX + 1];
    private double[] Y = new double[MMAX + 1];
    private double[] X = new double[NMAX + 1];
    private double[] AX = new double[MMAX + 1];
    private Double CX = 0d;
    private String buf = "";
    private String buff = "";

    private int l;
    private int k;

    private Double[] result;
    private double [][][] dMatrixOut;

    private double[][] planOut;

    private int mOut;
    private int nOut;
    private Double[] previousResult;

    public GeneratingProcessor(double[][][] dMatrixOut, double[][] planOut, int nOut, int mOut, int currentInterval, int currentTask, Double[] previousResult) {
        this.dMatrixOut = dMatrixOut;
        this.planOut = planOut;
        this.nOut = nOut;
        this.mOut = mOut;
        this.l = currentInterval;
        this.k = currentTask;
        this.previousResult = previousResult;
    }

    private static double rand1(double rfrom, double rto) {
        long i;
        double d = Math.random();
        d = rfrom + d * (rto - rfrom);
        i = Math.round(d * 10);
        d = i;
        return d / 10;
    }

    private static double maxX(double[] val, int size) {
        double maxVal = val[1];
        for(int i = 1; i <= size; i++) //TODO size?
        {
            if(val[i] >= maxVal)
            {
                maxVal = val[i];
            }
        }
        return maxVal;
    }


    private static double minX(double[] val, int size) {
        double minVal = val[1];
        for(int i = 1; i <= size; i++) //TODO size?
        {
            if(val[i] <= minVal)
            {
                minVal = val[i];
            }
        }
        return minVal;
    }


    public void process() throws IOException {
        FileWriter fo;

                buff = "" + l;
                buf = "" + k;
                fo = new FileWriter("out" + buff + "_" + buf + ".txt");
                if(l == 1) {
                    fo.write(nOut + "  <==N" + '\n');
                    fo.write(mOut + "  <==N" + '\n');

                    if(nOut > NMAX || mOut > MMAX)
                    {
                        return;
                    }
                }

                if (k == 1) {
                    for (int j = 1; j <= nOut; j++) {
                        X[j] = planOut[l][j]; //TODO l?
                    }
                }

                for (int j = 1; j <= nOut; j++) {
                    if(k == 1) {
                        if(X[j] < (minX(X, nOut) + 0.35 * (maxX(X, nOut) - minX(X, nOut)))) {
                            DN[j] = X[j];
                            DV[j] = maxX(X, nOut);
                            DEL[j] = rand1(DN[j]+0.25*(DV[j]-DN[j]),DN[j]+0.45*(DV[j]-DN[j]));
                        }
                        if(X[j]>=(minX(X, nOut)+0.35*(maxX(X, nOut)-minX(X, nOut))) && X[j]<=(minX(X, nOut)+0.65*(maxX(X, nOut)-minX(X, nOut)))) {
                            DN[j] = minX(X, nOut);
                            DV[j] = maxX(X, nOut);
                            DEL[j] = (maxX(X, nOut)-minX(X, nOut)) / 2;
                        }
                        if(X[j]>(minX(X, nOut)+0.65*(maxX(X, nOut)-minX(X, nOut)))) {
                            DN[j]=minX(X, nOut);
                            DV[j]=X[j];
                            DEL[j]=rand1(DN[j]+0.55*(DV[j]-DN[j]),DN[j]+0.75*(DV[j]-DN[j]));
                        }
                    }
                    else {
                        DN[j]=BN[j];
                        DV[j]=BV[j];
                        X[j] = previousResult[j - 1]; //X[j]=AX[j]; //+DB[j] before
                        if(X[j]<(DN[j]+0.35*(DV[j]-DN[j])))
                        {
                            DEL[j]=rand1(DN[j]+0.25*(DV[j]-DN[j]),DN[j]+0.45*(DV[j]-DN[j]));
                        }
                        if (X[j]>=(DN[j]+0.35*(DV[j]-DN[j])) && X[j]<=(DN[j]+0.65*(DV[j]-DN[j])))
                        {
                            DEL[j]=(DN[j]+DV[j])/2;
                        }
                        if (X[j]>(DN[j]+0.65*(DV[j]-DN[j])))
                        {
                            DEL[j] = rand1(DN[j]+0.55*(DV[j]-DN[j]),DN[j]+0.75*(DV[j]-DN[j]));
                        }
                    }
                }

                    for (int i = 1; i <= mOut; i++) {
                        AX[i] = 0d;
                        for (int j = 1; j <= nOut; j++) {
                            A[k][i][j] = dMatrixOut[k][i][j];
                            AX[i]=AX[i]+A[k][i][j]*X[j];
                        }
                    }

                for (int i = 1; i <= mOut; i++) {
                    if(AX[i]<(minX(AX, mOut)+0.35*(maxX(AX, mOut)-minX(AX, mOut)))) {
                        BN[i]=AX[i];
                        BV[i]=maxX(AX, mOut);
                        Y[i]=rand1(BN[i]+0.25*(BV[i]-BN[i]),BN[i]+0.45*(BV[i]-BN[i]));
                    }
                    if((AX[i]>=(minX(AX, mOut)+0.35*(maxX(AX, mOut)-minX(AX, mOut)))) && (AX[i]<=(minX(AX, mOut)+0.65*(maxX(AX, mOut)-minX(AX, mOut))))) {
                        BN[i]=minX(AX, mOut);
                        BV[i]=maxX(AX, mOut);
                        Y[i]=(maxX(AX, mOut)-minX(AX, mOut))/2;
                    }
                    if(AX[i]>(minX(AX, mOut)+0.65*(maxX(AX, mOut)-minX(AX, mOut)))) {
                        BN[i]=minX(AX, mOut);
                        BV[i]=AX[i];
                        Y[i]=rand1(BN[i]+0.55*(BV[i]-BN[i]),BN[i]+0.75*(BV[i]-BN[i]));
                    }
                }

                CX=0d;
                for (int j = 1; j <= nOut; j++) {
                    C[j]=DEL[j];
                    for (int i = 1; i <= mOut; i++) {
                        C[j]=C[j]+A[k][i][j]*Y[i];
                    }
                    CX=CX+C[j]*X[j];
                }

                fo.write("C, CX=  " + CX + '\n');
                for (int j = 1; j <= nOut; j++) {
                    fo.write("" + C[j] + "   \n");
                }
                fo.write("DN\n");
                for (int j = 1; j <= nOut; j++) {
                    fo.write("" + DN[j] + "   \n");
                }
                fo.write("DV\n");
                for (int j = 1; j <= nOut; j++) {
                    fo.write("" + DV[j] + "   \n");
                }
                fo.write("BN\n");
                for (int j = 1; j <= mOut; j++) {
                    fo.write("" + BN[j] + "   \n");
                }
                fo.write("BV\n");
                for (int j = 1; j <= mOut; j++) {
                    fo.write("" + BV[j] + "   \n");
                }

                fo.write("A\n");
                for (int i = 1; i <= mOut; i++) {
                    for (int j = 1; j <= nOut; j++) {
                        fo.write("" + A[k][i][j] + "   ");
                    }
                    fo.write('\n');
                }

                fo.write("X\n");
                for (int j = 1; j <= nOut; j++) {
                    fo.write("" + DV[j] + "   ");
                }
                fo.write('\n');

                fo.write("Xopt\n");
                for (int j = 1; j <= nOut; j++) {
                    fo.write("" + X[j] + "   ");
                }
                fo.write('\n');

                fo.write("B\n");
                Double[] localAX = new Double[mOut];
                for (int j = 1; j <= mOut; j++) {
                    fo.write("" + AX[j] + "   ");
                    localAX[j - 1] = AX[j];
                }
                result = localAX;
                fo.write('\n');
                fo.close();
    }

    public Double[] getResult() {
        return result;
    }
}