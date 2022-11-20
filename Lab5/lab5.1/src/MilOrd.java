public class MilOrd {
    private final boolean[]order; // false - left, true - right
    private final MyThread[]threads;
    private final int size;
    private final int threadsSize;
    private final boolean[] neighborsPrevState;
    private final int[] neighborsInd;
    private boolean isChanged;
    private boolean StopExecuting;
    private final CyclicBarrierCustom barrier;

    MilOrd(int size, int threadsSize){
        if(size/threadsSize>=50 && size>=100 && threadsSize>=2){
            this.size=size;
            this.threadsSize=threadsSize;
        }
        else{
            this.size=100;
            this.threadsSize=2;
        }
        this.neighborsPrevState = new boolean[this.threadsSize*2];
        this.neighborsInd = new int[this.threadsSize*2];
        this.threads = new MyThread[this.size];
        this.order = new boolean[this.size];
        this.isChanged = false;
        this.barrier = new CyclicBarrierCustom(this.threadsSize, () -> {
            System.out.println("Бар'єр досягся");
            this.afterwork();
            this.print();
            if(!this.isChanged){
                this.StopExecuting=true;
            }
            this.isChanged = false;
        });
    }

    public boolean isStopExecuting() {
        return StopExecuting;
    }
    public void print(){
        for(int i = 0;i<this.size;i++){
            System.out.print(this.order[i] + " ");
        }
        System.out.println("");
    }
    private void initialization(){
        //Заповнення order (нульова команда)
        for(int i=0;i<size;i++){
            int option = (int)(Math.random()*2);
            if(option==0){
                this.order[i] = false;
            } else{
                this.order[i]= true;
            }

        }
        //Поділ масиву для потоків і заповнення службових
        int counter=0;
        int left,right;
        for(int i =0;i<threadsSize;i++){
            left = i*(size/threadsSize);
            if(i==threadsSize-1){
                right = ((i*(size/threadsSize)+size/threadsSize)+(size-(i+1)*(size/threadsSize)));
            }
            else{
                right =((i*(size/threadsSize)+size/threadsSize));
            }
            this.neighborsPrevState[counter] = this.order[i*(size/threadsSize)];
            this.neighborsInd[counter] = i*(size/threadsSize);
            this.neighborsPrevState[counter+1] = this.order[right-1];
            this.neighborsInd[counter+1] = right-1;
            System.out.println(left + " " + right);
            threads[i] = new MyThread(this,left,right,barrier,i);
            counter+=2;
        }

    }
    public void work(int begin, int end){
        for(int i =begin;i<end;i++){
            if(i+1<end){
                if(this.order[i] && !this.order[i + 1]) {
                    boolean temp = this.order[i];
                    this.order[i] = this.order[i + 1];
                    this.order[i + 1] = temp;
                    i++;
                    this.isChanged= true;
                }
            }
        }
    }
    public void afterwork(){
        for(int i =1;i<threadsSize*2-1;){
            if((this.order[this.neighborsInd[i]]==this.neighborsPrevState[i])&&(this.order[this.neighborsInd[i+1]]==this.neighborsPrevState[i+1])){
                if(this.order[this.neighborsInd[i]] && !this.order[this.neighborsInd[i + 1]]) {
                    boolean temp = this.order[this.neighborsInd[i]];
                    this.order[this.neighborsInd[i]] = this.order[this.neighborsInd[i+1]];
                    this.order[this.neighborsInd[i+1]] = temp;
                    this.isChanged=true;
                }
            }
            i+=2;
        }
        for(int i =0;i<threadsSize*2;i++){
            this.neighborsPrevState[i]=this.order[this.neighborsInd[i]];
        }
    }
    public void run(){
        initialization();
        this.print();
        for(int i =0;i<threadsSize;i++){
            this.threads[i].start();
        }
    }
}

