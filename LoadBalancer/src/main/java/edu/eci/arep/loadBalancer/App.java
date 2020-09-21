package edu.eci.arep.loadBalancer;

import edu.eci.arep.loadBalancer.data.DBConnection;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import java.io.IOException;
import static spark.Spark.*;

/**
 * Main Application
 */
public class App
{
    private static int serverNumber=0;
    /**
     * The entry point of application also it defines the Endpoints .
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    private static String[] ports={":8081",":8082",":8083"};
    private static String baseUrl="http://172.17.0.1";
    public static void main( String[] args ) throws IOException {
        Balancer b = new Balancer();
        staticFileLocation("/public");
        port(getPort());
        get("/", (req, res) -> {
            String lel = b.getMessages(baseUrl+ports[serverNumber]+"/");
            roundRobin();
            return lel;
        });
        get("/testDB",(req,res)->{
            String lul = b.getMessages(baseUrl+ports[serverNumber]+"/testDB");
            System.out.println(lul);
            roundRobin();
            return lul;
        });

        post("/testPost",(req,res) -> {
            String lul = b.postMessage(req.body() + "-" + (serverNumber+1) ,baseUrl+ports[serverNumber]+ "/testPost");
            System.out.println(lul);
            roundRobin();
            return lul;
            });
    }
    public static void roundRobin(){
        App.serverNumber=(App.serverNumber+1)% ports.length;
    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set(i.e on localhost)
    }

}
