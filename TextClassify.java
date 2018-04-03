import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

// bundled data type of a double[][] and an int[]
final class LabeledSamples {
    private final double[][] samples;
    private final int[] labels;
    
    // constructor takes in a double[][] and an int[]
    public LabeledSamples(double[][] x, int[] y) {
        this.samples = x;
        this.labels = y;
    }
    
    public double[][] getSamples() {
        return this.samples;
    }
    
    public int[] getLabels() {
        return this.labels;
    }
}

// convert BOW csv files into arrays and classify them with Perceptron
public class TextClassify {

    // helper function: returns double[], given a String[]
    private static double[] convStrings(String[] input) {
        int len = input.length;
        double[] converted = new double[len];
        for (int i = 0; i < len; i++) {
            converted[i] = Double.parseDouble(input[i]);
        }
        return converted;
    }
    
    // returns a 2D double array and 1d int array representing a csv file
    public static LabeledSamples csvToArray(String filename) {
        
        // 1D array of answers
        List<Integer> answers = new ArrayList<Integer>();
        // number of features, will be answers.length
        int N; 
        // 2D array of feature values
        List<double[]> rows;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            // first line or iteration
            String s = reader.readLine();
            System.out.println("Reading file " + filename + "...");
            String[] fr = s.split(";");
        
            // convert string array into double array
            double[] firstRow = convStrings(fr);
            N = firstRow.length - 1;
            double[] sample = new double[N];
            for (int i = 0; i < N; i++) {
                sample[i] = firstRow[i];
            }
            
            // update answers and feature values
            answers.add((int) firstRow[N]);
            rows = new ArrayList<double[]>();
            rows.add(sample);
        
            // parse the rest
            while ((s = reader.readLine()) != null) {
                String[] cr = s.split(";");
                double[] curRow = convStrings(cr);
                double[] curSample = new double[N];
                for (int i = 0; i < N; i++) {
                    curSample[i] = curRow[i];
                }
                answers.add((int) curRow[N]);
                rows.add(curSample);            
            }
            
            // clean up, convert, end
            reader.close();
            int M = answers.size();
            double[][] data = new double[M][0];
            rows.toArray(data);
            int[] answersArray = new int[M];
            for (int i = 0; i < M; i++) {
                answersArray[i] = answers.get(i);
            }
            LabeledSamples ls = new LabeledSamples(data, answersArray);
            System.out.println("Finished parsing " + filename);
            return ls;
        }
        // handle exceptions
        catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'!", filename);
            e.printStackTrace();
            return null;
        }
    }
    
    // returns a double between 0 and 1 of proportion correctly classified
    // "accuracy of p on input, given answers"
    public static double accuracy(Perceptron p, LabeledSamples ls) {
        double[][] input = ls.getSamples();
        int[] answers = ls.getLabels();
        int M = answers.length;
        int sum = 0; // number of samples correctly classified
        // check error on every row of input (every sample)
        for (int i = 0; i < M; i++) {
            int result = p.error(input[i], answers[i]);
            if (result == 0)
                sum++;
        }
        return sum / (double) M; // number right divided by total
    }
    
    // takes in two command line arguments as csv filenames
    // and creates a perceptron from the first csv
    // then calculates accuracy on the second csv
    public static void main(String[] args) {
        
        // process command line args as filenames and convert csv files
        String train_filename = args[0];
        String test_filename = args[1];
        LabeledSamples training = csvToArray(train_filename);
        LabeledSamples testing = csvToArray(test_filename);
        
        // extract training data and create classifier object
        double[][] samples = training.getSamples();
        int[] answers = training.getLabels();
        System.out.println("Creating Perceptron classifier with training data...");
        Perceptron A = new Perceptron(samples, answers);
        System.out.println("Done");
        
        // run classifier on testing data
        System.out.println("Running classifier on testing data...");
        double results = accuracy(A, testing);
        System.out.println("Done. Testing accuracy: " + (results * 100) + "%");
        System.out.println("Weights: ");
        A.printWeights();

    }




}