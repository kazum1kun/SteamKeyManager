import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

/**
 * Created by Kazumi on 10:32 PM, 2/18/2016. Powered by JDK 1_8_0 and IntelliJ IDEA 15.
 */
public class KeyWriter extends JPanel implements ActionListener, ListSelectionListener
{
    JButton butOpen, butAdd, butSave, butRemove;
    JScrollPane scrollPane;
    JList<String> keyList;
    JTextField fldKey, fldName;
    JLabel lblKey, lblName, lblFile;
    JPanel buttPanel, mainPanel,upperPanel, inputPanel, textPanel, bottomPanel;
    Vector<String> keyArray, pendingArray;
    BufferedReader reader;
    Box initBox;
    CardLayout cl;
    int selectedIndex;
    File textFile;

    KeyWriter()
    {
        setLayout(new CardLayout());

        keyArray = new Vector<>();
        pendingArray = new Vector<>();

        butOpen = new JButton("Open a file...");
        butSave = new JButton("Save changes");
        butAdd = new JButton("Add item");
        butRemove = new JButton("Remove item");

        initBox = new Box(BoxLayout.Y_AXIS);

        lblKey = new JLabel("Key/URL: ");
        lblName = new JLabel("Game: ");
        lblFile = new JLabel("Current file: ");

        fldKey = new JTextField();
        fldName = new JTextField();

        keyList = new JList<>(pendingArray);

        scrollPane = new JScrollPane(keyList);

        buttPanel = new JPanel();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        upperPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new GridLayout(2,2));
        textPanel = new JPanel();
        bottomPanel = new JPanel();

        buttPanel.add(butOpen);
        initBox.add(Box.createVerticalGlue());
        initBox.add(buttPanel);
        initBox.add(Box.createVerticalGlue());

        inputPanel.add(lblName);
        inputPanel.add(fldName);
        inputPanel.add(lblKey);
        inputPanel.add(fldKey);

        upperPanel.add(lblFile, BorderLayout.PAGE_START);
        upperPanel.add(inputPanel, BorderLayout.CENTER);
        upperPanel.add(butAdd, BorderLayout.SOUTH);

        textPanel.add(scrollPane);

        bottomPanel.add(butRemove);
        bottomPanel.add(butSave);

        mainPanel.add(upperPanel);
        mainPanel.add(textPanel);
        mainPanel.add(bottomPanel);

        add(initBox, "INIT");
        add(mainPanel, "MAIN");

        butOpen.addActionListener(this);
        butAdd.addActionListener(this);
        butRemove.addActionListener(this);
        keyList.addListSelectionListener(this);
        butSave.addActionListener(this);

        buttPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        cl = (CardLayout) getLayout();
        cl.show(this,"INIT");
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == butOpen)
        {
            JFileChooser fileChooser = new JFileChooser("D:\\Dropbox\\Documents");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
            int returnVal = fileChooser.showOpenDialog(getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                textFile = fileChooser.getSelectedFile();
                try
                {
                    //scanner = new Scanner(textFile).useDelimiter("\n");
                    lblFile.setText("Current file: "+ fileChooser.getSelectedFile().getAbsolutePath());
                    reader = new BufferedReader(new FileReader(textFile));
                    String tempLine;
                    while ((tempLine = reader.readLine())!=null)
                    {
                        keyArray.addElement(tempLine);
                    }
                    reader.close();
                }catch (FileNotFoundException er)
                {
                    lblFile.setText("File not found!");
                }
                catch (IOException er)
                {
                    lblFile.setText("Error reading the file");
                }finally
                {
                    cl.next(this);
                }
            }
        }

        else if (e.getSource() == butAdd)
        {
            pendingArray.addElement(fldKey.getText()+ "; " +fldName.getText());
            keyList.updateUI();
            fldKey.setText("");
            fldName.setText("");
        }

        else if (e.getSource() == butRemove)
        {
            pendingArray.remove(selectedIndex);
            keyList.updateUI();
        }
        else if (e.getSource() == butSave)
        {
            for (String str : pendingArray)
            {
                keyArray.addElement(str);
            }
            try
            {
                FileWriter writer = new FileWriter(textFile);
                for (String str : keyArray)
                {
                    writer.write(str);
                    writer.write("\n");
                }
                writer.close();
            }catch (IOException er)
            {
                lblFile.setText("Error writing to the file");
            }finally
            {
                pendingArray.removeAllElements();
                keyList.updateUI();
            }
        }

    }

    public void valueChanged(ListSelectionEvent e)
    {
        selectedIndex = keyList.getSelectedIndex();
    }
}
