package Parts;

import java.io.*;

//The class which stress tests and benchmarks the system as well as records the results
public class Tester {

    //The amount of time from start to finish benchmarking
    private int time;

    //The amount of elapsed time
    private long elapsed;

    //The start time
    private long startTime;

    //The number of threads to use in testing
    private int Threads;

    //A boolean indicating if the tester is running
    private boolean on;

    //The thread to run testing on
    private Thread t;

    //The file to record information to
    private File file;

    //The highest recorded benchmark
    private int topBench;

    //The latest benchmark score
    private int latestBench;

    //Creates the tester and sets its variables
    public Tester() {
        time = 0;
        on = false;
        elapsed = 0;
        Threads = Runtime.getRuntime().availableProcessors()*2;
        latestBench = 0;

        String path = "./score.txt";
        file = new File(path);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Starts a stress test
    private void stress(int threads) {
        Threads = threads;
        System.out.println("testing");
        for(int i = 0; i < threads; i++) {
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int x = 0; x < 10000000; x++) {
                        String c = "Working Working Working".replaceAll("Work","Finish");
                        String y = "Working Working Working".replaceAll("Work","Finish");
                        String s = "Working Working Working".replaceAll("Work","Finish");
                        String l = "Working Working Working".replaceAll("Work","Finish");
                        String m = "Working Working Working".replaceAll("Work","Finish");
                        if(!on) {
                            break;
                        }
                    }
                }
            });
            t.start();
        }
    }

    //Acts as a toggle for benchmarks and stress tests
    public void toggleOn() {
        if(!on) {
            on = !on;
            if(Threads != 0) {
                stress(Threads);
            }
        }
        else {
            on = !on;
        }
    }

    //Starts a benchmark
    public int bench(int thread) throws InterruptedException, IOException {
        Thread[] threads = new Thread[thread];
        for(int i = 0; i < thread; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    startTime = System.currentTimeMillis();
                    for (int x = 0; x < 10000000; x++) {
                        String c = "Working Working Working".replaceAll("Work", "Finish");
                        String y = "Working Working Working".replaceAll("Work", "Finish");
                        String s = "Working Working Working".replaceAll("Work", "Finish");
                        String l = "Working Working Working".replaceAll("Work","Finish");
                    }
                    elapsed = System.currentTimeMillis() - startTime;
                    time += elapsed;
                }
            });
            threads[i].start();
        }
        for(int x = 0; x < thread; x++) {
            threads[x].join();
        }
        latestBench = (time*thread)/1000;
        save(latestBench);
        System.out.println(latestBench);
        return latestBench;
    }

    //Saves a benchmark score
    private void save(int score) throws IOException {
        if(score < read()) {
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                dos.flush();
                topBench = score;
                dos.writeInt(score);
            }
        }
        else {
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                dos.flush();
                dos.writeInt(read());
            }
        }
    }

    //Reads a benchmark score and saves it inside the program
    private int read() throws IOException{
        if (!new File("./Sys.txt").canRead()) {
            return 0;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream("./Sys.txt"))) {
            return dis.readInt();
        } catch (IOException ignored) {
            return 0;
        }
    }

    public int getTopBench() {
        return topBench;
    }

    public int getLatestBench() {
        return latestBench;
    }
}
