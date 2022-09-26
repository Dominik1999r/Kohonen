package kohonen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kohonen {
    
    private List<Point> listOfWeights = new ArrayList<>();
    private List<Point> examples;
    private int numberOfNeurons;
    private int iter;
    private static final int LAMBDA = 4;
    Random rand = new Random();
	
    public List<Point> getListOfWeights() {
	return listOfWeights;
    }

    public void setListOfWeights(List<Point> listOfWeights) {
	this.listOfWeights = listOfWeights;
    }

    public Kohonen(List<Point> examples, int iter, int quantityOfNeurons) {
	this.examples = examples;
	this.iter = iter;
	this.numberOfNeurons = quantityOfNeurons;
	weightsForNeurons();
    }
	
    public void weightsForNeurons() {  
	for(int i = 0; i < numberOfNeurons; i++) {
            Point point = new Point(rand.nextInt(550)+100, rand.nextInt(550)+100);
            listOfWeights.add(point);
        }
    }
	
    public int searchWinnerUnit(Point point) {
	int unit = 0;
	double min = Double.MAX_VALUE;
        double dis = 0;
	
        for (int i = 0; i < listOfWeights.size(); i++) {
            dis = Math.sqrt(Math.pow(point.x - listOfWeights.get(i).x, 2) 
                            + Math.pow(point.y - listOfWeights.get(i).y, 2));
            
            if(dis < min) {
            	unit = i;
                min = dis;
            }
        }
        
        return unit;
    }
	
    public double functionOfGauss(int w, int v) {
	double p = Math.abs(w - v);
	double result = Math.exp((-1) * Math.pow(p, 2)/(2 * Math.pow(LAMBDA, 2)));
        
	return result;
    }
	
    public void changeUnitAndNeighbors(Point point, int unit, double t) {
	double alfa = 1 - ((t-1)/iter);
	System.out.println(alfa);
        
	double px = listOfWeights.get(unit).x + alfa * functionOfGauss(unit, unit) 
	* (point.x - listOfWeights.get(unit).x);
	double py = listOfWeights.get(unit).y + alfa * functionOfGauss(unit, unit) 
	* (point.y - listOfWeights.get(unit).y);
	
        listOfWeights.set(unit, new Point(px, py));
		
	for (int i = 1; i < LAMBDA; i++) {
            if((unit - i) >= 0) {
		px = listOfWeights.get(unit - i).x + alfa * functionOfGauss(unit - i, unit) 
		* (point.x - listOfWeights.get(unit - i).x);
		py = listOfWeights.get(unit - i).y + alfa * functionOfGauss(unit - i, unit) 
		* (point.y - listOfWeights.get(unit - i).y);
		listOfWeights.set(unit - i, new Point(px, py));
            }
	}
		
	int count = listOfWeights.size();
	for (int i = 1; i < LAMBDA; i++) {
            if((unit + i) < count) {
		px = listOfWeights.get(unit + i).x + alfa * functionOfGauss(unit + i, unit) 
		*(point.x - listOfWeights.get(unit + i).x);
		py = listOfWeights.get(unit + i).y + alfa * functionOfGauss(unit + i, unit) 
		* (point.y - listOfWeights.get(unit + i).y);
		listOfWeights.set(unit + i, new Point(px, py));
            }
	}
    }
	
	
    public void learning(int t) {
        int exampleSize = examples.size();
        if(exampleSize != 0) {
            int randomExample = rand.nextInt(exampleSize);   
            Point point = examples.get(randomExample);
            
            int unit = searchWinnerUnit(point);
            changeUnitAndNeighbors(point, unit, t);
        }
    }	
}