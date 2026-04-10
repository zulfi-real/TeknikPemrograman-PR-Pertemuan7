import java.util.Scanner;
     
class Processor implements Runnable {
    private long start, end, partialSum;    
    private int threadId;

    //constructor untuk objek yang melakukan proses penjumlahan parsial
    public Processor(long start, long end, int threadId){
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.partialSum = 0;
    }

    public long getPartialSum() {
        return partialSum;
    }

    @Override
    public void run(){
        //Setiap kali thread dilakukan akan melakukan penjumlahan parsial
        //penjumlahan parsial, contoh: thread 1 = 1-5, maka 1 + 2 + 3 + 4 + 5, thread 2 = 6-10, maka 6 + 7 + 8 + 9 + 10, dst
        System.out.println("Thread " + threadId + " : Melakukan " + start + " sampai " + end);
        for (long i = start; i <= end; i++){
            partialSum += i;
        }
        System.out.println("Thread " + threadId + " Selesai, hasil: " + partialSum);
        
    }
}

public class PenjumlahanParalel {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        //Input untuk jumlah thread dan jumlah angka
        System.out.println("Input jumlah angka yang ingin ditambah:");
        int inputAngka = scanner.nextInt();

        System.out.println("Input jumlah thread yang ingin dimasukan:");
        int inputThread = scanner.nextInt();

        scanner.close();

        //Membuat array bertipe Thread dan Processor yang besar indexnya ditentukan oleh inputThread,
        //Elemen pada array akan diisi oleh masing-masing objek thread dan processor yang menampung setiap variabel
        Thread[] threads = new Thread[inputThread];
        Processor[] processors = new Processor[inputThread];

        //Bagian pembagian tugas untuk dilakukan pada setiap thread, 
        //jika angka= 4 dan thread = 2, maka thread 1: start = 1 dan end = 2, thread 2: start = 3 dan end = 4

        for (int i = 0; i < inputThread; i++){
            long start = i * (inputAngka / inputThread) + 1;
            long end = (i + 1) * (inputAngka / inputThread);
            
            //Penanganan sisa jika pembagian ada sisa, seperti angka= 5 dan thread = 2| 5/2 = 2 sisa 1, 
            //untuk sisanya dimasukkan ke thread terakhir
            if (i == inputThread - 1){
                end = inputAngka;
            }
            int id = i + 1; //Thread-0 -> id=1, Thread-1 -> id=2, etc

            //pembuatan objek processor berdasarkan variable yang ditentukan sebelumnya, dan objek thread yang menampung processor tersebut
            //lalu run setiap thread untuk memulai kalkulasi
            processors[i] = new Processor(start, end, id);
            threads[i] = new Thread(processors[i]);
            threads[i].start();
        }

        //Bagian penjumlahan total dari seluruh penjumlahan parsial
        long totalSum = 0;
        //Thread yang ditentukan akan memasuki main thread dan menambahkan nilainya ke totalSum, penentuan melalui iterasi 
        for (int i = 0; i < inputThread; i++){
            //main thread akan menunggu thread lain selesai terlebih dahulu sebelum melakukan penjumlahan ke totalSum, sehingga tidak akan terjadi Race Condition
            threads[i].join();
            totalSum += processors[i].getPartialSum();

        }
        System.out.println("Hasil parsial akhir dari program: " + totalSum );

    }
}
