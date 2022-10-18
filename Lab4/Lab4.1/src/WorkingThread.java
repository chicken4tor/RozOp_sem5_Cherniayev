public class WorkingThread extends Thread {
    private int task;
    private File file;

    public WorkingThread(File file, int task) {
        this.task = task;
        this.file = file;
    }
    @Override
    public void run() {
        switch (task) {
            case 1: FindPhoneByName(); break;
            case 2: FindNameByPhone(); break;
            case 3: ReadWriteRecords(); break;
            default:
                System.out.println("!!! Unknown task number. !!!");
        }
    }

    private void FindPhoneByName() {
        String name = "Ivan";

        while (!isInterrupted()) {
            file.LockRead();
            System.out.println("Search phone by name: Start.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Record record : file.records) {
                if (record.getName().equals(name))
                    System.out.println("Search phone by name: Found " + record.toString() );
            }
            System.out.println("Search phone by name: Finish.");
            file.UnlockRead();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void FindNameByPhone() {
        String number = "010";

        while (!isInterrupted()) {
            file.LockRead();
            System.out.println("Search name by phone: Start.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Record record : file.records) {
                if (record.getNumber().equals(number))
                    System.out.println("Search name by phone: Found " + record.toString());
            }
            System.out.println("Search name by phone: Finish.");
            file.UnlockRead();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void ReadWriteRecords() {
        Record record1 = new Record("Tessa", "Messa", "papa", "qwe");
        Record record2 = new Record("Ivan", "Pushkin", "Dima", "100");
        boolean tiktok = true;

        while (!isInterrupted()) {
            file.LockWrite();
            System.out.println("Write/delete records: Start.");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tiktok) file.records.add(record1);
            else file.records.remove(record1);

            System.out.println("Write/delete records: Finish.");
            file.UnlockWrite();

            file.LockWrite();
            System.out.println("Write/delete records: Start.");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (tiktok) file.records.remove(record2);
            else file.records.add(record2);

            System.out.println("Write/delete records: Finish.");
            file.UnlockWrite();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tiktok = !tiktok;
        }
    }
}