import java.util.concurrent.CyclicBarrier;

public class Str {
    private String str1,str2,str3,str4;
    private final MyThread[]threads;
    private boolean stopExecuting = false;

    public Str(int size){
        this.str1 = randStr(size);
        this.str2 = randStr(size);
        this.str3 = randStr(size);
        this.str4 = randStr(size);
        CyclicBarrier barrier = new CyclicBarrier(4, () -> {
            System.out.println("Бар'єр досягся");
            System.out.println("Рядки: " + this.getStrings() + " Розміри: " + this.getCounts());
            if(this.isEqual() && !this.getStopExecutingStatus()){
                this.setStopExecuting(true);
            }
        });
        this.threads = new MyThread[4];
        this.threads[0] = new MyThread(barrier,'1',this);
        this.threads[1] = new MyThread(barrier,'2',this);
        this.threads[2] = new MyThread(barrier,'3',this);
        this.threads[3] = new MyThread(barrier,'4',this);
    }

    public boolean getStopExecutingStatus() {
        return stopExecuting;
    }
    private void setStopExecuting(boolean ParStopExecuting) {
        this.stopExecuting = ParStopExecuting;
    }
    private String getStrings() {
        return str1 + " " + str2 + " " + str3 + " " + str4;
    }
    private String getCounts(){
        return (CountAandB(this.str1) + " " + CountAandB(this.str2) + " " + CountAandB(this.str3) + " " + CountAandB(this.str4));
    }
    private long CountAandB(String str){
        return str.chars().filter(ch -> ch == 'A' || ch == 'B').count();
    }
    private String randStr(int size){
        StringBuilder generatedString = new StringBuilder();
        generatedString.append("C".repeat(Math.max(0, size / 2)));
        generatedString.append("D".repeat(Math.max(0, size / 2)));
        int count = (int)(Math.random()*size);
        for(int i = 0;i<count;i++){
            generatedString = new StringBuilder(stringCharChange(generatedString.toString(),i));
        }
        return generatedString.toString();
    }
    private String stringCharChange(String str, int ind){
        int indx;
        if(ind == -1){
            indx = (int)(Math.random()*str.length());
        }
        else{
            indx = ind;
        }
        String newStr = str;
        switch (str.charAt(indx)) {
            case ('A') -> newStr = newStr.substring(0, indx) + 'C' + newStr.substring(indx + 1);
            case ('B') -> newStr = newStr.substring(0, indx) + 'D' + newStr.substring(indx + 1);
            case ('C') -> newStr = newStr.substring(0, indx) + 'A' + newStr.substring(indx + 1);
            case ('D') -> newStr = newStr.substring(0, indx) + 'B' + newStr.substring(indx + 1);
            default -> {
            }
        }
        return newStr;
    }
    public void work(char option){
        switch (option) {
            case ('1') -> str1 = stringCharChange(str1,-1);
            case ('2') -> str2 = stringCharChange(str2,-1);
            case ('3') -> str3 = stringCharChange(str3,-1);
            case ('4') -> str4 = stringCharChange(str4,-1);
            default -> {
            }
        }
    }
    public boolean isEqual(){
        if((CountAandB(str1) == CountAandB(str2) && CountAandB(str1) == CountAandB(str3))){
            return true;
        } else if ((CountAandB(str1) == CountAandB(str2) && CountAandB(str1) == CountAandB(str4))) {
            return true;
        } else if ((CountAandB(str1) == CountAandB(str3) && CountAandB(str1) == CountAandB(str4))){
            return true;
        } else return (CountAandB(str2) == CountAandB(str3) && CountAandB(str2) == CountAandB(str4));
    }
    public void run(){
        System.out.println("Початковий стан рядків та розмірів: " + this.getStrings() + " " + this.getCounts());
        for(int i =0;i<4;i++){
            this.threads[i].start();
        }
    }

}