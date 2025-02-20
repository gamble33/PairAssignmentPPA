public class Main {
    public static void main(String[] args) {
        new Main().runSimulation();
    }
    
    public void runSimulation() {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();        
    }
}