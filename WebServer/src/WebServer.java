import java.io.*;
import java.net.*;
import java.util.*;
public class WebServer {
    
    private int _numRequestsServiced;
    
    private boolean _isStartButtonClicked = false;
    
    private Shape[] _shapes;
    
    private int _curBallSize = 20;
    
    private String _curBallColor = "red";
    
    // Any initialization your web server requires.
    public WebServer() {
        _numRequestsServiced = 0;
        
        _shapes = new Shape[5];
        _shapes [0] = new Ball(200, 200, "red", 20);
        _shapes [1] = new Ball(300, 200, "green", 20);
        _shapes [2] = new Ball(400, 200, "blue", 20);
        
        _shapes [3] = new Rectangle(100, 200, "yellow", 20, 40);
        _shapes [4] = new Rectangle(500, 200, "blue", 30, 20);
    }
    
    // Called when an HTTP Get request is received. 
    // 'path' is the 'rest' of the URL being requested (a URL without the machine name). 
    // The response should be written to 'response'.
    public void processGETRequest(String path, PrintStream response) { 
        _numRequestsServiced++;
        if (path.equals("/advanceButtonClick")) {
        	for(int updateBall = 0; updateBall < _shapes.length; updateBall++) {
        		_shapes[updateBall].update();
        	}
        }
       
        if (path.equals("/advanceButtonClick")) {
        	for(int updateRectangle = 0; updateRectangle < _shapes.length; updateRectangle++) {
        		_shapes[updateRectangle].update();
        	}
        }
        
       if(path.startsWith("/mouseClick@")) {
    	   addBall(path);
       }
       
       if(path.startsWith("/ballSizeTextBox=")) {
    	   this._curBallSize = Integer.parseInt(path.substring(path.indexOf("=") + 1, path.length()));
       }
       
       if(path.startsWith("/ballColorTextBox=")) {
    	   this._curBallColor = path.substring(path.indexOf("=") + 1, path.length());
       }
       
        if (path.equals("/startButtonClick"))
        	this._isStartButtonClicked = true;
        else if (path.equals("/stopButtonClick")) 
        	this._isStartButtonClicked = false;
        
        response.println("<html>");
        response.println("<body>");
        // Create a canvas and draw some shapes on it.
        response.printf("<svg style='background-color:gray;' width='600' height='400' "
                + "onClick=\"location.href='mouseClick@'+event.offsetX+','+event.offsetY\">\n");
	    for (int currentBall = 0; currentBall < _shapes.length; currentBall++) {
	    	_shapes[currentBall].renderAsHTML(response);
	    }
	    
	    for (int currentRectangle = 0; currentRectangle < _shapes.length; currentRectangle++) {
	    	_shapes[currentRectangle].renderAsHTML(response);
	    }
	    
        response.println("</svg>");
        response.println("<br>");
        
        renderButtonAsHtml(response);
       
        response.println("<button onClick=\"location.href='advanceButtonClick'\">Advance</button>");
        if (this._isStartButtonClicked)
        	 response.println(" <script> setTimeout(() => location.href ='advanceButtonClick', 500); </script>");	//you can change reload time here
        response.println("<br>");    
        response.println("Ball Size: <input type=text value='ballSize' onchange=\"location.href='ballSizeTextBox='+event.target.value\"></input><br>");
        response.println("Ball Color: <input type=text value='ballColor' onchange=\"location.href='ballColorTextBox='+event.target.value\"></input>");
        response.println("<br>");
        // You can also simply write text, which we use here to give status.
        response.println("The path for the web page is: " + path + "<br>");
        response.println("Server has been called " + _numRequestsServiced + " times.<br>");
        response.println("</body>");
        response.println("</html>");
    }
    
    //adding new ball to the array
    private void addBall(String path) {
    	int localX, localY;
    	localX = Integer.parseInt(path.substring(path.indexOf("@") + 1, path.indexOf(",")));
    	localY = Integer.parseInt(path.substring(path.indexOf(",") + 1, path.length()));
    	
    	Ball newShape = new Ball(localX, localY, _curBallColor, _curBallSize);
    	_shapes = Arrays.copyOf(_shapes, _shapes.length + 1);   // Make an array that is 1 bigger, copy data
    	_shapes[_shapes.length-1] = newShape;    // Put the new ball in the new slot.  
    }
    
