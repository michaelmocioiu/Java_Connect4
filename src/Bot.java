import java.util.Random;
public class Bot {
    public static Random rand = new Random();
    public Bot() {

    }

    public static int Move(Board board) {
        return rand.nextInt(0, 6);
    }
}
