package Lab12;
// Реализация клиентской части

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    final static int ServerPort = 1234;

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);

        // получение ip-адреса локального хоста
        InetAddress ip = InetAddress.getByName("localhost");

        // установка соединения
        Socket s = new Socket(ip, ServerPort);

        // получение входных и выходных потоков
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // Поток отправки сообщения
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // чтение сообщения для доставки.
                    String msg = scn.nextLine();

                    try {
                        // запись в выходной поток
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // чтение потока сообщений
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // чтение сообщения, отправленного этому клиенту
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();

    }
}