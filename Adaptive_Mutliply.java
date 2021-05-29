
import java.io.*;

import java.util.*;

public class Adaptive_Mutliply {
    //the range where to use strassen
/* @author:Li Ruiqi
 */
    int calrange=0;
    String file="";
    public Adaptive_Mutliply(String strFile)throws IOException {
        File filename = new File(strFile); // read the input text
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename), "UTF-8"); // reader
        BufferedReader br = new BufferedReader(reader); // create a bufferereader obj
        String line = "";
        calrange = Integer.parseInt(br.readLine());
        if ( calrange == 0 ) {
            findtipe(1300);

        }
        if ( calrange == 0 ) {
            findtipe(2300);
        }
        OutputStreamWriter ops = null;
        BufferedWriter bw = null;
        File file;
            file = new File(strFile);
            ops = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            bw = new BufferedWriter(ops);
            bw.append(String.valueOf(calrange));
                if ( bw != null ) {
                    bw.flush();
                    bw.close();
                }
    }
    public void findtipe(int range) {
        /**
         * @author:Li Ruiqi
         * @methods: find the appropriate method of this computer by for loop create the matrix in different range.
         * @param: range ,the range normally is 1300
         * @return:null
         **/
        Random r=new Random(10);
        //start range
        for (int i = 0; i <10 ; i++) {//use for loop
            long[] testm = new long[(range+100*i)*(range+100*i)];
            for (int j = 0; j <testm.length ; j++) {
                testm[j]=1;
            }
            Matrix test=new Matrix(testm,true);
            long start_st = System.currentTimeMillis();
            Matrix tt=strassen_mult(test,test);
            long end_st=System.currentTimeMillis();
            int time_st= (int) ((end_st-start_st)/1000);
            System.out.println("strassen time:"+time_st);
            long start_nr = System.currentTimeMillis();
            tt=General(test,test);
            long end_nr=System.currentTimeMillis();
            System.out.println("normal:"+(end_nr-start_nr)/1000+"\ncalculating best value of your computer");
            if((end_nr-start_nr)/1000>time_st){
                calrange=range+100*i;
                break;
            }
        }
    }

    public Matrix power_calculate(Matrix matrix,int times) {
        /**
         * @author:Li Ruiqi
         * @methodsName: use the recursion method to calculate the power
         * @param:
         * @return:null
         */
        if ( times==1 ){
            return matrix;
        }
        if(times==2){
            if(matrix.n>calrange){
                return strassen_mult(matrix,matrix);
            }else {
                return  General(matrix,matrix);
            }
        }return power_calculate(power_calculate(matrix,times/2),power_calculate(matrix,times-times/2));

    }public Matrix power_calculate(Matrix matrix1,Matrix matrix2) {
        if(matrix1.n>calrange){
            return strassen_mult(matrix1,matrix2);
        }else {
            return General(matrix1,matrix2);
        }
    }
    public void storage(Matrix a){
        OutputStreamWriter ops = null;
        BufferedWriter bw = null;
        File file;
        try {
            String name="answer";
            file = new File(name + ".txt");
            ops = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            bw = new BufferedWriter(ops);
            bw.append(a.n+"\n");
            bw.append(a.toString());
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
    public Matrix  General(Matrix p, Matrix q){//calculate the different matrix multiplication
        long[] result=new long[q.n*q.n];
        for(int i=0;i<p.n;i++){
            for(int j=0;j<p.n;j++){
                result[i*q.n+j] = 0;
                for(int k=0;k<p.n;k++){
                    result[i*q.n+j] += p.matrix[i*q.n+k]*q.matrix[k*q.n+j];
                }
            }
        }
        return new Matrix(result,p.enlarge);
    }
    public Matrix strassen_mult(Matrix m, Matrix n) {
        /**
         * @author: Li Ruiqi
         * @methodsName: strassen multiplication
         * @param:
         *      matrix a
         *      matrix b
         *
         * @return: Matrix
         */
        if ( m.n ==4  ) {
            Matrix a=General(m,n);
            return a;
        }
        //separate the matrax to four part
        Matrix a11=new Matrix(m.n/2);
        Matrix a12=new Matrix(m.n/2);
        Matrix a21=new Matrix(m.n/2);
        Matrix a22=new Matrix(m.n/2);
        Matrix b11=new Matrix(m.n/2);
        Matrix b12=new Matrix(m.n/2);
        Matrix b21=new Matrix(m.n/2);
        Matrix b22=new Matrix(m.n/2);
        int orsize=m.n;
        int size=0;
        if ( a11.enlarge ){
            size=a11.n-1;
        }else {
            size = a11.n;
        }
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j < size; j++) {//add the number to matrix
                if(j!=a11.n-1||!b11.enlarge) {
                    a11.matrix[i*a11.n+j] = m.matrix[i*orsize+j];
                    a12.matrix[i*a11.n+j] = m.matrix[i*orsize+size+j];
                    a21.matrix[i*a11.n+j] = m.matrix[orsize*size+i*orsize+ j];
                    a22.matrix[i*a11.n+j] = m.matrix[orsize*size+i*orsize +size+ j];
                    b11.matrix[i*a11.n+j] = n.matrix[i*orsize+j];
                    b12.matrix[i*a11.n+j] = n.matrix[i*orsize+size+j];
                    b21.matrix[i*a11.n+j] = n.matrix[orsize*size+i*orsize + j];
                    b22.matrix[i*a11.n+j] = n.matrix[orsize*size+i*orsize +size+ j];
                }
            }
        }//get the 7 special matrax by strassen
        Matrix p1=strassen_mult(a11,minu(b12,b22));//P1=A11*S1=A11*B12-A11*B22 minu(b12,b22)=S1=B12-B22
        Matrix p2=strassen_mult(add(a11,a12),b22);//P2=S2*B22=A11*B22+A12*B22  add(a11,a12)=S2=A11+A12
        Matrix p3=strassen_mult(add(a21,a22),b11);//P3=S3*B11=A21*B11+A22*B11  add(a21,a22)=A21+A22=S3
        Matrix p4=strassen_mult(a22,minu(b21,b11));//P4=A22*S4=A22*B21-A22*B11  minu(b21,b11)=B21-B11=S4
        Matrix p5=strassen_mult(add(a11,a22),add(b11,b22));//P5=S5*S6=A11*B11+A11*B22+A22*B11+A22*B22  add(a11,a22)=A11+A22=S5 add(b11,b22)=B11+B22=S6
        Matrix p6=strassen_mult(minu(a12,a22),add(b21,b22));//P6=S7*S8=A12*B21+A12*B22-A22*B21-A22*B22  minu(a12,a22)=A12-A22=S7 add(b21,b22)=B21-B22=S8
        Matrix p7=strassen_mult(minu(a11,a21),add(b11,b12));//P7=S9*S10=A11*B11+A11*B12-A21*B11-A21*B12  minu(a11,a21)=A11-A21=S9 add(b11,b12)=B11-B12=S10
        Matrix c11=add(add(p5,p4),minu(p6,p2));//C11=P5+P4-P2+P6
        Matrix c12=add(p1,p2);//C12=P1+P2
        Matrix c21=add(p3,p4);//C21=P3+P4
        Matrix c22=minu(add(p1,p5),add(p3,p7));//C22=P5+P1-P3-P7
        //combine 4 submatrax together
        Matrix c=new Matrix(m.n);
        c.enlarge=m.enlarge;
        for (int i = 0; i <c.n/2 ; i++) {
            for (int j = 0; j <c.n/2 ; j++) {
                c.matrix[i*c.n+j]=c11.matrix[i*c11.n+j];
                c.matrix[i*c.n+size+j]=c12.matrix[i*c11.n+j];
                c.matrix[size*c.n+i*c.n+j]=c21.matrix[i*c11.n+j];
                c.matrix[size*c.n+i*c.n+size+j]=c22.matrix[i*c11.n+j];
            }
        }
        return c;
    }
    public Matrix add(Matrix a,Matrix b){
        /**

         * add two matrix
         * @author: Li Ruiqi
         * @Time 2012-11-2014:49:01

         *@param:
          *      matrix a
          *      matrix b
          *
          * @return: Matrix

         */
        Matrix c=new Matrix(a.n);
        for (int i = 0; i <c.matrix.length ; i++) {
            c.matrix[i]=a.matrix[i]+b.matrix[i];
        }

        return c;
    }
    public Matrix minu(Matrix a,Matrix b){
        /**
         * @author: Li Ruiqi
         * @methodsName: minus 2 matrax
         * @param:
         *      matrix a
         *      matrix b
         *
         * @return: Matrix
         */
        Matrix c=new Matrix(a.n);
        for (int i = 0; i <c.matrix.length ; i++) {
            c.matrix[i]=a.matrix[i]-b.matrix[i];
        }
        return c;
    }

    public static void main(String[] args) throws IOException {//test
        Adaptive_Mutliply adaptive_mutliply=new Adaptive_Mutliply(args[0]);
        Scanner input=new Scanner(System.in);
        System.out.println("Choose the type you want to calculate(input 1 or 2): \n 1.multiply \n 2.power");
        int tipe=input.nextInt();
        input.nextLine();
        String in1="";
        String in2="";
        int pow=0;
        if(tipe==1){
            System.out.println("type the path of first matrix:");
            in1=input.nextLine();
            System.out.println("type the second path of matrix:");
            in2=input.nextLine();
            Matrix matrix1=new Matrix(in1);
            Matrix matrix2=new Matrix(in2);
            Matrix matrix3=adaptive_mutliply.power_calculate(matrix1,matrix2);
            adaptive_mutliply.storage(matrix3);
        }else {
            System.out.println("input your matrix path:");
            in1=input.nextLine();
            System.out.println("input the power");
            pow=input.nextInt();
            Matrix matrix1=new Matrix(in1);
            Matrix matrix2=adaptive_mutliply.power_calculate(matrix1,pow);
            adaptive_mutliply.storage(matrix2);
        }

    }
}
class  Matrix{
/**
 * @author: Li Ruiqi
 * @className: Matrix
 * @description: create a Matrix by a long[] or dimension or config.txt
 * the text should be like this:
 * the first line contains size and the following are a sqare matrix
 * the matrix are stored in array. and will auto enlarge when is odd
 * *@param:
 *              matrix :matrix
 *              n:dimension
 * @data: 2020-05-10 12:20
*/
public long[] matrix;
    public int n;
    public boolean enlarge=false;
    public Matrix(String strFile) throws IOException {
        /* read TXT file to create matrix */
        File filename = new File(strFile); // read the input text
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename), "UTF-8"); // reader
        BufferedReader br = new BufferedReader(reader); // create a bufferereader obj
        String line = "";
        int size=Integer.parseInt(br.readLine());
        n=size;
        if(n==1){
            matrix=new long[n*n];
            int next=br.read();
            matrix[0] = next;
            return;
        }
        if(n%2!=0){
            n++;
            enlarge=true;
        }int i=0;
        matrix=new long[n*n];
        while((line=br.readLine())!=null){
            String [] retline=line.split("\\s+");
            for (int j = 0; j <retline.length ; j++) {
                matrix[j+i*n]=Integer.parseInt(retline[j]);
            }
            i++;
        }
    }
    public Matrix(int n){
        if ( n==4 ){
            this.matrix=new long[n*n];
            this.n=n;
            return;
        }
        if(n%2!=0){
            n++;
            enlarge=true;
        }
        this.matrix=new long[n*n];
        this.n=n;
    }
    public Matrix(long[] matrix,boolean enlarge){//used for test,need n be even
        this.matrix=matrix;
        this.n= (int) Math.sqrt(matrix.length);
        this.enlarge=enlarge;
    }

    @Override
    public String toString() {
        String a="";
        for (int i = 0; i <matrix.length ; i++) {
            if(enlarge&&(i+1)%n==0){
                a+="\n";
            }else if ( (i+1)%n==0 ){
                a+=matrix[i]+"\n";
            }else if(enlarge&&i>n*(n-1)-1) {
                break;
            }else{
                    a+=matrix[i]+" ";
                }
        }
        return "Matrix: \n"+
                 a ;
    }
}