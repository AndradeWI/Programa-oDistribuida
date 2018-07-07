/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pd.ws;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Rusemberg
 */
@ServerEndpoint("/chat/{sala}/{usuario}")
public class NewWSEndpoint {

    private static Map<String, ArrayList<String>> salas_usuarios = Collections.synchronizedMap(new HashMap<String, ArrayList<String>>());
    private static Map<String, ArrayList<Session>> salas_sessoes = Collections.synchronizedMap(new HashMap<String, ArrayList<Session>>());
    private static Map<String, Session> usuario_session = Collections.synchronizedMap(new HashMap<String, Session>());
    private static int num_user_patter = 0;
    private String nome_usuario = null;
    
    @OnOpen
    public void conectar(Session s, @PathParam("sala")String sala, @PathParam("usuario")String usuario){
        String msg = "";
        if(salas_usuarios.containsKey(sala)){
            if(salas_usuarios.get(sala).contains(usuario)){
                num_user_patter ++;
                this.nome_usuario = "usuário"+num_user_patter;
                salas_usuarios.get(sala).add(this.nome_usuario);
                salas_sessoes.get(sala).add(s);
                usuario_session.put(this.nome_usuario, s);
                
                msg = "{\"type\":\"usuario\",\"name\":\""+this.nome_usuario+"\"}";
            }else{
                this.nome_usuario = usuario;
                salas_usuarios.get(sala).add(this.nome_usuario);
                salas_sessoes.get(sala).add(s);
                usuario_session.put(this.nome_usuario, s);
                msg = "{\"type\":\"usuario\",\"name\":\""+this.nome_usuario+"\"}";
            }
        }else{
            this.nome_usuario = usuario;
            ArrayList<String> lista_usuarios = new ArrayList<String>();
            lista_usuarios.add(this.nome_usuario);
            salas_usuarios.put(sala, lista_usuarios);
        
            ArrayList<Session> lista_sessoes = new ArrayList<Session>();
            lista_sessoes.add(s);
            salas_sessoes.put(sala, lista_sessoes);
            
            usuario_session.put(this.nome_usuario, s);
            
            msg = "{\"type\":\"usuario\",\"name\":\""+this.nome_usuario+"\"}";
        }
      
        String usuarios = "";
        for(String user: salas_usuarios.get(sala)){
            usuarios += ","+user;
        }
        for(Session ses: salas_sessoes.get(sala)){
            try {
                ses.getBasicRemote().sendText("{\"type\":\"usuario\",\"name\":\""+usuarios+"\"}");
            } catch (IOException ex) {
                Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
    @OnClose
    public void desconectar(Session s, @PathParam("sala")String sala){
        if(salas_usuarios.containsKey(sala)){
            if(salas_usuarios.get(sala).contains(this.nome_usuario)){
               salas_usuarios.get(sala).remove(this.nome_usuario);
               salas_sessoes.get(sala).remove(s);
               usuario_session.remove(this.nome_usuario);
            }
        }
        String usuarios = "";
        for(String user: salas_usuarios.get(sala)){
            usuarios += ","+user;
        }
        for(Session ses: salas_sessoes.get(sala)){
            try {
                ses.getBasicRemote().sendText("{\"type\":\"usuario\",\"name\":\""+usuarios+"\"}");
            } catch (IOException ex) {
                Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @OnMessage
    public void onMessage(String message, @PathParam("sala")String sala, Session s) {
        if(message.contains("send")){
            if(message.contains("-u")){
                String[] array = new String[4];
                array = message.split(" ", 4);
                
                String user = array[2];
                
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                Date date = new Date(); 
                String data = dateFormat.format(date);
                
                String msg = "["+this.nome_usuario+"] ["+data+"] reservadamente: " +array[3];
                
                
                //RESOLVER VERIFICAÇÃO DE USUÁRIO SE EXISTE E DA RESPECTIVA SALA
                if(salas_usuarios.containsKey(sala)){
                    if(salas_usuarios.get(sala).contains(user)){
                        if(usuario_session.containsKey(user)){
                            Session session = usuario_session.get(user);
                            try {
                                session.getBasicRemote().sendText("{\"type\":\"mensagem\",\"text\":\""+msg+"\"}");
                            } catch (IOException ex) {
                                Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }else{
                        try {
                            s.getBasicRemote().sendText("{\"type\":\"mensagem\",\"text\":\""+"Mensagem não pôde ser enviada: usuário informado {"+user.toUpperCase()+"} não existe!"+"\"}");
                        } catch (IOException ex) {
                            Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            }else{
                String[] array = new String[2];
                array = message.split(" ", 2);
                
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                Date date = new Date(); 
                String data = dateFormat.format(date);
                String msg = "["+this.nome_usuario+"] ["+data+"] " +array[1];
                
                for(Session session: salas_sessoes.get(sala)){
                    try {
                        if(session != s){
                            session.getBasicRemote().sendText("{\"type\":\"mensagem\",\"text\":\""+msg+"\"}");
                        } 
                    } catch (IOException ex) {
                        Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    @OnError
    public void errors(Throwable t){
        
    }
    
}
