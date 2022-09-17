public class Field {
    private boolean [][]field;
    private final int beeNum = 3;
    private final int rows,columns;
    private boolean bearWasFound;
    private int rowToSearch;
    private final Thread[] bees = new Thread[beeNum];

    Field(int row, int column){
        this.rows = row;
        this.columns = column;
        this.rowToSearch=0;
        this.bearWasFound= false;
        this.field = new boolean[this.rows][this.columns];
        this.field[(int)(Math.random()*(this.rows-1))][(int)(Math.random()*(this.columns-1))]=true;
        for(int i =0;i<beeNum;i++){
            bees[i] = new Thread(new Bees(this));
            bees[i].setName("Бджолa "+ (i+1));
        }
    }
    public synchronized int getRowToSearch() {
        if(bearWasFound) {
            return -1;
        }
        else{
            return this.rowToSearch++;
        }
    }
    public int getRows(){
        return rows;
    }
    public int getColumns(){
        return columns;
    }
    public boolean[] getRow(int row){
        return field[row];
    }
    public void bearWasFound(){
        bearWasFound=true;
    }
    public void startExecution(){
        for(int i =0;i<beeNum;i++){
            bees[i].start();
        }
    }
}
