public abstract class Types {
    String id;
    int[] pos = {0, 0};
    int max_move;
    int hp;
    int ap;

    abstract void move(String move);
}
