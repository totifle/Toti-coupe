package ch.totifle.toticoupe;

public class Main {

    public  static Toticoupe thread;

    public static void main(String[] args) {
        thread = new Toticoupe();
        thread.init();
    }
}
