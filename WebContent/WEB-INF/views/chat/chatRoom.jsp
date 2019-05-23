<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
   var host = location.host;//localhost이거나, 서버컴퓨터 ip주소를 담아둠.
   var ws = new WebSocket('ws://'+ host +'${pageContext.request.contextPath}/chat/helloWebSocket');

   //웹 소켓을 통해 연결이 이루어 질 때 동작할 메소드
   ws.onopen = function(event){
       console.log("open!");
   };

   // 서버로부터 메시지를 전달 받을 때 동작하는 메소드
   ws.onmessage = function(e){
	   console.log("message!:"+e.data);
   }

   // 서버에서 에러가 발생할 경우 동작할 메소드
   ws.onerror = function(event){
	   console.log("error!");
   }

   // 서버와의 연결이 종료될 경우 동작하는 메소드
   ws.onclose = function(event){
	   console.log("close!");
   }
   </script>
</head>
<body>

</body>
</html>