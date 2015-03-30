package de.timbone.injector;

import de.timbone.injector.api.Injector;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tim on 25.03.2015.
 */
public class GuiInjector extends Injector {

    private JFrame jFrame;
    private JPanel guiPanel, logPanel;
    private JTextArea logText;
    private JFileChooser pluginChooser, extensionChooser, destinationChooser;

    public GuiInjector() {
        jFrame = new JFrame("Plugin Injector " + Main.VERSION);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        guiPanel = new JPanel();
        pluginChooser = new JFileChooser();


        logPanel = new JPanel();
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
        logPanel.setOpaque(true);

        logText = new JTextArea(25, 110);
        logText.setWrapStyleWord(true);
        logText.setEditable(false);
        logText.setFont(Font.getFont(Font.SANS_SERIF));
        ((DefaultCaret) logText.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane logScroll = new JScrollPane(logText);
        logScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        logScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        logPanel.add(logScroll);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Injector", guiPanel);
        tabbedPane.add("Log", logPanel);

        jFrame.getContentPane().add(tabbedPane);
        jFrame.pack();
    }

    public void display() {
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }

    @Override
    protected void log(String msg) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date current = new Date();
        String date = "[" + formatter.format(current) + "] ";
        logText.append(date + splitString(msg, 165) + "\n");
    }

    private String splitString(String s, int x) {
        StringBuffer res = new StringBuffer(s);
        boolean first = true;
        x++;
        for(int i = 0; i <= (s.length()/( x - 1)); i++)
            if(first)
                first = false;
            else
                res.insert(i * x, "\n                  " );
        return res.toString();
    }
}
