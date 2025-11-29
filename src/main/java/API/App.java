package API;

import io.javalin.Javalin;

public class App {
    public final static int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(PORT);

        app.get("/user/{dni}", UserHandlers.getUser);
        app.post("/newUser", UserHandlers.storeUser);
    }
}
