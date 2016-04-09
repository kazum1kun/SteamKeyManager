import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Vector;

/**
 * Created by Kazumi on 11:51 PM, 2/18/2016. Powered by JDK 1_8_0 and IntelliJ IDEA 15.
 */
public class Driver extends JApplet
{
    private static File textFile;
    private BufferedReader reader;
    private static JLabel lblFile = new JLabel();
    private static Vector<String> keyArray = new Vector<>();

    @Override
    public void init()
    {
        fileInit();

        Management mgmt = new Management();
        KeyWriter keyWriter = new KeyWriter(mgmt);
        JTabbedPane tPane = new JTabbedPane();

        tPane.addTab("Add Keys", keyWriter);
        tPane.add("Management", mgmt);
        getContentPane().add(tPane);
        setSize(800, 310);
    }

    private void fileInit()
    {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Kazumi\\Desktop");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnVal = fileChooser.showOpenDialog(getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            textFile = fileChooser.getSelectedFile();
            try
            {
                //scanner = new Scanner(textFile).useDelimiter("\n");
                reader = new BufferedReader(new FileReader(textFile));
                String tempLine;
                while ((tempLine = reader.readLine()) != null)
                {
                    keyArray.addElement(tempLine);
                }
                reader.close();
            } catch (FileNotFoundException er)
            {
                lblFile.setText("File not found!");
            } catch (IOException er)
            {
                lblFile.setText("Error reading the file");
            }
        }
    }

    public static File getTextFile()
    {
        return textFile;
    }

    public static JLabel getLblFile()
    {
        return lblFile;
    }

    public static Vector<String> getKeyArray()
    {
        return keyArray;
    }
}
