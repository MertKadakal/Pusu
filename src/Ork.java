import java.util.ArrayList;

public class Ork extends Types {
    
    public Ork(String id, String posx, String posy) {
        this.id = id;
        this.max_move = Constants.orkMaxMove;
        this.hp = Constants.orkHP;
        this.ap = Constants.orkAP;
        this.pos[0] = Integer.parseInt(posx);
        this.pos[1] = Integer.parseInt(posy);
    }

    public boolean check_exceptions(String move) {
        ArrayList<Integer> moves_x = new ArrayList<>();
        ArrayList<Integer> moves_y = new ArrayList<>();
        for (int i = 0; i < move.split(";").length; i++) {
            if (i%2 == 0) {
                moves_x.add(Integer.parseInt(move.split(";")[i]));
            }
            else {
                moves_y.add(Integer.parseInt(move.split(";")[i]));
            }
        }
        int sum_x = 0;
        int sum_y = 0;
        for (Integer num : moves_x) {
            sum_x += num;
        }
        for (Integer num : moves_y) {
            sum_y += num;
        }

        if (move.split(";").length != this.max_move*2) {
            FileOutput.writeToFile(Main.output_path, "Error : Move sequence contains wrong number of move steps. Input line ignored.\n", false, false);
            return false;
        }
        else if ((pos[0] + sum_x < 0) || (pos[0] + sum_x > Main.board_size) || (pos[1] + sum_y < 0) || (pos[1] + sum_y > Main.board_size)) {
            FileOutput.writeToFile(Main.output_path, "Error : Game board boundaries are exceeded. Input line ignored.\n", false, false);
            return false;
        }

        return true;
    }

    public ArrayList<Types> all_types_around() {
        ArrayList<Types> types = new ArrayList<>();
        types.add(check_if_the_cell_is_filled(0, 1));
        types.add(check_if_the_cell_is_filled(0, -1));
        types.add(check_if_the_cell_is_filled(1, -1));
        types.add(check_if_the_cell_is_filled(1, 0));
        types.add(check_if_the_cell_is_filled(1, 1));
        types.add(check_if_the_cell_is_filled(-1, -1));
        types.add(check_if_the_cell_is_filled(-1, 0));
        types.add(check_if_the_cell_is_filled(-1, 1));
        return types;
    }

    public Types check_if_the_cell_is_filled(int x, int y) {
        if (Main.filled_cells.containsValue(new int[]{pos[0]+x, pos[1]+y})) {
            for (Types t : Main.caliance_types) {
                if (t.pos[0] == pos[0]+x && t.pos[1] == pos[1]+y) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    void move(String move) {
        if (check_exceptions(move)) {
            String[] moves = move.split(";");
            for (int i = 0; i < moves.length/2; i += 2) {
                //filled_cells'te kendine ait yeri sil
                Main.filled_cells.remove(this.id);
                //hareketi gerçekleştir
                pos[0] += Integer.parseInt(moves[i]);
                pos[1] += Integer.parseInt(moves[i+1]);
                Main.filled_cells.put(this.id, pos);

                ArrayList<Types> around_types = all_types_around(); //8 komşudaki typeları çek
                //komşulardaki dostlara +10 hp ekle
                for (Types t : around_types) {
                    if (t != null) {
                        if ((t.id.charAt(0) == 'O') || (t.id.charAt(0) == 'T') || (t.id.charAt(0) == 'G')) {
                            t.hp += 10;
                            if (t.getClass().getName().equals("Ork") && t.hp > Constants.orkHP) {
                                t.hp = Constants.orkHP;
                            }
                            if (t.getClass().getName().equals("Troll") && t.hp > Constants.trollHP) {
                                t.hp = Constants.trollHP;
                            }
                            if (t.getClass().getName().equals("Goblin") && t.hp > Constants.goblinHP) {
                                t.hp = Constants.goblinHP;
                            }
                        }
                    }
                }
                this.hp += 10;
                if (this.hp > Constants.orkHP) {
                    this.hp = Constants.orkHP;
                }
                
                //fight to death kontrolü
                boolean check = false;
                Types defender = null;
                for (int[] pos : Main.filled_cells.values()) {
                    if (pos[0] == this.pos[0] && pos[0] == this.pos[0]) {
                        check = true;
                        for (Types t : Main.caliance_types) {
                            if (t.pos.equals(pos)) {
                                defender = t;
                            }
                        }
                        break;
                    }
                }
                if (check) { //true ise
                    //rakibin hp'sini düşür
                    defender.hp -= this.ap;
                    if (defender.hp > 0) { //rakibin hp'si pozitifse bunu ork'unkinden düş, ardından rakibi yok et
                        this.hp -= defender.hp;
                    }
                    Main.filled_cells.remove(defender.id);
                    Main.caliance_types.remove(defender);
                } else { //false ise
                    //8 yön için de saldırı
                    for (Types t : around_types) {
                        if (t != null && ((t.id.charAt(0) == 'H') || (t.id.charAt(0) == 'E') || (t.id.charAt(0) == 'D'))) {
                            t.hp -= this.ap;
                            if (t.hp < 0) {
                                Main.filled_cells.remove(t.id);
                                Main.caliance_types.remove(t);
                            }
                        }
                    }
                }
            }
        }
    }
}