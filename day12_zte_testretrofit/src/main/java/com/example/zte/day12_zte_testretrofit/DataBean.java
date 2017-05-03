package com.example.zte.day12_zte_testretrofit;

import java.util.List;

/**
 * Created by Administrator on 2017-05-02.
 */

public class DataBean {
    @Override
    public String toString() {
        return "DataBean{" +
                "pageno=" + pageno +
                ", ad=" + ad +
                ", list=" + list +
                '}';
    }

    /**
     * ad : [{"id":303,"title":"《九阴真经3D》五一独家礼包","flag":1,"iconurl":"/allimgs/img_iad/_1493286185901.jpg","addtime":"2017-04-17","giftid":"1493273448","appName":"九阴真经3D","appLogo":"/allimgs/img_iapp/201611/_1480055708127.png","appId":1462955336},{"id":301,"title":"《放开那三国2》独家礼包","flag":1,"iconurl":"/allimgs/img_iad/_1493361275759.jpg","addtime":"2017-04-16","giftid":"1493283355","appName":"放开那三国2","appLogo":"/allimgs/img_iapp/201609/_1474190951389.png","appId":1474191030},{"id":302,"title":"《放开那三国》五一独家礼包","flag":1,"iconurl":"/allimgs/img_iad/_1493361415046.jpg","addtime":"2017-04-14","giftid":"1493350252","appName":"放开那三国","appLogo":"/userfiles/applogo/_1410866849837.jpg","appId":2484862},{"id":296,"title":"《航海王强者之路》五一独家礼包","flag":1,"iconurl":"/allimgs/img_iad/_1493361509469.jpg","addtime":"2017-03-18","giftid":"1493347870","appName":"航海王强者之路","appLogo":"/allimgs/img_iapp/201511/_1448446144213.png","appId":1448446177},{"id":298,"title":"《火线冲突》五一独家礼包","flag":1,"iconurl":"/allimgs/img_iad/_1493361642887.jpg","addtime":"2017-03-18","giftid":"1493345224","appName":"火线冲突","appLogo":"/allimgs/img_iapp/201704/_1493283370280.png","appId":1493283424}]
     * pageno : 111
     * list : [{"id":"1493695572","iconurl":"/allimgs/img_iapp/201603/_1456903101032.png","giftname":"五一荣耀礼包","number":16,"exchanges":1,"type":1,"gname":"葫芦娃","integral":0,"isintegral":0,"addtime":"2017-05-02","ptype":"1,","operators":"","flag":1},{"id":"1493695808","iconurl":"/allimgs/img_iapp/201704/_1493286828803.png","giftname":"媒体礼包","number":20,"exchanges":0,"type":1,"gname":"圣剑守护","integral":0,"isintegral":0,"addtime":"2017-05-02","ptype":"1,","operators":"","flag":1},{"id":"1493695935","iconurl":"/allimgs/img_iapp/201701/_1484895585273.png","giftname":"安卓媒体礼包","number":600,"exchanges":0,"type":1,"gname":"战吕布","integral":0,"isintegral":0,"addtime":"2017-05-02","ptype":"1,","operators":"","flag":1},{"id":"1493345301","iconurl":"/allimgs/img_iapp/201704/_1493283370280.png","giftname":"五一独家礼包","number":52,"exchanges":4,"type":1,"gname":"火线冲突","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493345473","iconurl":"/allimgs/img_iapp/201704/_1493283370280.png","giftname":"五一礼包","number":25,"exchanges":1,"type":1,"gname":"火线冲突","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493345878","iconurl":"/allimgs/img_iapp/201607/_1469589494000.png","giftname":"劳动节独家礼包","number":48,"exchanges":2,"type":1,"gname":"口水封神","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493346176","iconurl":"/allimgs/img_iapp/201607/_1469589494000.png","giftname":"劳动节礼包","number":50,"exchanges":0,"type":1,"gname":"口水封神","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493346365","iconurl":"/allimgs/img_iapp/201704/_1493283510770.png","giftname":"五一独家礼包","number":39,"exchanges":0,"type":1,"gname":"全民喵忍酷跑","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493346580","iconurl":"/allimgs/img_iapp/201704/_1493283510770.png","giftname":"五一礼包","number":76,"exchanges":0,"type":1,"gname":"全民喵忍酷跑","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493346733","iconurl":"/allimgs/img_iapp/201704/_1493283637250.jpg","giftname":"劳动节礼包","number":51,"exchanges":2,"type":1,"gname":"猪猪侠机甲王","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493346922","iconurl":"/allimgs/img_iapp/201704/_1493283637250.jpg","giftname":"欢乐礼包","number":95,"exchanges":1,"type":1,"gname":"猪猪侠机甲王","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493347335","iconurl":"/allimgs/img_iapp/201702/_1487842180306.png","giftname":"五一礼包","number":15,"exchanges":1,"type":1,"gname":"攻城三国","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,","operators":"","flag":1},{"id":"1493347575","iconurl":"/allimgs/img_iapp/201511/_1448446144213.png","giftname":"五一节日礼包","number":0,"exchanges":15,"type":1,"gname":"航海王强者之路","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493347869","iconurl":"/allimgs/img_iapp/201511/_1448446144213.png","giftname":"五一独家礼包","number":0,"exchanges":10,"type":1,"gname":"航海王强者之路","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"1,2,","operators":"","flag":1},{"id":"1493348220","iconurl":"/allimgs/img_iapp/201704/_1491375403180.jpg","giftname":"五一礼包","number":7,"exchanges":1,"type":1,"gname":"红月传奇","integral":0,"isintegral":0,"addtime":"2017-04-28","ptype":"2,","operators":"","flag":1}]
     */

