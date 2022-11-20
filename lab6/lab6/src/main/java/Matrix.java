import java.util.Random;

public class Matrix {
    int[] matrix;
    int height, width;
    String name;

    Matrix(int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.matrix = new int[width * height];
        this.name = name;
    }

    public Matrix(int size, String name) {
        this.height = size;
        this.width = size;
        this.matrix = new int[width * height];
        this.name = name;
    }

    public void fillRandom(int maxNumber) {
        var rand = new Random();
        for (int i = 0; i < height * width; i++)
            this.matrix[i] = rand.nextInt(maxNumber);
    }
}