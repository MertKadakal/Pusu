import java.util.ArrayList;
import java.util.Arrays;

public class Goblin extends Types {
    
    public Goblin(String id, String posx, String posy) {
        this.id = id;
        this.max_move = Constants.goblinMaxMove;
        this.hp = Constants.goblinHP;
        this.def_hp = Constants.goblinHP;
        this.ap = Constants.goblinAP;
        this.pos[0] = Integer.parseInt(posx);
        this.pos[1] = Integer.parseInt(posy);
    }

    public boolean check_exceptions(String move) {
        ArrayList<Integer> moves_x = new ArrayList<>();
        ArrayList<Integer> moves_y = new ArrayList<>();
        for (int i = 0; i < move.split(";").length; i++) {
            if (i % 2 == 0) {
                moves_x.add(Integer.parseInt(move.split(";")[i]));
            } else {
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

        if (move.split(";").length != this.max_move * 2) {
            FileOutput.writeToFile(Main.output_path, "Error : Move sequence contains wrong number of move steps. Input line ignored.\n\n", true, false);
            return false;
        } else if ((pos[1] + sum_x < 0) || (pos[1] + sum_x > Main.board_size) || (pos[0] + sum_y < 0) || (pos[0] + sum_y > Main.board_size)) {
            FileOutput.writeToFile(Main.output_path, "Error : Game board boundaries are exceeded. Input line ignored.\n\n", true, false);
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
        int[] targetPos = {pos[0] + x, pos[1] + y};
        for (int[] filledPos : Main.filled_cells.values()) {
            if (Arrays.equals(filledPos, targetPos)) {
                for (Types t : Main.caliance_types) {
                    if (Arrays.equals(t.pos, targetPos)) {
                        return t;
                    }
                }
                for (Types t : Main.zorde_types) {
                    if (Arrays.equals(t.pos, targetPos)) {
                        return t;
                    }
                }
            }
        }
        return null;
    }

    @Override
    boolean move(String move) {
        if (check_exceptions(move)) {
            String[] moves = move.split(";");
            for (int i = 0; i < moves.length; i += 2) {

                ArrayList<Types> around_types = all_types_around(); // 8 komşudaki typeları çek
                // hareketi gerçekleştir
                pos[1] += Integer.parseInt(moves[i]);
                pos[0] += Integer.parseInt(moves[i + 1]);
                Main.filled_cells.put(this.id, pos);

                // fight to death kontrolü
                Types defender = null;
                for (String id : Main.filled_cells.keySet()) {
                    if (Arrays.equals(Main.filled_cells.get(id), this.pos) && !id.equals(this.id)) {
                        for (Types t : Main.caliance_types) {
                            if (Arrays.equals(t.pos, this.pos)) {
                                defender = t;
                                break;
                            }
                        }
                        break;
                    }
                }

                if (defender != null) { // true ise
                    // rakibin hp'sini düşür
                    defender.hp -= this.ap;

                    if (defender.hp > this.hp) { // rakibin hp'si daha büyükse sen ölürsün
                        Main.filled_cells.remove(this.id);
                        Main.zorde_types.remove(this);
                    } else if (defender.hp == this.hp) { // hp'ler eşitse ikiniz de ölürsünüz
                        Main.filled_cells.remove(this.id);
                        Main.zorde_types.remove(this);
                        Main.filled_cells.remove(defender.id);
                        Main.caliance_types.remove(defender);
                    } else { // senin hp'n daha büyükse o ölür
                        Main.filled_cells.remove(defender.id);
                        Main.caliance_types.remove(defender);
                    }
                    break;

                } else { // false ise
                    // 8 yön için de saldırı
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
            return true;
        }
        return false;
    }
}
