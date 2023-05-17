package game;

public interface BulletCode {
    void run(GProjectile bullet);
    default int pierce(GProjectile bullet){
        return 2 + (bullet.lvl/2);
    }
}
