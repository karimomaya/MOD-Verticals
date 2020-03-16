import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by karim on 3/14/20.
 */
public class MainClass {
    public static void main(String[] args ) {
        int[] inputVector = {1, 2, 3, 4, 1, 2 ,3, 3, 3 };
        String r = "";
        int result = Integer.MAX_VALUE;
        System.out.println(result);
        int[] res = new int[inputVector.length];

        for(int i = 0; i< res.length; i++){

            res[i] = 0;
        }
        for (int i=0; i< inputVector.length; i++){

            for(int j=i+1; j < inputVector.length; j++){
                if(inputVector[i] == inputVector[j]){
                    boolean found = false;
                    for(int k =0; k < res.length; k++){
                        if( k == inputVector[j]){
                            found = true;
                        }
                        if(k ==0) k = res.length;

                    }
                    if(!found){

                        if(!r.equals(""))  r += "\n";
                        r += inputVector[j];
                    }

                }
            }
        }
        /* YOUR CODE HERE */
        System.out.println(r);
    }
}
