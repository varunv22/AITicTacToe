import java.awt.*;
import java.lang.Thread;
import java.lang.Runnable;
import java.awt.image.*;

public class Main extends Canvas implements Runnable{ // allows game to be smooth and update frequently
    private Frame frame;
    private Board board;
    private boolean running;
    private Thread gameThread;
    
    public Main() {
        running = false;
        frame = new Frame(this);
        board = new Board();
         
    }

    public synchronized void start() {
        if(running) {
            return;
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void end() {

        if(!running) {
            return;
        }
        running = false;
        try {
            gameThread.join();
        } catch(InterruptedException x) {
            x.printStackTrace();
        }
    }
    public void run() {
        long lastTime = System.nanoTime();
        double num_updates = 60.0; // 60 fps
        double ns = 1000000000 / num_updates;
        double catchup = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long curr_time = System.nanoTime();
            catchup += (curr_time - lastTime) / ns;
            lastTime = curr_time;
            while (catchup >= 1) {
                update_board();
                catchup--;
            }
            init();
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        end_game();
    }

    public void update_board() {
        board.update();
    }

    public synchronized void end_game() {
        if (!running)
            return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    public void init() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics grp = bs.getDrawGraphics();
        grp.setColor(new Color(46, 64, 83));
        grp.fillRect(0, 0, Frame.WIDTH, Frame.HEIGHT);
        bs.show();
        grp.dispose();
    }

    public static void main(String[] args) {
        new Main();
    }
}
