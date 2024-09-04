public abstract class Types {
    String id;
    int[] pos = {0, 0};
    int max_move;
    int hp;
    int ap;
    int def_hp;
    boolean exceeded = false;

    abstract String move(String move);
}
