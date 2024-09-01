import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static String output_path = "src\\output.txt";
    static int board_size;
    static HashMap<String, int[]> filled_cells;
    static ArrayList<Types> caliance_types;
    static ArrayList<Types> zorde_types;

    public static void main(String[] args) {
        String[] initials_txt = FileInput.readFile("IO_1\\initials.txt", false, true);
        String[] commands_txt = FileInput.readFile("IO_1\\commands.txt", false, true);
        board_size = Integer.parseInt(initials_txt[1].split("x")[0]);
        caliance_types = new ArrayList<>();
        zorde_types = new ArrayList<>();
        filled_cells = new HashMap<>();
        
        //caliance ve zorde'nin hangi satırda olduğunun tespiti
        int zorde_ind = 0;
        int line_ind = 0;
        for (String line : initials_txt) {
            if (line.equals("ZORDE")) {
                zorde_ind = line_ind;
            }
            line_ind++;
        }

        //caliance elemanlarını oluşturma
        for (int i = 4; i<zorde_ind-1; i++) {
            switch (initials_txt[i].split(" ")[0]) {
                case "ELF":
                    caliance_types.add(new Elf(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(caliance_types.get(caliance_types.size()-1).id, caliance_types.get(caliance_types.size()-1).pos);
                    break;
                case "HUMAN":
                    caliance_types.add(new Human(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(caliance_types.get(caliance_types.size()-1).id, caliance_types.get(caliance_types.size()-1).pos);
                    break;
                case "DWARF":
                    caliance_types.add(new Dwarf(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(caliance_types.get(caliance_types.size()-1).id, caliance_types.get(caliance_types.size()-1).pos);
                    break;
            }
        }
        //zorde elemanlarını oluşturma
        int i = zorde_ind+1;
        while (i<initials_txt.length) {
            switch (initials_txt[i].split(" ")[0]) {
                case "ORK":
                    zorde_types.add(new Ork(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(zorde_types.get(zorde_types.size()-1).id, zorde_types.get(zorde_types.size()-1).pos);
                    break;
                case "TROLL":
                    zorde_types.add(new Troll(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(zorde_types.get(zorde_types.size()-1).id, zorde_types.get(zorde_types.size()-1).pos);
                    break;
                case "GOBLIN":
                    zorde_types.add(new Goblin(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    filled_cells.put(zorde_types.get(zorde_types.size()-1).id, zorde_types.get(zorde_types.size()-1).pos);
                    break;
            }
            i++;
        }

        for (Types t : zorde_types) {
            if (t.getClass().getName().equals("Ork")) {
                t.move("0;-1;0;-1;");
            }
        }
        /*
        //commands.txt'i oku ve işle
        for (String line : commands_txt) {
            while (true) {
                
                //for types of caliance
                int ind = 0;
                for (Types type : caliance_types) {
                    if (type.id.equals(line.split(" ")[0])) {
                        caliance_types.get(ind).move(line.split(" ")[1]);
                        break;
                    }
                    ind++;
                }
                //for types of zorde
                ind = 0;
                for (Types type : zorde_types) {
                    if (type.id.equals(line.split(" ")[0])) {
                        zorde_types.get(ind).move(line.split(" ")[1]);
                        break;
                    }
                    ind++;
                }
            }
                
        }
        */
    }
}