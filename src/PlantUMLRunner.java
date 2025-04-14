import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlantUMLRunner
{
    // Statyczna zmienna przechowująca ścieżkę do pliku .jar PlantUML-a
    private static String jarPath;


    //Getter
    public String getJarPath() {return jarPath;}


    // Setter — ustawia ścieżkę do pliku jar; metoda jest statyczna, bo jarPath też jest statyczne
    public static void setJarPath(String path)
    {
        jarPath = path;
    }
    //Generuje plik z diagramem UML
    public static void generateDiagram(String umlText, String outputDir,String outputFileName)throws IOException,InterruptedException
    {
        // Sprawdza, czy ścieżka do .jar została wcześniej ustawiona
        if(jarPath==null || jarPath.isEmpty())
        {
            throw new IllegalStateException("Ścieżka do pliku .jar nie została ustawiona.");
        }

        // Tworzy obiekt reprezentujący folder wyjściowy
        File dir=new File(outputDir);

        if(!dir.exists())
        {
            dir.mkdirs();// mkdirs tworzy cały łańcuch folderów
        }

        // Tworzy plik .puml w wybranym folderze, np. "diagram.puml"
        File umlFile=new File(dir,outputFileName+".puml");

        // Zapisuje tekst UML do tego pliku
        try(FileWriter writer=new FileWriter(umlFile))
        {
            writer.write(umlText);
        }
    }
}
