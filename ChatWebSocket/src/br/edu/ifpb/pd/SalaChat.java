package br.edu.ifpb.pd;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/chat")
public class SalaChat {

    private static List<Session> usuarios = 
            Collections.synchronizedList(
                            new ArrayList<Session>());
    @OnOpen
    public void conectar(Session ses){
        try {
            ses.getBasicRemote().sendText("Bem vindo ao chat de PD");
        } catch (IOException ex) {
            Logger.getLogger(SalaChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        usuarios.add(ses);
    }
    @OnMessage
    public void onMessage(String message) {
        for(Session s:usuarios){
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                Logger.getLogger(SalaChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @OnClose
    public void desconecta(Session ses){
        usuarios.remove(ses);
    }
    
}
