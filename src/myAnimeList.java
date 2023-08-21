import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;

public class myAnimeList extends JFrame {
    private JPanel Graphics;
    private JPanel buttonPanel;
    private JButton myListCardButton;
    private JPanel cardLayoutPanel;
    private JPanel myListPanel;
    private JButton top100CardButton;
    private JTable top100Table;
    private JPanel top100Panel;
    private JPanel topBarPanel;
    private JButton quitButton;
    private JScrollPane top100ScrollPane;
    private JScrollPane myListScrollPane;
    private JTextField animeNameField;
    private JTextField animeRateField;
    private JButton addAnimeButton;
    private JTable myListTable;
    private JLabel myAnimeAddSuccess;
    private JComboBox removeComboBox;
    private JButton removeButton;
    private JButton refreshRatingsButton;
    private JButton refreshTop100ListButton;

    public Color grayBackground = new Color(40, 40, 40);
    public Color blackBackground = new Color(18, 18, 18);

    ArrayList<String> myAnimeList;
    HashMap<String, Double> unsortedMap = new HashMap<>();

    private int count = 1;

    public myAnimeList() {

        createTop100Table();
        //updateTop100List();
        createMyList();

        Border empty = new EmptyBorder(0, 0, 0, 0);
        top100ScrollPane.setBorder(empty);
        top100ScrollPane.setBackground(grayBackground);

        myListScrollPane.setBorder(empty);
        myListScrollPane.setBackground(grayBackground);

        myListCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPage(myListPanel);
            }
        });
        top100CardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPage(top100Panel);
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        addAnimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = animeNameField.getText();
                String rate = animeRateField.getText();
                if (fieldCheck(animeNameField) && fieldCheck(animeRateField)) {
                    addMyList(name, rate);
                    myAnimeAddSuccess.setText("Succesfully Added");
                } else {
                    myAnimeAddSuccess.setText("Please Fill In All Fields.");
                }

            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                removeMyList();
                createMyList();
            }
        });
        refreshTop100ListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTop100Table();
                createTop100Table();
            }
        });
        refreshRatingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMyList();
                createMyList();
            }
        });
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("MyAnimeList BETA 1.0");
        frame.setContentPane(new myAnimeList().Graphics);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);

    }

    //***************************************************************************
    //***************************************************************************
    //function to switch card layout
    //***************************************************************************
    //***************************************************************************

    private void switchPage(JPanel x) {

        cardLayoutPanel.removeAll();
        cardLayoutPanel.add(x);
        cardLayoutPanel.repaint();
        cardLayoutPanel.revalidate();
    }

    //***************************************************************************
    //***************************************************************************
    //Function to check if textField is empty
    //***************************************************************************
    //***************************************************************************

    private boolean fieldCheck(JTextField x) {
        if (x.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    //***************************************************************************
    //***************************************************************************
    //Function to refresh top100table by rewriting Top100List.txt
    //***************************************************************************
    //***************************************************************************

    private void refreshTop100Table(){

        try {

            //*****************************************
            //Sets up a FOS and gets arraylists of names,rates, etc... for Top100List
            //*****************************************
            FileOutputStream fout = new FileOutputStream("Top100List.txt");

            ArrayList<String> list = webSearch.nameList();
            ArrayList<String> ratings = webSearch.rateList();
            ArrayList<String> episodes = webSearch.epList();
            ArrayList<String> dates = webSearch.dateList();
            ArrayList<String> members = webSearch.memberList();

            //*****************************************
            //Creates string named 'data' and adds info separated by spacing
            //*****************************************

            String data = "";

            for(String L:list){
                data += " " + L;
            }
            for(String r: ratings){
                data+= "  " + r;
            }
            for(String e: episodes){
                data += "  " + e;
            }
            for(String d: dates){
                data += "  " + d;
            }
            for(String m: members){
                data += "  " + m;
            }

            //*****************************************
            //Converts 'data' to byte array named 'b' to write to Top100List.txt using FOS
            //*****************************************

            byte[] b = data.getBytes(StandardCharsets.UTF_8);
            fout.write(b);

        }catch(Exception ex){
        }



    }

    //***************************************************************************
    //***************************************************************************
    //Function creates Top100table by reading Top100List.txt and sets style
    //***************************************************************************
    //***************************************************************************

    private void createTop100Table() {

        //*****************************************
        //Creates arraylists to store show data
        //*****************************************

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> ratings = new ArrayList<>();
        ArrayList<String> episodes = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> members = new ArrayList<>();

        try{

            //*****************************************
            //Uses FIS to read the bytes from Top100List.txt and save to 'readInfo'
            //*****************************************

            String readInfo = "";
            FileInputStream fin = new FileInputStream("Top100List.txt");
            int j;
            while((j = fin.read())!= -1){
                readInfo += (char)j;
            }

            //*****************************************
            //Converts string 'readInfo' into arraylist of info in order
            //*****************************************

            String data = readInfo;
            String[] tempData = data.split("  ");
            ArrayList<String> info = new ArrayList<>();
            Collections.addAll(info, tempData);

            //*****************************************
            //Adds info to correct arraylists following the order
            //*****************************************

            for (int i = 1; i < info.size(); i ++){
                if(i <= 100){
                    list.add(info.get(i));
                } else if(i <= 200 && i >= 100){
                    ratings.add(info.get(i));
                }else if(i <= 300 && i >= 200){
                    episodes.add(info.get(i));
                }else if(i <= 400 && i >= 300){
                    dates.add(info.get(i));
                }else if(i <= 500 && i >= 400){
                    members.add(info.get(i));
                }
            }
        } catch(Exception ex){
        }

        //*****************************************
        //Sets up rows and collumns for Top100List's jTable
        //*****************************************

        DefaultTableModel tblModel = (DefaultTableModel) top100Table.getModel();
        tblModel.setRowCount(0);
        tblModel.setColumnCount(0);
        tblModel.addColumn("#");
        tblModel.addColumn("Name");
        tblModel.addColumn("Rating");
        tblModel.addColumn("Episodes");
        tblModel.addColumn("Run Date");
        tblModel.addColumn("Members On Site");

        //*****************************************
        //Takes info for each show and adds it to 'Data' witch is added as a new row to the jTable
        //*****************************************

        int count = 0;
        String countStr;
        for (int i = 0; i < list.size(); i++) {
            count++;
            countStr = Integer.toString(count);

            String[] data = {countStr, list.get(i), ratings.get(i), episodes.get(i), dates.get(i), members.get(i)};
            tblModel.addRow(data);
        }

        //*****************************************
        //Sets size of columns, changes color of jTable, and centers text
        //*****************************************

        TableColumnModel columns = top100Table.getColumnModel();
        columns.getColumn(0).setMaxWidth(50);
        columns.getColumn(0).setMinWidth(50);
        columns.getColumn(1).setMinWidth(400);
        columns.getColumn(1).setMaxWidth(400);
        columns.getColumn(2).setMinWidth(100);
        columns.getColumn(2).setMaxWidth(100);
        columns.getColumn(3).setMinWidth(150);
        columns.getColumn(3).setMaxWidth(150);
        columns.getColumn(4).setMaxWidth(200);
        columns.getColumn(4).setMinWidth(200);
        columns.getColumn(5).setMinWidth(150);
        columns.getColumn(5).setMaxWidth(150);

        JTableHeader header = top100Table.getTableHeader();
        header.setBackground(blackBackground);
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);
        columns.getColumn(5).setCellRenderer(centerRenderer);
    }


    //***************************************************************************
    //***************************************************************************
    //Function creates MyList JTable by reading from MuList.txt and setting style
    //***************************************************************************
    //***************************************************************************

    public void createMyList() {
        //****************************************************
        //create default model and Arraylists to go into table
        //****************************************************
        DefaultTableModel tblModel = (DefaultTableModel) myListTable.getModel();

        tblModel.setRowCount(0);
        tblModel.setColumnCount(0);
        count = 1;

        myAnimeList = new ArrayList<>();
        ArrayList<String> myAnimeRate = new ArrayList<String>();
        ArrayList<String> webRate = new ArrayList<String>();

        try {
            //***************************************************************************
            //reads from .txt file to add saved shows and rates to appropriate arraylists
            //***************************************************************************

            String readInfo = "";
            FileInputStream fin = new FileInputStream("MyList.txt");
            int j;
            while((j = fin.read())!= -1){
                readInfo += (char)j;
            }

            String[] tempData = readInfo.split("  ");
            ArrayList<String> info = new ArrayList<>();
            Collections.addAll(info, tempData);
            System.out.println("Info == " + info);

            int t = 0;
            for(int i = 0; i < info.size(); i++){
                t++;
                if(t == 1){
                    myAnimeList.add(info.get(i));
                } else if(t == 2){
                    myAnimeRate.add(info.get(i));
                } else if (t == 3){
                    webRate.add(info.get(i));
                }

                if(t >= 3){
                    t = 0;
                }
            }

        //******************************************
        //Finally adds columns to table and add rows
        //******************************************
        tblModel.addColumn("#");
        tblModel.addColumn("Anime");
        tblModel.addColumn("My Rating");
        tblModel.addColumn("Website Rating");

        for (int i = 0; i < myAnimeList.size(); i++) {
            String countStr = Integer.toString(count);
            String[] data = {countStr, myAnimeList.get(i), myAnimeRate.get(i), webRate.get(i)};
            tblModel.addRow(data);
            count++;
        }
        j = count;
        for(int i = j; i < 41; i ++){
            String countStr = Integer.toString(count);
            String[] data = {countStr};
            tblModel.addRow(data);
            count++;
        }

        removeComboBoxUpdate();

        } catch (Exception ex) {
        }

        TableColumnModel columns = myListTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(50);
        columns.getColumn(0).setMinWidth(50);
        columns.getColumn(1).setMinWidth(400);
        columns.getColumn(1).setMaxWidth(400);
        columns.getColumn(2).setMinWidth(100);
        columns.getColumn(2).setMaxWidth(100);
        columns.getColumn(3).setMinWidth(150);
        columns.getColumn(3).setMaxWidth(150);


        JTableHeader header = myListTable.getTableHeader();
        header.setBackground(blackBackground);
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(2).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
    }

    //***************************************************************************
    //***************************************************************************
    //Function sorts values for the MyList table by ratings
    //***************************************************************************
    //***************************************************************************

    public Map<String, Double> sortByValue(boolean order) {
        //convert HashMap into List
        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortedMap.entrySet());
        //sorting the list elements
        Collections.sort(list, new Comparator<Entry<String, Double>>() {
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                if (order) {
                    //compare two object and return an integer
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        //creates the sorted HashMap to return
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    //***************************************************************************
    //***************************************************************************
    //Function updates MyList jTable by rewriting the web stats saved and re sorting by personal rating
    //***************************************************************************
    //***************************************************************************

    public void updateMyList(){

        try {

            /*
            Creating Arraylists for info
             */


            ArrayList <String>list = new ArrayList();
            ArrayList <String> rate = new ArrayList();
            ArrayList <String> webRate = new ArrayList();

            String writeInfo = "";
            String readInfo = "";
            FileInputStream fin = new FileInputStream("MyList.txt");
            int j;
            while ((j = fin.read()) != -1) {
                readInfo += (char) j;
            }

            String[] tempData = readInfo.split("  ");
            ArrayList<String> info = new ArrayList<>();
            Collections.addAll(info, tempData);
            System.out.println("Info == " + info);
            int t = 0;

            for(int i = 0; i < info.size(); i++){
                t++;
                if(t == 1){
                    list.add(info.get(i));
                } else if(t == 2){
                    rate.add(info.get(i));
                }

                if(t >= 3){
                    t = 0;
                }
            }

            //*****************************************
            //Sort arraylists based on personal rating
            //*****************************************

        for (int i = 0; i < list.size(); i++) {
            unsortedMap.put(list.get(i), Double.parseDouble(rate.get(i)));
        }
        //System.out.println("Before " + unsortedMap);

        Map<String, Double> sortedMap = sortByValue(false);
        //System.out.println("After " + sortedMap);

        list = new ArrayList<>(sortedMap.keySet());
        rate = new ArrayList<String>();
        ArrayList<Double> temp = new ArrayList<>(sortedMap.values());
        for(int i =0; i < temp.size(); i++){
        rate.add(Double.toString(temp.get(i)));
        }

            for(int i = 0; i < list.size(); i++){
                webRate.add(webSearch.getRating((String) list.get(i)));
            }

            System.out.println(list);
            System.out.println(rate);
            System.out.println(webRate);

            int c = 0;
            for(int i = 0; i < list.size(); i++){
                for(j = 1; j <= 3; j++) {
                    if (j == 1) {
                        writeInfo += list.get(i) + "  ";
                    } else if (j == 2) {
                        writeInfo += rate.get(i) + "  ";
                    } else {
                        writeInfo += webRate.get(i) + "  ";
                    }
                }
            }

            System.out.println("Write data == " + writeInfo);

            FileOutputStream fout = new FileOutputStream("MyList.txt");

            byte[] a = writeInfo.getBytes(StandardCharsets.UTF_8);

            fout.write(a);

            fout.close();




        } catch(Exception ex){

        }


    }

    //***************************************************************************
    //***************************************************************************
    //function adds user input to MyList.txt
    //***************************************************************************
    //***************************************************************************


    public void addMyList(String name, String rate) {

        try {

            FileOutputStream fout = new FileOutputStream("MyList.txt", true);

            String n = name + "  ";
            String r = rate + "  ";
            String wr = webSearch.getRating(name) + "  ";

            byte[] a = n.getBytes(StandardCharsets.UTF_8);
            byte[] b = r.getBytes(StandardCharsets.UTF_8);
            byte[] c = wr.getBytes(StandardCharsets.UTF_8);

            fout.write(a);
            fout.write(b);
            fout.write(c);

            fout.close();

            createMyList();


        } catch (Exception ex) {
        }
    }

    //***************************************************************************
    //***************************************************************************
    //Function removes a show and its data from the MyList.txt saved info
    //***************************************************************************
    //***************************************************************************

    public void removeMyList() {

        try {
            FileInputStream fin = new FileInputStream("MyList.txt");

            String readInfo = new String();
            int j;
            while ((j = fin.read()) != -1) {
                readInfo += (char) j;
            }

            String[] tempData = readInfo.split("  ");
            ArrayList<String> data = new ArrayList<>();
            Collections.addAll(data, tempData);

            String removeShow = removeComboBox.getSelectedItem().toString();
            String line = "";

            data.remove(data.indexOf(removeShow)+2);
            data.remove(data.indexOf(removeShow)+1);
            data.remove(data.indexOf(removeShow));

            String writeInfo = new String();

            for(int i = 0; i < data.size(); i++){
                        writeInfo += data.get(i) + "  ";

            }

            FileOutputStream fout = new FileOutputStream("MyList.txt");

            byte[] a = writeInfo.getBytes(StandardCharsets.UTF_8);
            fout.write(a);
            fout.close();

        } catch (Exception ex) {

        }


    }

    //***************************************************************************
    //***************************************************************************
    //Function updates the delete combo box when show is deleted
    //***************************************************************************
    //***************************************************************************

    public void removeComboBoxUpdate() {

        removeComboBox.removeAllItems();

        for (int i = 0; i < myAnimeList.size(); i++) {
            removeComboBox.addItem(myAnimeList.get(i));
        }

    }


    public void pause(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println("Error in running rotation animation!");
            System.exit(-1);
        }
    }
}