    private int pageno;
    private List<AdBean> ad;
    private List<ListBean> list;

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public List<AdBean> getAd() {
        return ad;
    }

    public void setAd(List<AdBean> ad) {
        this.ad = ad;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class AdBean {
        @Override
        public String toString() {
            return "AdBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", flag=" + flag +
                    ", iconurl='" + iconurl + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", giftid='" + giftid + '\'' +
                    ", appName='" + appName + '\'' +
                    ", appLogo='" + appLogo + '\'' +
                    ", appId=" + appId +
                    '}';
        }

        /**
         * id : 303
         * title : 《九阴真经3D》五一独家礼包
         * flag : 1
         * iconurl : /allimgs/img_iad/_1493286185901.jpg
         * addtime : 2017-04-17
         * giftid : 1493273448
         * appName : 九阴真经3D
         * appLogo : /allimgs/img_iapp/201611/_1480055708127.png
         * appId : 1462955336
         */

        private int id;
        private String title;
        private int flag;
        private String iconurl;
        private String addtime;
        private String giftid;
        private String appName;
        private String appLogo;
        private int appId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getGiftid() {
            return giftid;
        }

        public void setGiftid(String giftid) {
            this.giftid = giftid;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppLogo() {
            return appLogo;
        }

        public void setAppLogo(String appLogo) {
            this.appLogo = appLogo;
        }

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }
    }

    public static class ListBean {
        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", iconurl='" + iconurl + '\'' +
                    ", giftname='" + giftname + '\'' +
                    ", number=" + number +
                    ", exchanges=" + exchanges +
                    ", type=" + type +
                    ", gname='" + gname + '\'' +
                    ", integral=" + integral +
                    ", isintegral=" + isintegral +
                    ", addtime='" + addtime + '\'' +
                    ", ptype='" + ptype + '\'' +
                    ", operators='" + operators + '\'' +
                    ", flag=" + flag +
                    '}';
        }

        /**
         * id : 1493695572
         * iconurl : /allimgs/img_iapp/201603/_1456903101032.png
         * giftname : 五一荣耀礼包
         * number : 16
         * exchanges : 1
         * type : 1
         * gname : 葫芦娃
         * integral : 0
         * isintegral : 0
         * addtime : 2017-05-02
         * ptype : 1,
         * operators :
         * flag : 1
         */

        private String id;
        private String iconurl;
        private String giftname;
        private int number;
        private int exchanges;
        private int type;
        private String gname;
        private int integral;
        private int isintegral;
        private String addtime;
        private String ptype;
        private String operators;
        private int flag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public String getGiftname() {
            return giftname;
        }

        public void setGiftname(String giftname) {
            this.giftname = giftname;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getExchanges() {
            return exchanges;
        }

        public void setExchanges(int exchanges) {
            this.exchanges = exchanges;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIsintegral() {
            return isintegral;
        }

        public void setIsintegral(int isintegral) {
            this.isintegral = isintegral;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getPtype() {
            return ptype;
        }

        public void setPtype(String ptype) {
            this.ptype = ptype;
        }

        public String getOperators() {
            return operators;
        }

        public void setOperators(String operators) {
            this.operators = operators;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
}
