import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
public class Client extends JFrame{
    Socket socket;
    BufferedReader br;//for reading the data
    PrintWriter out;

    //declare components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.BOLD,20);
    public Client(){
        try{
            System.out.println("Sending request to server");
            // socket = new Socket("127.0.0.1",7778);
            socket = new Socket("192.168.1.5",7778);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            hangleEvent();
            startReading();
            // startWriting();
        }
        catch(Exception e){

        }
    }
    private void createGUI(){
        this.setTitle("Client Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(new ImageIcon("clogo.jpg"));
        // heading.setHorizontalTextPosition(SwingConstants.CENTER);
        // heading.setHorizontalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messagArea.setEditable(false);
        //set the layout of frame
        this.setLayout(new BorderLayout());

        //addint the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    public void hangleEvent(){
        messageInput.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e){

            }
            public void keyPressed(KeyEvent e){

            }
            public void keyReleased(KeyEvent e){
                // System.out.println("Key Released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messagArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }
    public void startReading(){
        // thread-> read krke data rhega
        Runnable r1 = ()->{
            System.out.println("Reader started...");
            try{
            while(true){
                
                String msg = br.readLine();
                if(msg.equals("exit")){
                    // System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                // System.out.println("Server : "+msg);
                messagArea.append("Server : "+msg+"\n");
            }
        }catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection closed");
        }
        };
        new Thread(r1).start();

    }
    public void startWriting(){
        // thread -> data user lega and then send krega client tk
        Runnable r2 = ()->{
            System.out.println("Writer started");
            try{
            while(true && !socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
            }
            // System.out.println("Connection is closed");
        }catch(Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r2).start();

    }
    public static void main(String ar[]){
        System.out.println("This is client");
        new Client();
    }
}
