package base;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Philipp Imperatori, Nils Nedderhut, Louis Neumann
 * modified by Roman Hergenreder
 * <p>
 * Version 1.0
 */
public class PerlinNoise {

    private int width ;
    private int height;
    private int scale;

    private int gwidth; // scaled width
    private int gheight; // scaled height

    private Random random;
    private ArrayList<Vector<Double>> Vectors; //gradients

    public PerlinNoise(int width, int height, int scale) {

        this.width = width * scale;
        this.height = height * scale;
        this.scale = scale;
        this.gwidth = width;
        this.gheight = height;

        this.random = new Random();
        this.Vectors = new ArrayList<>();
        createVectors((1 + gwidth) * (1 + gheight));
    }

    public Dimension getRealSize() {
        return new Dimension(this.width, this.height);
    }

    public Dimension getScaledSize() {
        return new Dimension(this.gwidth, this.gheight);
    }

    /**
     * create's a list with n elements where each element is a vector in the union circle (gradient)
     * @param n: number of gradients to be created
     */
    private void createVectors(int n) {
        this.Vectors = new ArrayList<>();
        for(int i=0;i<n;i++){
            double randomValue = random.nextDouble();
            randomValue = randomValue * 2 * Math.PI;
            double x = Math.cos(randomValue);
            double y = Math.sin(randomValue);
            Vector<Double> grad = new Vector<>(x,y);
            this.Vectors.add(grad);
        }
    }

    /**
     * Uses a Sigmoid function to smooth numbers betwenn 0 to 1
     * @param t: number to smooth
     * @return smoothed number
     */
    private double fade(double t){
        return (((6*(t*t*t*t*t))-(15*t*t*t*t))+(10*t*t*t));
    }

    /** Computes a weighted linear interpolation with point (x,y) and weight w
     * @param x: value of x-coordinate
     * @param y: value of y-coordinate
     * @param w: weight w to interpolate
     * @return result of linear interpolation
     */
    private double linearInterpolation(double x, double y, double w){
        return ((1.0-w)*x+w*y);
    }

    /**
     * Calculates the scalar product between the direction vector and the gradient in the corner
     * @param vector: direction vector
     * @param gradient: gradient in the corner
     * @return scalar product
     */
    private double scalarVekGrad(Vector<Double> vector, Vector<Double> gradient){
        return (vector.getX()*gradient.getX() + vector.getY()*gradient.getY());
    }

    /**
     * Converts the value from its old interval [oldMin,oldMax] to a new interval [newMin,newMax]
     * @param value : value to be converted
     * @return converted value
     */
    private double mapToInterval(double value) {
        return (value + 1.0)/2.0;
    }


    /**
     * Creates the noise value for the given point (x,y)
     * @param x: x-coordinate of point
     * @param y: y-coordinate of point
     * @return noise of point
     */
    public double getNoise (double x, double y) {
        if (x >= width || y >= height) {
            System.out.println("ERROR: x or/and y is not in picture");
            return 0;
        }

        double scaledX = x / this.scale;
        double scaledY = y / this.scale;

        //Left upper edge
        int xlo = (int) scaledX;
        int ylo = (int) scaledY;

        //right upper egde
        int xro = ((int) scaledX)+1;
        int yro = ((int) scaledY);

        //left lower edge
        int xlu = (int) scaledX;
        int ylu = ((int) scaledY)+1;

        //right lower edge
        int xru = ((int) scaledX)+1;
        int yru = ((int) scaledY)+1;

        // int cell_nr = xlo+(ylo*this.gwidth);

        Vector<Double> gradLO = this.Vectors.get(xlo+(ylo*(this.gwidth+1)));
        Vector<Double> gradRO = this.Vectors.get(xro+(yro*(this.gwidth+1)));
        Vector<Double> gradLU = this.Vectors.get(xlu+((ylu)*(this.gwidth+1)));
        Vector<Double> gradRU = this.Vectors.get(xru+((yru)*(this.gwidth+1)));

        Vector<Double> rvLO = new Vector<>(scaledX - xlo,scaledY - ylo);
        Vector<Double> rvRO = new Vector<>(scaledX - xro,scaledY - yro);
        Vector<Double> rvLU = new Vector<>(scaledX - xlu,scaledY - ylu);
        Vector<Double> rvRU = new Vector<>(scaledX - xru,scaledY - yru);

        //upper edges
        double linIntOben = this.linearInterpolation(this.scalarVekGrad(rvLO,gradLO),this.scalarVekGrad(rvRO,gradRO),this.fade(rvLO.getX()));

        //lower edges
        double linIntUnten = this.linearInterpolation(this.scalarVekGrad(rvLU,gradLU),this.scalarVekGrad(rvRU,gradRU),this.fade(rvLO.getX()));

        //final interpolation
        return this.mapToInterval(this.linearInterpolation(linIntOben,linIntUnten,this.fade(rvLO.getY())));
    }
}
