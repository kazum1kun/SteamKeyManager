import javax.swing.*;

/**
 * Created by Kazumi on 11:51 PM, 2/18/2016. Powered by JDK 1_8_0 and IntelliJ IDEA 15.
 */
public class Driver extends JApplet
{
    private KeyWriter keyWriter;
    private JTabbedPane tPane;

    @Override
    public void init()
    {
        keyWriter = new KeyWriter();
        tPane = new JTabbedPane();

        tPane.addTab("Add", keyWriter);

        getContentPane().add(tPane);
        setSize(800, 310);
    }
}
