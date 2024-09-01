import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] initials_txt = FileInput.readFile("IO_1\\initials.txt", false, true);
        String[] commands_txt = FileInput.readFile("IO_1\\commands.txt", false, true);
        String output_path = "src\\output.txt";
        int board_size;
        ArrayList<Types> caliance_types = new ArrayList<>();
        ArrayList<Types> zorde_types = new ArrayList<>();
        
        //caliance ve zorde'nin hangi satırda olduğunun tespiti
        int caliance_ind = 3;
        int zorde_ind = 0;
        int line_ind = 0;
        for (String line : initials_txt) {
            if (line_ind == 1) {
                board_size = Integer.parseInt(line.split("x")[0]);
            }
            if (line.equals("ZORDE")) {
                zorde_ind = line_ind;
            }
            line_ind++;
        }

        //caliance elemanlarını oluşturma
        for (int i = 4; i<zorde_ind-1; i++) {
            System.out.println(initials_txt[i]);
        }
        //zorde elemanlarını oluşturma
        int i = zorde_ind+1;
        while (i<initials_txt.length) {
            switch (initials_txt[i].split(" ")[0]) {
                case "ORK":
                    zorde_types.add(new Ork(initials_txt[i].split(" ")[1], initials_txt[i].split(" ")[2], initials_txt[i].split(" ")[3]));
                    break;
            }
            i++;
        }
    }
}