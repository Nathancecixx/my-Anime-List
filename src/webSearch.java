import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class webSearch {

    public static ArrayList<String> nameList() {

        String str = new String();
        String site = new String("https://myanimelist.net/topanime.php?limit=0");;
        URL webpage = null;
        URLConnection conn = null;
        int count = 0;
        ArrayList<String> list = new ArrayList<String>();

        try{
            while (count < 2) {
                    webpage = new URL(site);
                    conn = webpage.openConnection();
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
                    BufferedReader br = new BufferedReader(reader);
                    String line = "";
                        while ((line=br.readLine()) != null) {
                            //line = br.readLine();

                            if (line.contains("<img width=\"50\" height=\"70\" alt=\"Anime: ")) {
                                str += line;
                                str = str.substring(str.indexOf(":") + 1, str.lastIndexOf("class") - 2);
                                if (str.contains("&amp;#039;"))
                                    str = str.replace("&amp;#039;", "'");

                                list.add(str);
                                str = null;
                            }
                        }
                        //System.out.println(list.size());
                        count ++;
                        site = "https://myanimelist.net/topanime.php?limit=50";
                    }
        } catch(Exception ex){

        }
        return list;
    }

    public static ArrayList<String> rateList(){

        String strRev = new String();
        String site = new String("https://myanimelist.net/topanime.php?limit=0");
        URL webpage = null;
        URLConnection conn = null;
        ArrayList<String> ratings = new ArrayList<String>();
        int count =0;

        try{
            while (count < 2) {
                webpage = new URL(site);
                conn = webpage.openConnection();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                while ((line=br.readLine()) != null) {

                    if (line.contains("text on score-label")) {
                        strRev += line;
                        strRev = strRev.substring(strRev.lastIndexOf("-"), strRev.lastIndexOf("/"));
                        strRev = strRev.substring(strRev.indexOf(">") + 1, strRev.indexOf("<"));
                        ratings.add(strRev);
                        strRev = null;
                    }
                }

                count ++;
                site = "https://myanimelist.net/topanime.php?limit=50";
            }

        } catch(Exception ex){

        }
        return ratings;
    }


    public static ArrayList<String> epList(){

        String strEp = new String();
        String site = new String("https://myanimelist.net/topanime.php?limit=0");
        URL webpage = null;
        URLConnection conn = null;
        ArrayList<String> episodes = new ArrayList<String>();
        int count =0;
        int c=0;

        try{
            while (count < 2) {
                webpage = new URL(site);
                conn = webpage.openConnection();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                while ((line = br.readLine()) != null) {

                    if (line.contains("eps")) {
                        strEp += line;
                        if (c == 0) {
                            strEp = strEp.substring(7, strEp.lastIndexOf("<"));
                        } else {
                            strEp = strEp.substring(12, strEp.lastIndexOf("<"));
                        }
                        episodes.add(strEp);
                        strEp = null;
                        c++;
                    }
                }

                count ++;
                site = "https://myanimelist.net/topanime.php?limit=50";
            }

        } catch(Exception ex){

        }
        return episodes;
    }


    public static ArrayList<String> dateList() {

        String str = new String();
        String site = new String("https://myanimelist.net/topanime.php?limit=0");;
        URL webpage = null;
        URLConnection conn = null;
        boolean flag = false;
        int c = 0;
        int count = 0;
        ArrayList<String> dates = new ArrayList<String>();

        try{
            while (count < 2) {
                webpage = new URL(site);
                conn = webpage.openConnection();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                while ((line=br.readLine()) != null) {

                    if (line.contains("eps")) {
                        flag = true;

                    } else if (flag == true && c ==0){
                        str = line;
                        str = str.substring(8, str.lastIndexOf("<"));

                        dates.add(str);
                        str = null;
                        flag = false;
                        c++;
                    } else if (flag == true){
                        str = line;
                        str = str.substring(8, str.lastIndexOf("<"));

                        dates.add(str);
                        str = null;
                        flag = false;

                    }
                }
                //System.out.println(dates.size());
                //System.out.println(dates);
                count ++;
                site = "https://myanimelist.net/topanime.php?limit=50";
            }
        } catch(Exception ex){

        }
        return dates;
    }


    public static ArrayList<String> memberList() {

        String str = new String();
        String site = new String("https://myanimelist.net/topanime.php?limit=0");;
        URL webpage = null;
        URLConnection conn = null;
        boolean flag = false;
        int c = 0;
        int count = 0;
        ArrayList<String> members = new ArrayList<String>();

        try{
            while (count < 2) {
                webpage = new URL(site);
                conn = webpage.openConnection();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                while ((line=br.readLine()) != null) {

                    if (line.contains("members") && line.contains(",")) {

                        str = line;
                        str = str.substring(8, str.lastIndexOf(" "));

                        members.add(str);
                        str = null;
                        flag = false;
                    }
                }
                //System.out.println(members.size());
                //System.out.println(members);
                count ++;
                site = "https://myanimelist.net/topanime.php?limit=50";
            }
        } catch(Exception ex){

        }
        return members;
    }

    public static String getRating (String name){

        String [] nameSplit = name.split(" ");
        String str = new String();
        String site = new String("https://myanimelist.net/search/all?cat=all&q=");;
        URL webpage = null;
        URLConnection conn = null;
        int flag = 0;
        int count = 0;
        String webRate = "";

        for (int i = 0; i < nameSplit.length; i++){
            if(count==0){
                site += nameSplit[i];
            }else{
                site += "%20" + nameSplit[i];
            }
            count++;

        }
        System.out.println(site);

        try {

            webpage = new URL(site);
            conn = webpage.openConnection();
            InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF8");
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            boolean continuee = false;
            while (continuee == false && (line=br.readLine()) != null){

                if (flag == 0 && line.contains(name)){
                    flag = 1;
                } else if (flag == 1 && line.contains("Scored")){
                    flag = 5;
                    webRate = line.substring(6, line.indexOf("<"));
                    continuee = true;
                }

            }
            return webRate;
        } catch (Exception ex){
            return "Anime Not Found";
        }

    }

}
