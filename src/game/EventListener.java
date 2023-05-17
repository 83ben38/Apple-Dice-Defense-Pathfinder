package game;

public interface EventListener {
    default void roundStarted(int roundNumber){}
    default void merged(GTile merger1, GTile merger2, GTile result){}
    default void placed(GTile placed){}
    default void pickedUp(GTile pickedUp){}
    default void diceGrown(GTile grown){}
}
