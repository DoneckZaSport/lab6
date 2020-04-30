package Utility;

import Common.CreatePerson;
import Common.CreateTicket;
import Common.Ticket;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ClientReceiver {
   public  static DatagramSocket sock;
    public static int clientport;
    public static void receive() throws SocketTimeoutException {
        byte[] buffer = new byte[1000];
        try {
            DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
            sock.setSoTimeout(2500);
            sock.receive(reply);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(reply.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            byteArrayInputStream.close();
            objectInputStream.close();
            Map<String,Integer> answer = (Map<String, Integer>) obj;
            if (answer.entrySet().iterator().next().getValue()==0) {
                System.out.println("Ответ с сервера: "+answer.entrySet().iterator().next().getKey());
            }
            else if (answer.entrySet().iterator().next().getValue() == 1){
                System.out.println("Ответ с сервера: "+answer.entrySet().iterator().next().getKey());
                System.out.print("$ ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String s = reader.readLine();
                ClientSender.send(s);
                ClientReceiver.receive();
            }
            else if (answer.entrySet().iterator().next().getValue() == 2){
                System.out.println("Ответ с сервера: "+answer.entrySet().iterator().next().getKey());
                ClientReceiver.receive();
            } else if (answer.entrySet().iterator().next().getValue() == 3) {
                CreateTicket ct=new CreateTicket();
                Ticket ticket = ct.create(Long.parseLong(answer.entrySet().iterator().next().getKey()));
                ClientSender.send(ticket);
                ClientReceiver.receive();
            } else if (answer.entrySet().iterator().next().getValue() == 4){
                CreatePerson cp = new CreatePerson();
                Ticket person = cp.create();
                ClientSender.send(person);
                ClientReceiver.receive();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            System.out.println("Возможно сервер занят или выключен,попробуйте ещё раз.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}