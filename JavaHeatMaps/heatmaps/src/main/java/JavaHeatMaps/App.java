package JavaHeatMaps;

import gheat.datasources.*;
import gheat.graphics.ThemeManager;
import org.eclipse.jetty.server.Server;

import java.net.URL;


public class App {
    public static DataManager dataManager = null;
    static URL classpathResource = ClassLoader.getSystemClassLoader().getResource("//");

    public static void main(String[] args) throws Exception {
        if (dataManager == null) {

            ThemeManager.init(classpathResource.getPath() + "res/etc/");
            HeatMapDataSource dataSource = getFileDataSource();

            dataManager = new DataManager(dataSource);
            System.out.println("Checkeo inicio");
        }

        Server server = new Server(8080);
        server.setHandler(new TileHandler());

        server.start();
        server.join();

    }


    private static FileDataSource getFileDataSource() {
        return new FileDataSource(classpathResource.getPath() + "puntos.txt", 2, 1, 0);
    }


    private static QuadTreeDataSource getQuadTreeDataSource() {
        return new QuadTreeDataSource(classpathResource.getPath() + "puntos.txt", 2, 1, 0);
    }
}
