package Lesson6_Java2.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

public class  ServerMain {
    private Vector<ClientHandler> clients;// синхронизированный список клиентов
    public ServerMain() throws SQLException {
        clients = new Vector<>();
        ServerSocket server = null; //инициализируем сервер
        Socket socket = null;//инициализируем сокет
        try {
            AuthService.connect(); // подключение к БД
            server = new ServerSocket(8189); //создаем сервер
            System.out.println("Сервер запущен!");
            while (true){
                socket = server.accept();//ждём подключения клиента
                System.out.println("Клиент подключился!");
                new ClientHandler(this, socket);// создаем нового клиента

                // раз и добавляем их в синхронизированный список
            }


//            Scanner in = new Scanner(socket.getInputStream());//справшиваем входящий поток
//            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);//исходяший поток


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); //закрываем сокет
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();//закрываем сервер
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect(); // закрываем БД
        }
    }

//событие добавляет клиента в список
    public void subscribe(ClientHandler client){
        clients.add(client);
    }
    //событие удаляет  клиента из списка
    public void unsubscribe(ClientHandler client){
        clients.remove(client);
    }
    //метод делает рассылку msg всем пользователям чата, перебирая коллекцию
    public void broadCast(String msg){
        for (ClientHandler o:clients) {
            o.sendMsg(msg);
        }
    }
}