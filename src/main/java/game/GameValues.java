package game;

public enum GameValues {
    FIELD_SIZE(32),
    NR_OF_FIELDS(20),
    SLEEP_TIME(50),
    GAME_WIDTH(NR_OF_FIELDS.getValue()*FIELD_SIZE.getValue() + 64),
    GAME_HEIGHT(NR_OF_FIELDS.getValue()*FIELD_SIZE.getValue() + 64),
    X_OFF(32),
    Y_OFF(20),
    START_HEALTH(75),
    EAT_BONUS(75);

    private int value;

    private GameValues(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
