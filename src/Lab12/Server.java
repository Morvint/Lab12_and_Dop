package Lab12;
// Реализация серверной части


import java.io.*;
import java.util.*;
import java.net.*;
public class Server
{

    // Вектор для хранения активных клиентов
    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;

    public static void main(String[] args) throws IOException
    {
        // сервер прослушивает порт 1234
        ServerSocket ss = new ServerSocket(1234);

        Socket s;

        // запуск бесконечного цикла для получения
        // запроса клиента
        while (true)
        {
            // Принять входящий запрос
            s = ss.accept();

            System.out.println("New client request received : " + s);

            // получение входных и выходных потоков
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            // Создание нового объекта-обработчика для обработки этого запроса.
            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos);

            // Создание нового потока с этим объектом.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // добавление этого клиента в список активных клиентов
            ar.add(mtch);

            // запуск потока
            t.start();

            // увеличение значение i для нового клиента.
            // i используется только для присвоения имен и может быть заменен
            // любой схемой присвоения имен
            i++;

        }
    }
}

class ClientHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // конструктор
    public ClientHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {
                // получение строки
                received = dis.readUTF();

                //Разбиение строки для получения комманды
                String command [] = received.split("@");

                //Команды на смену имени
                if (command[0].equals("name")){
                    this.name = command[1];
                    continue;
                }

                if(received.equals("logout"))
                {
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }

                // Разбиение строки на сообщение и получателя
                String SplitsReceived [] = received.split("@");

                // Проверка, указан ли получатель
                // Если нет, то сообщение пересылается всем
                if (SplitsReceived.length != 2)
                {
                    String MsgToSend1 = SplitsReceived[0];

                    for (ClientHandler mc : Server.ar)
                    {
                        if(mc.isloggedin==true && !(mc.name.equals(this.name)))
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend1);
                        }
                    }
                } else //Иначе ищется конкретный получатель
                {
                    String MsgToSend = SplitsReceived[0];
                    String recipient = SplitsReceived[1];

                    // найти получателя в списке подключенных устройств.
                    // ar - это клиент для хранения векторов активных пользователей
                    for (ClientHandler mc : Server.ar)
                    {

                        // если получатель найден, то записывается выходной поток
                        if (mc.name.equals(recipient) && mc.isloggedin==true)
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            break;
                        }
                    }
                }

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // закрытие
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
