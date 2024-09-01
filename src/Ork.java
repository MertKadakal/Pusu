public class Ork extends Types {
    
    public Ork(String id, String posx, String posy) {
        this.id = id;
        this.max_move = Constants.orkMaxMove;
        this.hp = Constants.orkHP;
        this.ap = Constants.orkAP;
        this.pos[0] = Integer.parseInt(posx);
        this.pos[1] = Integer.parseInt(posy);
    }

    @Override
    void move(String move) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
}
