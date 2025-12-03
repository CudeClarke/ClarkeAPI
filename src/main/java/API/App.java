package API;

import io.javalin.Javalin;

public class App {
    public final static int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        app.get("/user/{dni}", UserHandlers.getUser);
        app.post("/newUser", UserHandlers.storeUser);

        app.get("/api/eventos", EventoHandlers.getEvents);
        app.get("/api/eventos/{nombre}", EventoHandlers.getEventByName);
        app.post("/api/eventos", EventoHandlers.addEvent);

        app.get("/api/tickets", TicketHandler.getTickets);
        app.get("/api/tickets/{id}", TicketHandler.getTicketById);
        app.post("/api/tickets", TicketHandler.addTicket);
        app.delete("/api/tickets/{id}", TicketHandler.deleteTicket);
    }
}
