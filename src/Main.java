import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Main {
    static String output_path = "v2output.txt";
    static int board_size;
    static HashMap<String, int[]> filled_cells;
    static ArrayList<Types> caliance_types;
    static ArrayList<Types> zorde_types;

    /**
     * Hareket gerçeleştirildikten sonra tablo ve altına karakterlerin kalan ve default HP değerleri output dosyasına yazılır.
     */
    public static void write_table_on_output() {
        FileOutput.writeToFile(output_path, "**".repeat(Main.board_size +1) + "\n", true, false);
        for (int k = 0; k < board_size; k++) {
            FileOutput.writeToFile(output_path, "*", true, false);
            for (int j = 0; j < board_size; j++) {
                boolean isCellFilled = false;
                for (String id : filled_cells.keySet()) {
                    if (Arrays.equals(filled_cells.get(id), new int[]{k, j})) {
                        FileOutput.writeToFile(output_path, id, true, false);
                        isCellFilled = true;
                        break;
                    }
                }
                if (!isCellFilled) {
                    FileOutput.writeToFile(Main.output_path, "  ", true, false);
                }
            }
            FileOutput.writeToFile(Main.output_path, "*\n", true, false);
        }
        FileOutput.writeToFile(Main.output_path, "**".repeat(Main.board_size +1) + "\n\n", true, false);

        ArrayList<Types> all_types = new ArrayList<>();
        all_types.addAll(zorde_types);
        all_types.addAll(caliance_types);
        // 'combinedList' içindeki 'Types' nesnelerini 'id' değerine göre sıralama
        Collections.sort(all_types, new Comparator<Types>() {
            @Override
            public int compare(Types t1, Types t2) {
                return t1.id.compareTo(t2.id); // 'id' değerine göre karşılaştırma
            }
        });
        // Eleman ve hp'lerini yaz
        for (Types t : all_types) {
            String def_hp = "("+t.def_hp+")";
            FileOutput.writeToFile(Main.output_path, String.format("%s\t%s\t%s\n", t.id, t.hp, def_hp), true, false);
        }
        FileOutput.writeToFile(Main.output_path, "\n", true, false);
    }

    /**
     * Bu kod ilk çalıştırılır. Verilen dosya pathlerinden başlangıç değerleri ve komutlar elde edilir. Zorde ve Calliance türündeki karakterler
     * oluşturulurarak filled_cells mapine kaydedilir. Burada "Karakter ID'si: Karakterin konumu" şeklinde veri depolanır. Ayrıca karakterlerin
     * de tutulduğu iki tür için de iki ArrayList vardır. Bu 3 yapı oluşturulduktan sonra komut satırları sırayla okunarak işlemler yapılır.
     */
    public static void main(String[] args) throws IOException {
        FileOutput.writeToFile(output_path, "", false, false);
        String[] initials_txt = FileInput.readFile("IO_3\\initials.txt", false, true);
        String[] commands_txt = FileInput.readFile("IO_3\\commands.txt", false, true);
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

        // caliance elemanlarını oluşturma
        for (int i = 4; i < zorde_ind - 1; i++) {
            switch (initials_txt[i].split(" ")[0]) {
                case "ELF":
                    caliance_types.add(new Elf(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(caliance_types.get(caliance_types.size() - 1).id, caliance_types.get(caliance_types.size() - 1).pos);
                    break;
                case "HUMAN":
                    caliance_types.add(new Human(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(caliance_types.get(caliance_types.size() - 1).id, caliance_types.get(caliance_types.size() - 1).pos);
                    break;
                case "DWARF":
                    caliance_types.add(new Dwarf(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(caliance_types.get(caliance_types.size() - 1).id, caliance_types.get(caliance_types.size() - 1).pos);
                    break;
            }
        }

        // zorde elemanlarını oluşturma
        int i = zorde_ind + 1;
        while (i < initials_txt.length) {
            switch (initials_txt[i].split(" ")[0]) {
                case "ORK":
                    zorde_types.add(new Ork(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(zorde_types.get(zorde_types.size() - 1).id, zorde_types.get(zorde_types.size() - 1).pos);
                    break;
                case "TROLL":
                    zorde_types.add(new Troll(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(zorde_types.get(zorde_types.size() - 1).id, zorde_types.get(zorde_types.size() - 1).pos);
                    break;
                case "GOBLIN":
                    zorde_types.add(new Goblin(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[3], initials_txt[i].split(" ")[2]));
                    filled_cells.put(zorde_types.get(zorde_types.size() - 1).id, zorde_types.get(zorde_types.size() - 1).pos);
                    break;
            }
            i++;
        }

        write_table_on_output();

        //commands.txt'i oku ve işle
        for (String line : commands_txt) {
            while (true) {
                int ind = 0;
                
                //for types of caliance
                for (Types type : caliance_types) {
                    if (type.id.equals(line.split(" ")[0])) {
                        String move_result = caliance_types.get(ind).move(line.split(" ")[1]);
                        switch (move_result) {
                            case "done":
                                write_table_on_output();
                                break;
                            case "exceeded":
                                write_table_on_output();
                                FileOutput.writeToFile(output_path, "Error : Game board boundaries are exceeded. Input line ignored.\n\n", true, false);
                                break;
                        }
                        break;
                    }
                    ind++;
                }
                    
                //for types of zorde
                ind = 0;
                for (Types type : zorde_types) {
                    if (type.id.equals(line.split(" ")[0])) {
                        String move_result = zorde_types.get(ind).move(line.split(" ")[1]);
                        switch (move_result) {
                            case "done":
                                write_table_on_output();
                                break;
                            case "exceeded":
                                write_table_on_output();
                                FileOutput.writeToFile(output_path, "Error : Game board boundaries are exceeded. Input line ignored.\n\n", true, false);
                                break;
                        }
                        break;
                    }
                    ind++;
                }
                break;
            }

            //oyun bitti mi kontrol et
            if (zorde_types.size() == 0) {
                FileOutput.writeToFile(Main.output_path, "\nGame Finished\nCalliance Wins", true, false);
                break;
            }
            if (caliance_types.size() == 0) {
                FileOutput.writeToFile(Main.output_path, "\nGame Finished\nZorde Wins", true, false);
                break;
            }
        }
    }
}