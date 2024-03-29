package generator;

import java.io.FileWriter;
import java.io.IOException;

public class GeneratingProcessor {
    private static final int MMAX = 10;
    private static final int NMAX = 10;
    private static final int TMAX = 10;
    private static final int IMAX = 50;
    private int[] M = new int[IMAX + 1];
    private int[] N = new int[IMAX + 1];
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

    private int iterOut;
    private int interOut;
    private double [][][] dMatrixOut;
    private double[][] planOut;
    private int[] mOut;
    private int[] nOut;

    public GeneratingProcessor(Integer iterOut, Integer interOut, double[][][] dMatrixOut, double[][] planOut, int[] mOut, int[] nOut) {
        this.iterOut = iterOut;
        this.interOut = interOut;
        this.dMatrixOut = dMatrixOut;
        this.planOut = planOut;
        this.mOut = mOut;
        this.nOut = nOut;
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
        for(int i = 1; i <= size - 1; i++)
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
        for(int i = 1; i <= size - 1; i++)
        {
            if(val[i] <= minVal && val[i] != 0d) //TODO &&
            {
                minVal = val[i];
            }
        }
        return minVal;
    }


    public void process() throws IOException {
        FileWriter fo;
        int inter = interOut;
        int iter = iterOut;

        for(int l = 1;  l <= inter; l++) {
            for(int k = 1; k <= iter; k++) {
                buff = "" + l;
                buf = "" + k;
                fo = new FileWriter("out" + buff + "_" + buf + ".txt");
                if(l == 1) {
                    if(k == 1)
                    {
                        N[k] = nOut[k];
                    }
                    else
                    {
                        N[k] = M[k - 1];
                    }
                    M[k] = mOut[k];
                    fo.write(N[k] + "  <==N" + '\n');
                    fo.write(M[k] + "  <==N" + '\n');

                    if(N[k] > NMAX || M[k] > MMAX)
                    {
                        return;
                    }
                }
                if(k == 1) {
                    for(int j = 1; j <= N[k]; j++) {
                        X[j] = planOut[l][j];
                    }
                }

                for (int j = 1; j <= N[k]; j++) {
                    if(k == 1) {
                        if(X[j] < (minX(X, N[k]) + 0.35 * (maxX(X, N[k]) - minX(X, N[k])))) {
                            DN[j] = X[j];
                            DV[j] = maxX(X, N[k]);
                            DEL[j] = rand1(DN[j]+0.25*(DV[j]-DN[j]),DN[j]+0.45*(DV[j]-DN[j]));
                        }
                        if(X[j]>=(minX(X, N[k])+0.35*(maxX(X, N[k])-minX(X, N[k]))) && X[j]<=(minX(X, N[k])+0.65*(maxX(X, N[k])-minX(X, N[k])))) {
                            DN[j] = minX(X, N[k]);
                            DV[j] = maxX(X, N[k]);
                            DEL[j] = (maxX(X, N[k])-minX(X, N[k])) / 2;
                        }
                        if(X[j]>(minX(X, N[k])+0.65*(maxX(X, N[k])-minX(X, N[k])))) {
                            DN[j]=minX(X, N[k]);
                            DV[j]=X[j];
                            DEL[j]=rand1(DN[j]+0.55*(DV[j]-DN[j]),DN[j]+0.75*(DV[j]-DN[j]));
                        }
                    }
                    else {
                        DN[j]=BN[j];
                        DV[j]=BV[j];
                        X[j]=AX[j]; //+DB[j] before
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

                if(l == 1) {
                    for (int i = 1; i <= M[k]; i++) {
                        AX[i] = 0d;
                        for (int j = 1; j <= N[k]; j++) {
                            A[k][i][j] = dMatrixOut[k][i][j];
                            AX[i]=AX[i]+A[k][i][j]*X[j];
                        }
                    }
                }
                else {
                    for (int i = 1; i <= M[k]; i++) {
                        AX[i]=0d;
                        for (int j = 1; j <= N[k]; j++) {
                            AX[i]=AX[i]+A[k][i][j]*X[j];
                        }
                    }
                }

                for (int i = 1; i <= M[k]; i++) {
                    if(AX[i]<(minX(AX, M[k])+0.35*(maxX(AX, M[k])-minX(AX, M[k])))) {
                        BN[i]=AX[i];
                        BV[i]=maxX(AX, M[k]);
                        Y[i]=rand1(BN[i]+0.25*(BV[i]-BN[i]),BN[i]+0.45*(BV[i]-BN[i]));
                    }
                    if((AX[i]>=(minX(AX, M[k])+0.35*(maxX(AX, M[k])-minX(AX, M[k])))) && (AX[i]<=(minX(AX, M[k])+0.65*(maxX(AX, M[k])-minX(AX, M[k]))))) {
                        BN[i]=minX(AX, M[k]);
                        BV[i]=maxX(AX, M[k]);
                        Y[i]=(maxX(AX, M[k])-minX(AX, M[k]))/2;
                    }
                    if(AX[i]>(minX(AX, M[k])+0.65*(maxX(AX, M[k])-minX(AX, M[k])))) {
                        BN[i]=minX(AX, M[k]);
                        BV[i]=AX[i];
                        Y[i]=rand1(BN[i]+0.55*(BV[i]-BN[i]),BN[i]+0.75*(BV[i]-BN[i]));
                    }
                }

                CX=0d;
                for (int j = 1; j <= N[k]; j++) {
                    C[j]=DEL[j];
                    for (int i = 1; i <= M[k]; i++) {
                        C[j]=C[j]+A[k][i][j]*Y[i];
                    }
                    CX=CX+C[j]*X[j];



                }

                fo.write("C, CX=  " + CX + '\n');
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + C[j] + "   \n");
                }
                fo.write("DN\n");
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + DN[j] + "   \n");
                }
                fo.write("DV\n");
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + DV[j] + "   \n");
                }
                fo.write("BN\n");
                for (int j = 1; j <= M[k]; j++) {
                    fo.write("" + BN[j] + "   \n");
                }
                fo.write("BV\n");
                for (int j = 1; j <= M[k]; j++) {
                    fo.write("" + BV[j] + "   \n");
                }

                fo.write("A\n");
                for (int i = 1; i <= M[k]; i++) {
                    for (int j = 1; j <= N[k]; j++) {
                        fo.write("" + A[k][i][j] + "   ");
                    }
                    fo.write('\n');
                }

                fo.write("X\n");
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + DV[j] + "   ");
                }
                fo.write('\n');

                fo.write("Xopt\n");
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + X[j] + "   ");
                }
                fo.write('\n');

                fo.write("B\n");
                for (int j = 1; j <= N[k]; j++) {
                    fo.write("" + AX[j] + "   ");
                }
                fo.write('\n');
                fo.close();
            }
        }
    }
}