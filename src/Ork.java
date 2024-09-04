import java.util.ArrayList;
import java.util.Arrays;

public class Ork extends Types {
    int count=0;
    
    public Ork(String id, String posx, String posy) {
        this.id = id;
        this.max_move = Constants.orkMaxMove;
        this.hp = Constants.orkHP;
        this.def_hp = Constants.orkHP;
        this.ap = Constants.orkAP;
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
        } else if ((pos[1] + sum_x < 0) || (pos[1] + sum_x > Main.board_size-1) || (pos[0] + sum_y < 0) || (pos[0] + sum_y > Main.board_size-1)) {
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
        if (containsValueInFilledCells(targetPos)) {
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
        return null;
    }

    public boolean containsValueInFilledCells(int[] target) {
        for (int[] value : Main.filled_cells.values()) {
            if (Arrays.equals(value, target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    String move(String move) {
        exceeded = false;
        count++;
        ArrayList<Types> around_types = all_types_around(); // 8 komşudaki typeları çek

        // komşulardaki dostlara +10 hp ekle
        for (Types t : around_types) {
            if (t != null && ((t.id.charAt(0) == 'O') || (t.id.charAt(0) == 'T') || (t.id.charAt(0) == 'G'))) {
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
        this.hp += 10;
        if (this.hp > Constants.orkHP) {
            this.hp = Constants.orkHP;
        }

        if (check_exceptions(move)) {
            String[] moves = move.split(";");
            for (int i = 0; i < moves.length; i += 2) {

                // hedef hücrede dost varmı kontrol et: yoksa hareketi gerçekleştir, varsa dur
                int[] target = {pos[1] + Integer.parseInt(moves[i]), pos[0] + Integer.parseInt(moves[i + 1])};
                for (int[] value : Main.filled_cells.values()) {
                    if (Arrays.equals(value, target)) {
                        for (String id : Main.filled_cells.keySet()) {
                            if (Main.filled_cells.get(id).equals(target) && ((id.charAt(0) == 'O') || (id.charAt(0) == 'T') || (id.charAt(0) == 'G'))) {
                                break;
                            }
                        }
                    }
                }
                
                if ((pos[1] + Integer.parseInt(moves[i]) < 0) || (pos[1] + Integer.parseInt(moves[i]) > Main.board_size-1) || (pos[0] + Integer.parseInt(moves[i+1]) < 0) || (pos[0] + Integer.parseInt(moves[i+1]) > Main.board_size-1)) {
                    exceeded = true;
                    break;
                }

                pos[1] += Integer.parseInt(moves[i]);
                pos[0] += Integer.parseInt(moves[i + 1]);
                Main.filled_cells.put(this.id, pos);
                ArrayList<Types> around_types2 = all_types_around(); // 8 komşudaki typeları çek

                // Fight to death kontrolü
                boolean check = false;
                Types defender = null;
                int[] currentPosition = {this.pos[0], this.pos[1]};
                for (Types t : Main.caliance_types) {
                    System.out.println(t.id + " " + t.pos[1] + " " + t.pos[0] + "--" + currentPosition[1] + " " + currentPosition[0]);
                    if (Arrays.equals(t.pos, currentPosition)) {
                        check = true;
                        defender = t;
                        break;
                    }
                }
                if (check) { // True ise
                    if (defender != null) {
                        boolean anyone_died = false;
                        if (this.hp == defender.hp) { //ikinizin de hp'leriniz eşitse ikiniz de ölürsünüz
                            Main.filled_cells.remove(this.id);
                            Main.zorde_types.remove(this);
                            Main.filled_cells.remove(defender.id);
                            Main.caliance_types.remove(defender);
                        }
                        else {
                            defender.hp -= this.ap;
                            if (defender.hp <= 0) {
                                Main.filled_cells.remove(defender.id);
                                Main.caliance_types.remove(defender);
                                anyone_died = true;
                            }
                        }

                        if (!(anyone_died)) {
                            //hp'si daha az olan ölür
                            if (this.hp <= defender.hp) { //sen ölürsün
                                defender.hp -= this.hp;
                                Main.filled_cells.remove(this.id);
                                Main.zorde_types.remove(this);
                                if (defender.hp <= 0) {
                                    Main.filled_cells.remove(defender.id);
                                    Main.caliance_types.remove(defender);
                                }
                            }
                            else { //düşman ölür
                                this.hp -= defender.hp;
                                Main.filled_cells.remove(defender.id);
                                Main.caliance_types.remove(defender);
                                if (this.hp <= 0) {
                                    Main.filled_cells.remove(this.id);
                                    Main.zorde_types.remove(this);
                                }
                            }
                        }
                        
                        break;
                    }
                } else { // false ise
                    // 8 yön için de saldırı
                    for (Types t : around_types2) {
                        if (t != null && ((t.id.charAt(0) == 'H') || (t.id.charAt(0) == 'E') || (t.id.charAt(0) == 'D'))) {
                            t.hp -= this.ap;
                            if (t.hp < 0) {
                                Main.filled_cells.remove(t.id);
                                Main.zorde_types.remove(t);
                            }
                        } else {
                        }
                    }
                }
            }
            if (exceeded) {
                return "exceeded";
            }
            else {
                return "done";
            }
        }
        return "failed";
    }
}
