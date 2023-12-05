package Lab12;
// Реализация клиентской части

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Bot
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
                //Установка имени
                String NewNames = "name@BOT";
                try
                {
                    dos.writeUTF(NewNames);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                long start = System.currentTimeMillis();

                while (true) {

                    if (System.currentTimeMillis() - start > 20000)
                    {
                        start = System.currentTimeMillis();
                        String msg = "Buenos dias, pedrilas";
                        try {
                            // запись в выходной поток
                            dos.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                        // чтение сообщения, отправленного этому клиенту и формирование ответного
                        String msg1 [] = dis.readUTF().split(" : ");
                        String msg2 = msg1[1] + "@" + msg1[0];

                        // запись в выходной поток
                        try {
                            dos.writeUTF(msg2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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