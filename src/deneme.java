import java.util.ArrayList;
import java.util.List;

public class deneme {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> list1 = new ArrayList<>();
        list1.add(new ArrayList<>(List.of(1,2)));
        list1.add(new ArrayList<>(List.of(2,2)));
        list1.add(new ArrayList<>(List.of(3,2)));
        list1.add(new ArrayList<>(List.of(4,2)));
        System.out.println(list1);

        ArrayList<ArrayList<Integer>> list2 = new ArrayList<>();
        list2.add(new ArrayList<>(List.of(1,2)));
        list2.add(new ArrayList<>(List.of(3,2)));
        list2.add(new ArrayList<>(List.of(4,2)));
        list2.add(new ArrayList<>(List.of(2,2)));
        System.out.println(list2);

        System.out.println(list1.equals(list2));
    }
}
