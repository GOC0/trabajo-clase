import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;



public class Main {


    public static void main(String[] args) {

        /**
         * DTO
         * @param nombre
         * @param contrasena
         */

        record Usuario(String nombre, String contrasena){

        }

        var app = Javalin.create(/*config*/config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress = false;
                staticFileConfig.aliasCheck = null;
            });
        })
                    .start(8000);


        app.before("/*", ctx -> {

            String path = ctx.path();

            // Permitir login, procesamiento y archivos estáticos
            if (path.equals("/login") ||
                    path.equals("/procesar-login") ||
                    path.endsWith(".html") ||
                    path.endsWith(".css") ||
                    path.endsWith(".js") ||
                    path.startsWith("/images")) {
                return;
            }

            Usuario usuario = ctx.sessionAttribute("usuario");

            if (usuario == null) {
                ctx.redirect("/login");
            }
        });

        app.post("/procesar-login", ctx -> {
            String nombre = ctx.formParam("usuario");
            String contrasena = ctx.formParam("password");

            if ("admin".equals(nombre) && "admin".equals(contrasena)) {
                ctx.sessionAttribute("usuario", new Usuario(nombre, contrasena));
                ctx.redirect("/");
            } else {
                ctx.redirect("/login");
            }
        });





        // crear un endpoint
        app.get("/", ctx -> ctx.redirect("/index.html"));
        app.get("/login", ctx -> ctx.redirect("/login.html"));




    }

}
