package br.edu.ifpb.pd.eleicoes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint("/voto/{papel}")
public class Eleicao {
	  private static List<Session> usuarios = 
	            Collections.synchronizedList(
	                            new ArrayList<Session>());
	  private static int urna;
	  private static int canditato1;
	  private static int candidato2;
	 
	    @OnOpen
	    public void conectar(Session ses,  @PathParam("papel") String papel){
	    	if(papel.equals("eleitor")) {
	    	try {
	            ses.getBasicRemote().sendText("Eleições 2018 bem vindo "+papel);
	        	
	        } catch (IOException ex) {
	            Logger.getLogger(Eleicao.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        usuarios.add(ses);
	    	}
	    }
	    @OnMessage
	    public void onMessage(Session ses, String message, @PathParam("papel") String papel) {
	    	if(papel.equals("eleitor")) {
                try {
					ses.getBasicRemote().sendText("Resultado parcial="+Integer.toString(urna));
				} catch (IOException e) {
					// TODO Bloco catch gerado automaticamente
					e.printStackTrace();
				}
		        
	        }else {
	        	//instância um objeto da classe Random usando o construtor básico
	    		
	    	    if(message.equals("candidato1")) {
	    	    Random gerador = new Random();
            	canditato1+=gerador.nextInt(26);
            	
	    	    }
	    	    if(message.equals("candidato2")) {
	    	    Random gerador = new Random();
	    	    candidato2+=gerador.nextInt(26);            	
	    	    }
	    	    urna=canditato1 + candidato2;
            	for(Session s:usuarios){
		            try {
		            	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		            	Date date = new Date();
		                s.getBasicRemote().sendText("========"+dateFormat.format(date)+"========");
		                s.getBasicRemote().sendText("Parcial candidato 1="+Integer.toString(canditato1)+" votos");
		                s.getBasicRemote().sendText("Parcial candidato 2="+Integer.toString(candidato2)+" votos");
		                s.getBasicRemote().sendText("Total de votos apurados="+Integer.toString(urna));
		            	}
		             catch (IOException ex) {
		                Logger.getLogger(Eleicao.class.getName()).log(Level.SEVERE, null, ex);
		            }
		        }
            	}
	 
	    }
	    @OnClose
	    public void desconecta(Session ses){
	        usuarios.remove(ses);
	    }

}