    //rendering HTML button properly
    private void renderButtonAsHtml (PrintStream response) {
    	if (this._isStartButtonClicked) {
    		response.println("<button style='background-color:darkgray' onClick=\"location.href='startButtonClick'\">Start</button>");
    		response.println("<button onClick=\"location.href='stopButtonClick'\">Stop</button>");
    	}
    	else if (!this._isStartButtonClicked){
    		response.println("<button onClick=\"location.href='startButtonClick'\">Start</button>");
    		response.println("<button style='background-color:darkgray' onClick=\"location.href='stopButtonClick'\">Stop</button>");
    	}
    }
    
    // Called when you have established a the 'clientConnection' to some web
    // browser wanting service. Its job is to read the HTTP request, and
    // write back a response
    public void processWebRequest(Socket clientConnection) throws IOException {
        // we read characters from the client via input stream on the socket
        // Connect that stream to something that can read it a line at a time.
        BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
           
        // get first line of the request from the client
        String line = in.readLine();
        if (line == null)
            return;
        int firstSpace = line.indexOf(" ");
        String method = line.substring(0, firstSpace).toUpperCase();
        int secondSpace = line.indexOf(" ", firstSpace + 1);
        if (secondSpace < 0)
            secondSpace = line.length();
        String methodArg = line.substring(firstSpace + 1, secondSpace);
        
        // we support only GET commands 
        if (!method.equals("GET"))
            throw new UnsupportedOperationException("Not Implemented : \" + method + \" method.\"");
        
        // Web browsers look for this special icon, simply reject the request.
        if (methodArg.equals("/favicon.ico")) {
            OutputStream httpResponse = clientConnection.getOutputStream();
            PrintStream httpResponsePrintStream = new PrintStream(httpResponse);
            httpResponsePrintStream.println("HTTP/1.1 404 Not Found");
            clientConnection.close();
            return;
        }
        
        // Get response, a ByteArrayOutputStream makes a place to store chars
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        // make a PrintStream that allows you to do 'println'
        PrintStream responcePrintStream = new PrintStream(responseStream);
        
        // Process the request doing println's to generate a response.
        // we undo the URL escaping of spaces before we send the path to this method.  
        processGETRequest(methodArg.replace("%20", " "), responcePrintStream);
        
        // The resulting output ends up in this byte[] called responseData.
        responcePrintStream.flush();
        byte[] responseData = responseStream.toByteArray();
        // Now send out the complete HTTP response, including HTTP headers
        OutputStream httpResponse = clientConnection.getOutputStream();
        PrintStream httpResponsePrintStream = new PrintStream(httpResponse);
        // send HTTP Headers
        httpResponsePrintStream.println("HTTP/1.1 200 OK");
        httpResponsePrintStream.println("Server: Java HTTP Server Version 1.0");
        httpResponsePrintStream.println("Date: " + new Date());
        httpResponsePrintStream.println("Content-type: text/html");
        httpResponsePrintStream.println("Content-length: " + responseData.length);
        // blank line between headers and content, very important !
        httpResponsePrintStream.println();
        httpResponsePrintStream.flush(); // make sure the header is written out.
        httpResponse.write(responseData); // then send the response.
        httpResponse.close(); // close the response, which insures it is sent.
        clientConnection.close(); // We are not done with the connection
    }
    
    // main program for the server. Open a HTTP port and listen for new connections.
    public static void main(String[] args) throws IOException {
        // port to listen connection
        final int PORT = 8080; // 80 is the default port, use 8080 if you have permissions issues
        // we listen until user halts server execution
        System.out.println("To use this server\ntype this URL into a browser:  http://localhost:" + PORT);
        ServerSocket serverConnect = new ServerSocket(PORT);
        WebServer server = new WebServer();
        while (true) {
            Socket clientConnection = serverConnect.accept();
            server.processWebRequest(clientConnection);
        }
    }
}
