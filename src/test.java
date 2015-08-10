import java.util.ArrayList;

/**
 * Created by abetts on 8/7/15.
 */
public class test {

    public static void main(String... args){

        int [][] yoloy = new int [10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                yoloy[i][j] = (int) (Math.random() * 10);
                System.out.print(yoloy[i][j]+"\t");
            }
            System.out.println();
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ArrayList<Integer> k = new ArrayList<>();
                for(int x = Math.max(i-1,0);x<Math.min(10,i+2);x++){
                    for(int y = Math.max(j-1,0);y<Math.min(10,j+2);y++){
                        System.out.print(yoloy[x][y] + " " + i + " " + j + "|\t");
                    }
                }
                System.out.println();


            }
        }
    }
}
