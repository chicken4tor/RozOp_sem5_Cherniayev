public class Bees extends Thread{
    private final Field field;

    public Bees(Field Field) {
        this.field = Field;
    }
    @Override
    public void run() {
        while (true) {
            int rowToCheck = field.getRowToSearch();

            if (rowToCheck == -1 || rowToCheck>=field.getRows()) {
                System.out.println(Thread.currentThread().getName() + " закінчила свою роботу");
                break;
            }
            System.out.println(Thread.currentThread().getName() + " дивиться на ділянці: " + rowToCheck);

            boolean[] row = field.getRow(rowToCheck);
            for (int i = 0; i < field.getColumns(); i++) {
                if (row[i]==true) {
                    field.bearWasFound();
                    System.out.println(Thread.currentThread().getName() + " знайшла ведмедя в ділянці " + rowToCheck + " на позиції " + i);
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " Закінчила шукати на ділянці: " + rowToCheck);
        }
    }
}
