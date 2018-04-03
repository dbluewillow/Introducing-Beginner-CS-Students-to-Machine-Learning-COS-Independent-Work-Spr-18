public class Perceptron {
    
    // array of words and their predictive weights (initialize to 0.0)
    private double[] weights;
    // bias term (explain this shit)
    private double b;
    // correct outputs
    private int[] answers;
    // number of words -- should match weights.length
    private int N;
    // number of samples examined by this particular Perceptron
    // -- should match answers.length
    private int M;
    
    // fractional threshold between 0 and 1 for affirmative classification
    private final double T = 0.3;
    // fractional multiplier on how hard the algorithm corrects itself
    private final double rate = 0.2;
    // max possible number of updates before algorithm stops
    private final int limit = 200;
    
    // alternate constructor for debugging
    // instantiate given weights, no training
    public Perceptron(double[] w) {
        this.answers = null;
        this.N = w.length;
        this.M = 0;
        this.b = 0;
        this.weights = new double[N];
    }

    // instantiate a classifier object and train it
    // good opportunity to demonstrate use of the "this" keyword
    // can change arguments into command line / StdIn args 
    public Perceptron(double[][] inputs, int[] answers) {
        this.answers = answers;
        this.N = inputs[0].length;
        this.M = answers.length;
        this.b = 0;
        this.weights = new double[N];
        // train classifier on inputs
        // passing inputs as an arg is bad because its HUGE
        // so try to change
        for (int i = 0; i < limit; i++) {
            if (update(inputs) == 0)
                break;
        }
    }
    
    // return number of examples looked at by this Perceptron
    public int size() {
        return M;
    }
    
    // returns number of words being considered (weights)
    public int words() {
        return N;
    }
    
    // prints the weights to the console
    public void printWeights() {
        for (int i = 0; i < N; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(weights[i]);
        }
        System.out.println();
    }
    
    // updates weights and returns total error
    public int update(double[][] inputs) {
        int totalError = 0;
        for (int i = 0; i < M; i++) {
            int sampleError = error(inputs[i], answers[i]);
            totalError += java.lang.Math.abs(sampleError);
            double change = rate * sampleError;
            b += change;
            // every feature's weight updates
            for (int j = 0; j < this.N; j++){
                weights[j] += change * inputs[i][j];
            }
        }
        return totalError;
    }
    
    // return the current error rate on iteration j of the input text
    public int error(double[] sample, int answer) {
        int result = classify(sample);
        return answer - result;
    }
    
    // first thing students write: assume a working, trained perceptron
    // return 1 if sum weights times inputs > threshold, 0 if not
    // most basic function of the perceptron
    public int classify(double[] sample) {
        double total = 0;
        int len = sample.length;
        for (int i = 0; i < len; i++) {
            total += sample[i] * weights[i];
        }
        total += b;
        if (total > T)
            return 1;
        else
            return 0;
    }
    
    // testing method
    public static void main(String[] args) {
        double inputs[][] = {{0,0},{0,1},{1,0},{1,1}};
        int outputs[] = {1,0,1,0};
        Perceptron A = new Perceptron(inputs, outputs);
        System.out.println(A.size());
        System.out.println(A.words());
        A.printWeights();
        System.out.println(A.classify(new double[]{0,0}));
        System.out.println(A.classify(new double[]{0,1}));
        System.out.println(A.classify(new double[]{1,0}));
        System.out.println(A.classify(new double[]{1,1}));     
    }
}