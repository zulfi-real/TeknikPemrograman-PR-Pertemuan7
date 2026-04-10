class Resto {
    private int chickenStock = 80;

    public synchronized void serveCustomer(String cashierName) {
        //Penambahan keyword sychronized sehingga hanya 1 thread yang dapat melakukan proses, 
        if (chickenStock > 0) {
            //thread 'tidur' dan dijeda, mengakibatkan thread lain dapat mengakses proses dan mengecek variable dan lalu 'tidur'. 
            //Ini hanya terjadi jika tidak menggunakan synchronized, karena synchronized akan mengunci thread, sehingga ini hanya menjeda proses 1 thread
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            
            chickenStock--; 
            System.out.println(cashierName + " berhasil menjual 1 ayam. Sisa stok: " + chickenStock);
        } else {
            System.out.println(cashierName + " gagal: Stok Habis!");
        }
    }

    public int getRemainingStock() {
        return chickenStock;
    }
}

public class RestoSimulasi {
    public static void main(String[] args) throws InterruptedException {
        Resto ayamJuicyLuicyGallagher = new Resto();

        Runnable task = () -> {
            for (int i = 0; i < 30; i++) {
                ayamJuicyLuicyGallagher.serveCustomer(Thread.currentThread().getName());
            }
        };

        Thread kasir1 = new Thread(task, "Kasir-A");
        Thread kasir2 = new Thread(task, "Kasir-B");
        Thread kasir3 = new Thread(task, "Kasir-C");

        kasir1.start();
        kasir2.start();
        kasir3.start();

        kasir1.join();
        kasir2.join();
        kasir3.join();

        System.out.println("--- HASIL AKHIR STOK: " + ayamJuicyLuicyGallagher.getRemainingStock() + " ---");
    }
}
