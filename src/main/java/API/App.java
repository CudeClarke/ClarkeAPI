package API;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class App {
    public final static int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add("/images");
            javalinConfig.bundledPlugins.enableCors(cors->{
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(PORT);

        app.get("/usuario/{dni}", UserHandlers.getUserByDni);
        app.post("/nuevoUsuario", UserHandlers.registerUser);

        app.get("/eventos", EventoHandlers.getEvents);
        app.get("/eventos/{idEvento}", EventoHandlers.getEventByID);
        app.get("/eventos/{id}/entradas", EntradaHandlers.getEntradasByEvento);
        app.post("/nuevoEvento", EventoHandlers.addEvent);

        app.post("/comprar/check", CompraHandlers.checkTicketsAvailability);
        app.post("/comprar/start", CompraHandlers.setTransaction);
        app.post("/comprar/confirmar/{idTransaction}", CompraHandlers.processPayment);
        app.get("/comprar/cancelar/{idTransaction}", CompraHandlers.cancelTransaction);
        app.post("/getTicketsInfo", CompraHandlers.getTicketsInfo);
    }
}
