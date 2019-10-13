package game;

public enum DirMutilayer {
    UP(new int[][] {{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1},{1,0},{1,1}}),
    DOWN(new int[][] {{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1}}),
    LEFT(new int[][] {{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}}),
    RIGHT(new int[][] {{-1,-1},{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1}});

    int[][] value;
    private DirMutilayer(int[][] value){
        this.value = value;
    }

    public int[][] getValue() {
        return value;
    }
}
