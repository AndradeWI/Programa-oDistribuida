
import java.io.File;
import java.util.Scanner; 

public class Leitor {
    public static void main(String[] args) throws Exception {
    	    String diretorio = "/home/wanderson/Downloads/";
    	    String recurso = "teste.txt";
			String texto = new Scanner(new File(diretorio,recurso), "UTF-8").useDelimiter("\\A").next();
    	    System.out.println(texto);
   
    }
}