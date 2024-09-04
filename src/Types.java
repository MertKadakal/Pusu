public abstract class Types {
    String id;
    int[] pos = {0, 0};
    int max_move;
    int hp;
    int ap;
    int def_hp;
    boolean exceeded = false;

    /**
     * Bu karakter oynandığında çalıştırılan fonksiyondur.
     * @param move Karakterin hareket komutları
     * @return <ul>
     * <li><code>"Exceeded"</code> = Tablonun dışına çıkmaya sebep olacak hamle olduğu için hareket durduruldu
     * <li><code>"Done"</code> = Hareket başarıyla tamamlandı
     * <li><code>"Failed"</code> = Kontrol edildi ve komutun uygun olmadığı tespit edildi
     * </ul>
     */
    abstract String move(String move);
}
