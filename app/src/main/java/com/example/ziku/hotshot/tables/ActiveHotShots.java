package com.example.ziku.hotshot.tables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.example.ziku.hotshot.jsonservices.KeyItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Ziku on 2016-09-11.
 */
@Table(name = ActiveHotShots.HOT_SHOT_TABLE)
public class ActiveHotShots extends Model implements Comparable<ActiveHotShots>{

    public static final String HOT_SHOT_TABLE = "hot_shots_table";
    public static final String HOT_SHOT_WEB_ID = "hot_shot_web_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String OLD_PRICE = "old_price";
    public static final String NEW_PRICE = "new_price";
    public static final String IMG_URL = "img_url";
    public static final String PRODUCT_URL = "product_url";
    public static final String WEB_SITE_ID = "web_site_id";
    public static final String LAST_NEW_CHANGE = "last_new_change";
    public static final String LAST_NOTYFICATION = "last_notyfication";

    @Column(name = PRODUCT_NAME)
    public String productName;

    @Column(name = OLD_PRICE)
    public int oldPrice;

    @Column(name = NEW_PRICE)
    public int newPrice;

    @Column(name = IMG_URL)
    public String imgUrl;

    @Column(name = PRODUCT_URL)
    public String productUrl;

    @Column(name = WEB_SITE_ID)
    public ActiveWebSites webSites;

    @Column(name = LAST_NOTYFICATION)
    public String lastNotyfication;

    @Column(name = LAST_NEW_CHANGE)
    public Date lastNewChange;


    public ActiveHotShots(){
        super();
    }

    public ActiveHotShots( String productName, int oldPrice, int newPrice, String imgUrl, String productUrl, ActiveWebSites webSites, String lastNotyfication, Date lastNewChange) {
        this.productName = productName;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.imgUrl = imgUrl;
        this.productUrl = productUrl;
        this.webSites = webSites;
        this.lastNotyfication = lastNotyfication;
        this.lastNewChange = lastNewChange;
    }

    @Override
    public int compareTo(ActiveHotShots activeHotShots) {
        Date date1 = activeHotShots.lastNewChange;
        Date date2 = this.lastNewChange;
        if(date1.after(date2)){
            return 1;
        } else return -1;
    }


    public static List<ActiveHotShots> ReturnAllActiveHotShotsActive(int category){

        List<ActiveHotShots> activeHotShotses;
        activeHotShotses = new Select().from(ActiveHotShots.class).where(ActiveHotShots.PRODUCT_NAME + " != ?", KeyItems.EMPTY).execute();

        List<ActiveHotShots> returnList = new ArrayList<>();
        for (ActiveHotShots active :activeHotShotses) {
            if(active.webSites.isActive){
                if(category == 0) {
                      returnList.add(active);
                }
                else {
                    ActiveCategories activeCategories = ActiveCategories.load(ActiveCategories.class, category);
                    if(activeCategories.categoryType == active.webSites.activeCategory.categoryType)
                        returnList.add(active);
                }
            }
        }
        Collections.sort(returnList);
        return  returnList;
    }

}
