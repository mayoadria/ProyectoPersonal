package adria.mayo.proyectopersonal.service;

import adria.mayo.proyectopersonal.Excepciones.Vehicle.EncontrarVehicleException;
import adria.mayo.proyectopersonal.entity.Reserva;
import adria.mayo.proyectopersonal.entity.Vehiculo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EnviarCorreo {

    private final JavaMailSender mailSender;
    private final VehicleService vehicleService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EnviarCorreo(JavaMailSender mailSender, VehicleService vehicleService) {
        this.mailSender = mailSender;
        this.vehicleService = vehicleService;
    }

    private MimeMessageHelper crearMensaje(String correo, String asunto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(correo);
        helper.setSubject(asunto);
        return helper;
    }


    private String plantillaBase(String tituloColor, String tituloTexto, String cuerpoHtml, String bordeColor) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: left; " +
                "box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border: 2px solid " + bordeColor + ";'>" +
                "<h2 style='color: " + tituloColor + "; font-size: 24px;'>" + tituloTexto + "</h2>" +
                cuerpoHtml +
                "</div>" +
                "</body></html>";
    }

    private String datosVehiculoHtml(Vehiculo vehiculo) {
        return "<h3 style='color: #01B8AA;'>🔍 Dades del vehicle:</h3>" +
                "<ul style='font-size: 15px; color: #333;'>" +
                "<li><strong>Matrícula:</strong> " + vehiculo.getMatricula() + "</li>" +
                "<li><strong>Marca:</strong> " + vehiculo.getMarca() + "</li>" +
                "<li><strong>Model:</strong> " + vehiculo.getModel() + "</li>" +
                "<li><strong>Color:</strong> " + vehiculo.getColor() + "</li>" +
                "</ul>";
    }

    private String datosReservaHtml(Reserva reserva, Vehiculo vehiculo) {
        return "<h3 style='color: #01B8AA;'>📅 Dades de la reserva:</h3>" +
                "<ul style='font-size: 15px; color: #333;'>" +
                "<li><strong>Data d'inici:</strong> " + reserva.getFechaInici() + "</li>" +
                "<li><strong>Data de finalització:</strong> " + reserva.getFechaFinal() + "</li>" +
                "<li><strong>Hora d'inici:</strong> " + reserva.getHoraInici() + "</li>" +
                "<li><strong>Hora de finalització:</strong> " + reserva.getHoraFin() + "</li>" +
                "<li><strong>Lloc:</strong> " + vehiculo.getCreador().getDireccio() + "</li>" +
                "<li><strong>Preu total:</strong> " + reserva.getPreuComplert() + " €</li>" +
                "</ul>";
    }

    public void enviarCorreo(String correo, String token) {
        try{
            MimeMessageHelper helper = crearMensaje(correo,"OpenRoad - Token Password Reset");

            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center; "
                    +
                    "box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border: 2px solid #01ECE9;'>" +
                    "<h2 style='color: #50E820; font-size: 24px;'>🔐 Recuperació de Contrasenya</h2>" +
                    "<p style='color: #333; font-size: 16px;'>Has sol·licitat restablir la teva contrasenya. Fes servir el següent codi:</p>"
                    +
                    "<div style='display: inline-block; padding: 10px 20px; background: #DDCB01; color: white; font-size: 22px;"
                    +
                    "font-weight: bold; border-radius: 5px; letter-spacing: 2px;'>" +
                    "<span>" + token + "</span>" +
                    "</div>" +
                    "<p style='color: #555; margin-top: 15px; font-size: 14px;'>Si no has sol·licitat aquest canvi, ignora aquest correu.</p>"
                    +
                    "</div>" +
                    "</body></html>";

            helper.setText(htmlContent, true); // Indicamos que es HTML

            mailSender.send(helper.getMimeMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }




    public void enviarCorreoReservaA(String correo, String matricula, Reserva reserva) {
        try {
            Vehiculo vehiculo = vehicleService.buscarVehiculo(matricula);
            if (vehiculo == null) throw new EncontrarVehicleException("Vehiculo no encontrado");

            MimeMessageHelper helper = crearMensaje(correo, "OpenRoad - Confirmació de reserva");

            String cuerpo = "<p style='font-size: 16px; color: #333;'>Hola,</p>" +
                    "<p style='font-size: 16px; color: #333;'>Has fet una reserva amb èxit. A continuació tens els detalls:</p>" +
                    datosVehiculoHtml(vehiculo) +
                    datosReservaHtml(reserva, vehiculo) +
                    "<p style='color: #555; margin-top: 20px; font-size: 14px;'>Si no has fet aquesta reserva, pots ignorar aquest correu.</p>";

            String htmlContent = plantillaBase("#50E820", "🚗 Reserva confirmada", cuerpo, "#01ECE9");

            helper.setText(htmlContent, true);
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarCorreoReservaCancelada(String correo, String matricula, Reserva reserva) {
        try {
            Vehiculo vehiculo = vehicleService.buscarVehiculo(matricula);
            if (vehiculo == null) throw new EncontrarVehicleException("Vehiculo no encontrado");

            MimeMessageHelper helper = crearMensaje(correo, "OpenRoad - Cancel·lació de la reserva");

            String cuerpo = "<p style='font-size: 16px; color: #333;'>Hola,</p>" +
                    "<p style='font-size: 16px; color: #333;'>La teva reserva ha estat cancel·lada. A continuació tens els detalls, si tens qualsevol dubte, respon a aquest correu:</p>" +
                    datosVehiculoHtml(vehiculo).replace("#01B8AA", "#D32F2F") +  // Cambiar color de encabezados
                    datosReservaHtml(reserva, vehiculo).replace("#01B8AA", "#D32F2F") +
                    "<p style='color: #555; margin-top: 20px; font-size: 14px;'>Si creus que s'ha produit un error, si us plau contacta amb el nostre servei d'atenció.</p>";

            String htmlContent = plantillaBase("#FF4C4C", "🚫 Reserva cancel·lada", cuerpo, "#FF4C4C");

            helper.setText(htmlContent, true);
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
