import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Created by Kazumi on 4:25 PM, 4/6/2016. Powered by JDK 1_8_0 and IntelliJ IDEA 16.
 */

class Management extends JPanel implements ActionListener, ListSelectionListener, KeyListener
{
    private JScrollPane scrollPane;
    private JList<String> keyList;
    private Vector<String> keyArray, resultArray;
    private JLabel lblSearch, lblInfo;
    private JButton btnDelete, btnUndo, btnSave;
    private JTextField fldSearch;
    private File textFile;
    private JPanel upperPanel, lowerPanel, searchPanel;
    private Stack<Object[]> removedStack, searchStack;
    private int listSelection, currentList;

    Management()
    {
        keyArray = Driver.getKeyArray();
        textFile = Driver.getTextFile();

        resultArray = new Vector<>();
        removedStack = new Stack<>();
        searchStack = new Stack<>();
        keyList = new JList<>(this.keyArray);
        scrollPane = new JScrollPane(keyList);
        currentList = 0;

        btnDelete = new JButton("Remove");
        btnUndo = new JButton("Undo");
        btnSave = new JButton("Commit changes");

        fldSearch = new JTextField();
        fldSearch.setColumns(65);

        lblInfo = new JLabel("Current file: " + this.textFile.getAbsolutePath());
        lblSearch = new JLabel("Search: ");
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        upperPanel = new JPanel();
        lowerPanel = new JPanel();
        searchPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());

        searchPanel.add(lblSearch);
        searchPanel.add(fldSearch);
        upperPanel.add(lblInfo);
        upperPanel.add(searchPanel);
        lowerPanel.add(btnDelete);
        lowerPanel.add(btnUndo);
        lowerPanel.add(btnSave);

        add(upperPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(this);
        btnUndo.addActionListener(this);
        btnSave.addActionListener(this);
        fldSearch.addKeyListener(this);
        keyList.addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnDelete)
        {
            if (currentList == 0)
            {
                Object[] info = new Object[2];
                info[0] = listSelection;
                info[1] = keyArray.get(listSelection);
                removedStack.push(info);
                keyArray.remove(listSelection);
                keyList.updateUI();
            } else
            {
                Object[] info = new Object[2];
                info[0] = listSelection;
                info[1] = resultArray.get(listSelection);
                searchStack.push(info);
                keyArray.removeElement(resultArray.get(listSelection));
                resultArray.remove(listSelection);
                keyList.updateUI();
            }
        } else if (e.getSource() == btnUndo)
        {
            if (currentList == 0)
            {
                if (!removedStack.isEmpty())
                {
                    Object[] current = removedStack.pop();
                    keyArray.add((Integer) current[0], (String) current[1]);
                    keyList.updateUI();
                }
            } else
            {
                if (!searchStack.isEmpty())
                {
                    Object[] current = searchStack.pop();
                    resultArray.add((Integer) current[0], (String) current[1]);
                    keyArray.add((Integer) current[0], (String) current[1]);
                    keyList.updateUI();
                }
            }
        } else if (e.getSource() == btnSave)
        {
            try
            {
                FileWriter writer = new FileWriter(textFile);
                for (String str : keyArray)
                {
                    writer.write(str);
                    writer.write("\n");
                }
                writer.close();
            } catch (IOException er)
            {
                lblInfo.setText("Error writing to the file");
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        listSelection = keyList.getSelectedIndex();
    }


    @Override
    public void keyTyped(KeyEvent e)
    {
        if (fldSearch.getText().isEmpty())
        {
            keyList.setListData(keyArray);
            currentList = 0;
        } else
        {
            resultArray.clear();
            searchStack.clear();
            String target = fldSearch.getText();
            resultArray.addAll(keyArray.stream().filter(str -> str.contains(target)).collect(Collectors.toList()));
            keyList.setListData(resultArray);
            currentList = 1;
            keyList.updateUI();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    public JList<String> getKeyList()
    {
        return keyList;
    }
}