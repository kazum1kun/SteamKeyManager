import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

/**
 * Created by Kazumi on 10:32 PM, 2/18/2016. Powered by JDK 1_8_0 and IntelliJ IDEA 15.
 */
class KeyWriter extends JPanel implements ActionListener, ListSelectionListener
{
    private JButton butAdd, butSave, butRemove;
    private JScrollPane scrollPane;
    private JList<String> keyList;
    private JTextField fldKey, fldName;
    private JLabel lblKey, lblName, lblFile, lblPending;
    private JPanel upperPanel, inputPanel, bottomPanel;
    private JSeparator separator;
    private Vector<String> keyArray, pendingArray;
    private int selectedIndex;
    private File textFile;
    private Management mgmt;

    KeyWriter(Management mgmt)
    {
        keyArray = Driver.getKeyArray();
        textFile = Driver.getTextFile();
        this.mgmt = mgmt;

        pendingArray = new Vector<>();

        butSave = new JButton("Commit changes");
        butAdd = new JButton("Add item");
        butRemove = new JButton("Remove item");
        butAdd.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblKey = new JLabel("Key/URL: ");
        lblName = new JLabel("Game: ");
        lblFile = new JLabel("Current file: " + textFile.getAbsolutePath());
        lblPending = new JLabel("Keys to be added: ");
        lblPending.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFile.setAlignmentX(Component.CENTER_ALIGNMENT);

        fldKey = new JTextField();
        fldName = new JTextField();

        separator = new JSeparator();

        keyList = new JList<>(pendingArray);

        scrollPane = new JScrollPane(keyList);
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        inputPanel = new JPanel(new GridLayout(2,2));
        bottomPanel = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        inputPanel.add(lblName);
        inputPanel.add(fldName);
        inputPanel.add(lblKey);
        inputPanel.add(fldKey);

        upperPanel.add(lblFile);
        upperPanel.add(inputPanel);
        upperPanel.add(butAdd);

        bottomPanel.add(butRemove);
        bottomPanel.add(butSave);

        add(upperPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(separator);
        add(lblPending);
        add(scrollPane);
        add(bottomPanel);


        butAdd.addActionListener(this);
        butRemove.addActionListener(this);
        keyList.addListSelectionListener(this);
        butSave.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        /*
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

        }*/

        if (e.getSource() == butAdd)
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
                mgmt.getKeyList().updateUI();
            }
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        selectedIndex = keyList.getSelectedIndex();
    }
}
