import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Creatmatrix {
    public Creatmatrix(){
        Random random=new Random(50);
        OutputStreamWriter ops = null;
        BufferedWriter bw = null;
        File file;
        try {
            String name="config";
            file = new File("C:\\Users\\李瑞祺\\Downloads\\[www.java1234.com]Java实现飞行棋\\ass3\\src\\",name + ".txt");
            ops = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            bw = new BufferedWriter(ops);
            bw.append("500\n");
            for (int i = 0; i <50*50 ; i++) {
                bw.append(random.nextInt(200)+" ");
                if((i+1)%50==0){
                    bw.append("\n");;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Creatmatrix c=new Creatmatrix();
    }
}
