

// A Java program for a Client
import java.net.*;
import java.io.*;

public class Client
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream  input   = null;

    private DataInputStream in=null;
    private DataOutputStream out     = null;

    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input  = new DataInputStream(System.in);

            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }



        // string to read message from input
//        String line = "";

        // keep reading until "Over" is input
//        while (!line.equals("Over"))
//        {
//            try
//            {
//                line = input.readLine();
//                out.writeUTF(line);
//            }
//            catch(IOException i)
//            {
//                System.out.println(i);
//            }
//        }
//
//        // close the connection
//        try
//        {
//            input.close();
//            out.close();
//            socket.close();
//        }
//        catch(IOException i)
//        {
//            System.out.println(i);
//        }
    }
    private void closeConnection()
    {
        try
        {
            out.writeUTF("over");
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    private String login() throws IOException {

        System.out.println("Please input the login credentials:");
        System.out.print("Username: ");
        String user="";
        user=input.readLine().trim();
        System.out.print("Password: ");
        String pass="";
        pass=input.readLine().trim();
        out.writeUTF("login");
        out.writeUTF(user);
        out.writeUTF(pass);
        return in.readUTF();
    }

    private void register() throws IOException{
        System.out.println("Please choose credentials");
        System.out.print("Email: ");
        String email="";
        email=input.readLine().trim();
        System.out.print("Username: ");
        String user="";
        user=input.readLine().trim();
        System.out.print("Password: ");
        String pass="";
        pass=input.readLine().trim();
        System.out.print("Verify password: ");
        String pass2="";
        pass2=input.readLine().trim();
        if(pass==pass2&&email.length()>0&&pass.length()>0&&user.length()>0)
        {
            out.writeUTF("register");
            out.writeUTF(email);
            out.writeUTF(user);
            out.writeUTF(pass);
            if(in.readBoolean())
            {
                System.out.println("Registered succesfuly");
                mainMenu();
            }
            else
                registerFailed();
        }
            else
            {
                registerFailed();
            }
    }
    private void registerFailed() throws IOException {
        System.out.println("Wrong credentials");
        System.out.println("1.Try again");
        System.out.println("2.Back to the main menu");
        String line = "";
        input.readLine();
        switch (line) {
            case "1":
                register();
                break;
            case "2":
                mainMenu();
                break;
        }
    }
    private void recoverFailed() throws IOException
    {
        System.out.println("Wrong credentials");
        System.out.println("1.Try again");
        System.out.println("2.Back to the main menu");
        String line = "";
        input.readLine();
        switch (line) {
            case "1":
                recoverPass();
                break;
            case "2":
                mainMenu();
                break;
        }
    }
    private void recoverPass() throws IOException
    {
        System.out.println("Please insert credentials");
        System.out.println("Email:");
        String email="";
        email=input.readLine().trim();
        System.out.print("Username: ");
        String user="";
        user=input.readLine().trim();
        out.writeUTF("recover");
        out.writeUTF(email);
        out.writeUTF(user);
        String pass= in.readUTF();
        if(pass!="null")
            System.out.println("Your password is: "+pass);
        else
            recoverFailed();
    }
    private void loginSucc(String user) throws IOException
    {
        System.out.println("Welcome back, "+user+"!");
        System.out.println("1.Log out");
        System.out.println("2.Close connection");
        String line="";
        input.readLine();
        switch (line) {
            case "1":
                mainMenu();
                break;
            case "2":
                closeConnection();
                break;
        }

    }
    private void loginFailed() throws IOException {
        System.out.println("Wrong credentials");
        System.out.println("1.Try again");
        System.out.println("2.Recover password");
        System.out.println("3.Back to the main menu");
        String line="";
        input.readLine();
        switch (line)
        {
            case "1":
                if(login()!="null")
                {
                    loginSucc(login());
                }
                else
                {
                    loginFailed();
                }
                break;
            case "2":
                recoverPass();
                break;
            case "3":
                mainMenu();
        }
    }
    public void mainMenu()
    {
        System.out.println("Welcome to the console!");
        System.out.println("Pick where to go next:");
        System.out.println("1.Login");
        System.out.println("2.Register");
        System.out.println("3.Recover password");
        System.out.println("4.Close connection");
        String line = "";
        try {
            line = input.readLine();
            switch (line){
                case "1":
                    if(login()!="null")
                    {
                        loginSucc(login());
                    }
                    else
                    {
                        loginFailed();
                    }
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    recoverPass();
                    break;
                case "4":
                    closeConnection();
                    break;
            }
        }
        catch (IOException i)
        {
            i.printStackTrace();
        }

    }
    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
        client.mainMenu();
    }
}