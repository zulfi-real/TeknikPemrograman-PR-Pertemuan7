class Account {
    int balance = 150;
}

public class TransferFulus {
    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account();
        Account acc2 = new Account();

        // Thread 1: Menjumlahkan/ transfer fulus dari acc1 ke acc2
        Thread t1 = new Thread(() -> {
            synchronized (acc1) { // synchronized() mengunci acc1 pada thread t1
                
                System.out.println(Thread.currentThread().getName() +": Sychronizing (mengunci) thread ke acc1");
                try { 
                    Thread.sleep(100); 
                } catch (Exception e) {} // Simulasi dengan memberikan jeda. Mengapa diperlukan Exception?
                //Exception diperlukan karena method .sleep() melempar error InterruptedException jika diinterupsi oleh thread lain saat didelay (sleep)

                synchronized (acc2) { // synchronized() mengunci acc2 pada thread t1
                    System.out.println(Thread.currentThread().getName() +": Sychronizing (mengunci) thread ke acc2");
                    acc2.balance += acc1.balance; //var balance pada acc2 ditambahkan oleh var balancce dari acc1
                }
                System.out.println(Thread.currentThread().getName() + " selesai, membuka acc1 dan acc2");
                System.out.flush(); //digunakan sehingga pesan yang ada langsung dikeluarkan terlebih dahulu sebelum keluar dari method
            } 
        });

        // Thread 2: Menjumlahkan/ transfer fulus dari acc2 ke acc1
        Thread t2 = new Thread(() -> {
            synchronized (acc1) { // synchronized() mengunci acc1 pada thread t2.
            // Thread 2 akan mencoba untuk menggunakan acc1, tetapi di lock pada thread 1 sehingga menunggu thread 1 selesai terlebih dahulu
                System.out.println(Thread.currentThread().getName() +": Sychronizing (mengunci) thread ke acc1");
                try { 
                    Thread.sleep(100); 
                } catch (Exception e) {}

                synchronized (acc2) { // synchronized() mengunci acc2 pada thread t2
                    System.out.println(Thread.currentThread().getName() +": Sychronizing (mengunci) thread ke acc2");
                    acc1.balance += acc2.balance;
                }
            }
        });

        //Memulai thread t1 dan t2, karena t1 diawal, t1 akan terlebih dahulu digunakan
        t1.start();
        t2.start();

        //
        t1.join();
        t2.join();

        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Saldo Akhir acc1: " + acc1.balance);
		System.out.println("Saldo Akhir acc2: " + acc2.balance);
    }
}
