package API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import utils.json_generator;

import DB.MySQLAccessFactory;
import Datos.Ticket.TicketFactory;
import Datos.Usuario.*;
import Managers.EventoManager;
import Managers.UserManager;
import Managers.CompraManager;
import Managers.Transaction;

public class CompraHandlers {
    private static CompraManager compraManager = new CompraManager(MySQLAccessFactory.getInstance());
    private static EventoManager eventoManager = new EventoManager(MySQLAccessFactory.getInstance());
    private static UserManager userManager = new UserManager(MySQLAccessFactory.getInstance());
    private static final String ID_EVENTO = "idEvento";
    private static final String ID_ENTRADA = "idEntrada";
    private static final String AMOUNT = "amount";
    private static final String COMPRADOR = "comprador";
    private static final String LISTA_ENTRADAS = "lista_entrada";
    private static final String PAGO_EXTRA = "pago_extra";
    private static final String ASISTENTE = "asistente";
    private static final String DORSAL = "dorsal";
    private static final String ASIENTO = "asiento";
    private static final String BOLETO = "boleto";

    public static Handler checkTicketsAvailability = ctx -> {
        String res = "";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode req = mapper.readTree(ctx.body());

        int i = 0;
        boolean available = true;
        while (available && i< req.size()){
            JsonNode current_node = req.get(i);
            if (current_node.has(ID_EVENTO) && current_node.has(ID_ENTRADA) && current_node.has(AMOUNT)){
                int idEvento = current_node.get(ID_EVENTO).asInt();
                int idEntrada = current_node.get(ID_ENTRADA).asInt();
                int amount = current_node.get(AMOUNT).asInt();
                available = compraManager.checkAvailabity(idEvento, idEntrada, amount);
            }else{
                i += req.size() + 1;
                res = json_generator.status_response(1, "Incorrect request format");
            }
            i++;
        }

        if (!available){
            res = json_generator.status_response(1, "Tickets not available");
        }
        if (available && i<= req.size()){
            res = json_generator.status_response(0, "");
        }

        ctx.json(res);
    };

    public static Handler setTransaction = ctx ->{
      String res = "";
      ObjectMapper mapper = new ObjectMapper();
      JsonNode req = mapper.readTree(ctx.body());

      if (req.has(COMPRADOR) && req.has(LISTA_ENTRADAS)){
          iUsuario comprador = mapper.treeToValue(req.get(COMPRADOR), UsuarioBase.class);
          JsonNode entradas = req.get(LISTA_ENTRADAS);
          int idTransaction = compraManager.startTransaction(comprador);

          int i = 0;
          boolean correct_format = true;
          boolean exito = true;
          TicketFactory ticketFactory = null;
          int previousEvent = -1;

          while (correct_format && exito && i< req.size()){
              JsonNode current_node = entradas.get(i);
              try {
                int idEvento = current_node.get(ID_EVENTO).asInt();
                int idEntrada = current_node.get(ID_ENTRADA).asInt();
                float pagoExtra = current_node.get(PAGO_EXTRA).floatValue();
                String dniAsistente = current_node.get(ASISTENTE).get("dni").asText();

                String information = "";
                if (current_node.has(DORSAL)) {information = current_node.get(DORSAL).asText();}
                if (current_node.has(ASIENTO)) {information = current_node.get(ASIENTO).asText();}
                if (current_node.has(BOLETO)) {information = current_node.get(BOLETO).asText();}

                if (ticketFactory == null || idEvento != previousEvent){
                    ticketFactory = compraManager.getTicketFactoryByEventType(eventoManager.searchById(idEvento));
                }
                exito = compraManager.addTicketToTransaction(idTransaction, idEvento, idEntrada, ticketFactory.createTicket(comprador,dniAsistente, pagoExtra, information));
                previousEvent = idEvento;
                i++;
              } catch (Exception e){
                  System.out.println(e.getMessage());
                  correct_format = false;
              }
          }
          if (!correct_format){
              compraManager.deleteTransaction(idTransaction);
              res = json_generator.status_response(1, "Incorrect entrada format");
          }
          if (!exito){
              compraManager.deleteTransaction(idTransaction);
              res = json_generator.status_response(1, "Error while processing transaction");
          }
          if (correct_format && exito){
              res = json_generator.status_response(0, Integer.toString(idTransaction));
          }
      }else{
          res = json_generator.status_response(1, "Incorrect request format");
      }
      ctx.json(res);
    };

    public static Handler processPayment = ctx ->{
        String res = "";
        int idTransaction;
        try {
            idTransaction = Integer.parseInt(ctx.pathParam("idTransaction"));
        } catch (NumberFormatException e){
            idTransaction = -1;
        }

        Transaction transaction = compraManager.getTransaction(idTransaction);
        if (transaction != null) {
            boolean paymentSucces = true;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode req = mapper.readTree(ctx.body());

            // We parse buyers info in req and send it to payment service
            //  paymentSuccess = PaymentService.processPayment(req)

            if (paymentSucces) {
                userManager.registerUsuario(transaction.getComprador());
                compraManager.confirmTransaction(idTransaction);
                res = json_generator.status_response(0, "Transaction Confirmed");
            } else {
                res = json_generator.status_response(1, "Error processing payment");
            }
        }else{
            res = json_generator.status_response(1, "Invalid Transaction ID");
        }

        ctx.json(res);
    };

    public static Handler cancelTransaction = ctx ->{
        String res = "";
        int idTransaction;
        try {
            idTransaction = Integer.parseInt(ctx.pathParam("idTransaction"));
        } catch (NumberFormatException e){
            idTransaction = -1;
        }

        if (idTransaction > 0){
            compraManager.deleteTransaction(idTransaction);
            res = json_generator.status_response(0, "Transaction Cancelled");
        }else{
            res = json_generator.status_response(1, "Invalid Transaction ID");
        }

        ctx.json(res);
    };
}
