package com.example.ziku.hotshot.tables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.ziku.hotshot.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Ziku on 2016-11-28.
 */

@Table(name = ActiveInfo.INFO_TABLE)
public class ActiveInfo extends Model implements Comparable<ActiveInfo>{

    public static final String INFO_TABLE = "info_table";
    public static final String INFO_NAME = "info_name";
    public static final String INFO_DATE = "info_date";
    public static final String INFO_VERSION = "info_version";
    public static final String INFO_CONTENT = "info_content";

    @Column(name = INFO_VERSION)
    public String version;

    @Column(name = INFO_DATE)
    public Date infoDate;

    @Column(name = INFO_NAME)
    public String infoName;

    @Column(name = INFO_CONTENT)
    public String infoContent;

    public ActiveInfo(){super();}

    public ActiveInfo(String version, Date infoDate, String infoName, String infoContent) {
        this.version = version;
        this.infoDate = infoDate;
        this.infoName = infoName;
        this.infoContent = infoContent;
    }

    @Override
    public int compareTo(ActiveInfo activeInfo) {
        long id1 = activeInfo.getId();
        long id2 = this.getId();
        if(id1 > id2){
            return 1;
        } else {return -1;}
    }

    public static List<ActiveInfo> ReturnAllInfoItem(){
        List<ActiveInfo> activeInfosList;
        activeInfosList = new Select().from(ActiveInfo.class).execute();
        Collections.sort(activeInfosList);
        return activeInfosList;
    }

    public static void AddElementifDoesNotExist(ActiveInfo activeInfo){
        List<ActiveInfo> activeInfos = ReturnAllInfoItem();
        boolean notExist = true;
        for(ActiveInfo active : activeInfos){
            if(active.infoName.equals(activeInfo.infoName))
                notExist = false;
        }
        if(notExist)
            activeInfo.save();
    }

    public static void AddAllinterestingElement(){
        String date = "2016-12-12 00:00:00.0";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date1;
        try {
            date1 = simpleDateFormat.parse(date);
        }catch (ParseException ex){date1 = new Date();}
        ActiveInfo info1 = new ActiveInfo(BuildConfig.VERSION_NAME,date1,"Podziękowania","Specjalne podziękowania należą się grupie " +
                "osób, która podjeła się niełatwego zadania, testowania pierwszych wersji aplikacji, oraz udzielała cennych wskazówek i porad:\n\n" +
                "\t\t- Piotr Małecki-Jurek\n" +
                "\t\t- Piotr Lampa\n" +
                "\t\t- Anita Grezel\n" +
                "\t\t- Kamil Bajor\n" +
                "\t\t- Klaudia Czerwiec\n" +
                "\t\t- Klaudia Nita");
        AddElementifDoesNotExist(info1);

        ActiveInfo info2 = new ActiveInfo(BuildConfig.VERSION_NAME,date1,"O Aplikacji","W zigiełku pracy i wykonywanych obowiązków ludzie zwykle wykonują kilka " +
                "powtarzalnych  czynności.\nOdbierają mejle, piją kawę, telefonują i obowiązkowo przeglądają gorące oferty z kilku ulubionych stron internetowych." +
                "\nCzemu by nie zaoszczędzić trochę czasu i skorzystać z aplikacji która oprócz tego żę będzie prezentować obecne gorące oferty to jeszcze powiadomi nas" +
                " o tym że pojawiła się zupełnie nowa?\n\n I tutaj pojawia się pierwsza wersja aplikacji HotShot której idea zrodziła się w wakacje roku pańskiego 2016.");

        AddElementifDoesNotExist(info2);

        ActiveInfo info3 = new ActiveInfo(BuildConfig.VERSION_NAME,date1,"Wersja Beta","Prezentowa obecnie aplikacja jest wersją testową, która mam nadzieję " +
                " dzięki waszym uwagom będę mógł poprawić i udoskonalić. Z wszelkimi propozycjami liczę na kontakt mejlowy który można uzyskać dzięki zakładce " +
                "\"Napisz do nas\" w menu głównym lub proszę o bezpośredni kontakt na adres mejlowy - grezelek@gmail.com \n\n Dziękuję");

        AddElementifDoesNotExist(info3);
    }

}
