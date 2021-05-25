// This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)
// Copyright (c) 1997 by David Flanagan
// This example is provided WITHOUT ANY WARRANTY either expressed or implied.
// You may study, use, modify, and distribute it for non-commercial purposes.
// For any commercial use, see http://www.davidflanagan.com/javaexamples

/*CPG*/ // Fuente: https://resources.oreilly.com/examples/9781565923713/blob/master/SimpleProxyServer.java
/*CPG*/ // Modificado por Carlos Pineda G. 2021

import java.io.*;
import java.net.*;

/**
 * This class implements a simple single-threaded proxy server.
 **/
public class SimpleProxyServer {
  /** The main method parses arguments and passes them to runServer */
  public static void main(String[] args) throws IOException {
    try {
      // Check the number of arguments
      /*CPG*/ // argumentos: host-remoto puerto-remoto puerto-local puerto-server2
      /*CPG*/ if (args.length != 3) 
      if (args.length != 4) 
        throw new IllegalArgumentException("Wrong number of arguments.");

      // Get the command-line arguments: the host and port we are proxy for
      // and the local port that we listen for connections on
      String host = args[0];
      int remoteport = Integer.parseInt(args[1]);
      int localport = Integer.parseInt(args[2]);
      /*CPG*/ int localport2 = Integer.parseInt(args[3]); // puerto del server2
      // Print a start-up message
      // CPG
      // System.out.println("Starting proxy for " + host + ":" + remoteport +
      //                   " on port " + localport);
      System.out.println("Iniciando SimpleProxyServer: " + host + ":" + remoteport +
                         " on port " + localport);
      // And start running the server
      /*CPG*/ // runServer(host, remoteport, localport);   // never returns
      runServer(host, remoteport, localport,localport2);   // never returns
    } 
    catch (Exception e) {
      System.err.println(e);
      System.err.println("Usage: java SimpleProxyServer " +
                         /*CPG*/ // "<host> <remoteport> <localport>");
                         "<remote-host> <remoteport> <localport> <server2-port>");
    }
  }

  /**
   * This method runs a single-threaded proxy server for 
   * host:remoteport on the specified local port.  It never returns.
   **/
  /*CPG*/ // public static void runServer(String host, int remoteport, int localport) 
  public static void runServer(String host, int remoteport, int localport, int localport2) 
       throws IOException {
    // Create a ServerSocket to listen for connections with
    ServerSocket ss = new ServerSocket(localport);

    // Create buffers for client-to-server and server-to-client communication.
    // We make one final so it can be used in an anonymous class below.
    // Note the assumptions about the volume of traffic in each direction...
    final byte[] request = new byte[1024];
    byte[] reply = new byte[4096];
    
    // This is a server that never returns, so enter an infinite loop.
    while(true) { 
      // Variables to hold the sockets to the client and to the server.
      Socket client = null, server = null;
      /*CPG*/ Socket server2 = null;
      try {
        // Wait for a connection on the local port
        client = ss.accept();
        
        // Get client streams.  Make them final so they can
        // be used in the anonymous thread below.
        final InputStream from_client = client.getInputStream();
        final OutputStream to_client= client.getOutputStream();

        // Make a connection to the real server
        // If we cannot connect to the server, send an error to the 
        // client, disconnect, then continue waiting for another connection.
        try { server = new Socket(host, remoteport); 
	/*CPG*/ // en este caso el puerto del servidor2 se define como el puerto local mas uno
	/*CPG*/ server2 = new Socket("localhost", localport2);
	} 
        catch (IOException e) {
          PrintWriter out = new PrintWriter(new OutputStreamWriter(to_client));
          out.println("Proxy server cannot connect to " + host + ":" +
                      remoteport + ":\n" + e);
          out.flush();
          client.close();
          continue;
        }

        // Get server streams.
        final InputStream from_server = server.getInputStream();
        final OutputStream to_server = server.getOutputStream();
	/*CPG*/ final InputStream from_server2 = server2.getInputStream();
	/*CPG*/ final OutputStream to_server2 = server2.getOutputStream();

        // Make a thread to read the client's requests and pass them to the 
        // server.  We have to use a separate thread because requests and
        // responses may be asynchronous.
        Thread t = new Thread() {
          public void run() {
            int bytes_read;
            try {
              while((bytes_read = from_client.read(request)) != -1) {
                to_server.write(request, 0, bytes_read);
                to_server.flush();
		/*CPG*/ to_server2.write(request, 0, bytes_read);
		/*CPG*/ to_server2.flush();
              }
            }
            catch (IOException e) {}

            // the client closed the connection to us, so  close our 
            // connection to the server.  This will also cause the 
            // server-to-client loop in the main thread exit.
            try {to_server.close();
		/*CPG*/ to_server2.close();
	    } catch (IOException e) {}
          }
        };

        // Start the client-to-server request thread running
        t.start();  

        // Meanwhile, in the main thread, read the server's responses
        // and pass them back to the client.  This will be done in
        // parallel with the client-to-server request thread above.
        int bytes_read;
	/*CPG*/ 
	// la respuesta del server (remoto) no se necesita por tanto se ignora
	/*	
        try {
          while((bytes_read = from_server.read(reply)) != -1) {
            to_client.write(reply, 0, bytes_read);
            to_client.flush();
          }
        }
        catch(IOException e) {}
 	*/	
	// se envia al cliente la respuesta del server2 (local)
        try {
          while((bytes_read = from_server2.read(reply)) != -1) {
            to_client.write(reply, 0, bytes_read);
            to_client.flush();
          }
        }
        catch(IOException e) {}

        // The server closed its connection to us, so close our 
        // connection to our client.  This will make the other thread exit.
        to_client.close();
      }
      catch (IOException e) { System.err.println(e); }
      // Close the sockets no matter what happens each time through the loop.
      finally { 
        try { 
          if (server != null) server.close(); 
	  /*CPG*/ if (server2 != null) server.close();
          if (client != null) client.close(); 
        }
        catch(IOException e) {}
      }
    }
  }
}