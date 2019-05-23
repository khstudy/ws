package com.kh.ws.chat;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/chat/helloWebSocket", configurator=HelloWebSocketConfigurator.class)
public class HelloWebSocketServer {
	//현재접속한 userId와 WebSocketSession을 매핑해 관리할 static필드(동기화처리 필수) 
    private static Map<String,Session> clients = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(EndpointConfig config, Session session) throws IOException{
    	//접속리스트에 새로 연결 요청이 들어온 사용자를 추가한다.
        String userId = (String)config.getUserProperties().get("userId");
        clients.put(userId, session);

        //세션에 userId를 담아서 onClose등에서 사용할 수 있게함.
		session.getUserProperties().put("userId", userId);

        System.out.println("현재접속자("+clients.size()+") : "+clients);
        
        //접속사실을 이 채팅방의 클라이언트들에게 알림
        onMessage("welcome§["+userId+"]님이 입장하셨습니다.", session);
    }
    
    @OnMessage
    public void onMessage(String msg, Session session) throws IOException{
    	 System.out.println("[서버가 받은 메세지} : "+msg);
         
         
         /*하나의 업무가 실행하는 동안 사용자변경이 일어나서는 안된다.
         	즉, NullPointerException을 방지하기 위해 동기화처리*/
         synchronized (clients) {
             Collection<Session> sessionList = clients.values();
             
             for(Session client : sessionList) 
                 
                 //메세지를 작성한 본인을 제외하고, 메세지를 보냄.
                 if(!client.equals(session))
                     client.getBasicRemote().sendText(msg);
         }
    }
    
    @OnError
    public void onError(Throwable e){
        // 데이터 전달 과정에서 에러가 발생할 경우 수행하는 메소드
	    //e.printStackTrace();
    }
    
    @OnClose
    public void onClose(Session session) throws IOException{
    	String userId = (String)session.getUserProperties().get("userId");
		onMessage("adieu§"+userId, session);
		clients.remove(userId);
		
		System.out.println("현재접속자("+clients.size()+") : "+clients);
    }

}
