package com.transsion.sdk.demo;

/**
 * Created by wenshuai.liu on 2017/4/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class Constant {


    public static final String DOWNLOAD_TEST_URL = "http://s1.music.126.net/download/android/CloudMusic_official_4.1.2_191275.apk";
    public static final String CONFIG_URL = "http://ads.shtranssion.com/chadmin/frontend/web/index.php?r=media-config/indexv1";
    public static final String TEST_JSON = "{\n" +
            "    \"classname\": \"com.mysite.Person\",\n" +
            "    \"firstname\": \"Charlie\",\n" +
            "    \"lastname\": \"Rose\",\n" +
            "    \"age\": 23,\n" +
            "    \"birthplace\": \"Big Sky, Montanna\"\n" +
            "}";
    public static final String TEST_JSON_TWO = "{\n" +
            "    buyer: \"book\",\n" +
            "    sex: \"man\",\n" +
            "    lineItems: [\n" +
            "        {\n" +
            "            name: \"nails\",\n" +
            "            quantity: 100\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String AD_CONFIG = "{\n" +
            //"    \"adConfig\": {\n" +
            //"#全屏广告展示时间单位秒\n" +
            "        \"showTime\": 5,\n" +
            //"#全屏广告请求接口超时时间单位毫秒\n" +
            "        \"requestTimeOut\": 5000\n" +
            //"    }\n" +
            "}";


    public static final String[] IMAGES = new String[]{
            // Phone images
            "http://img4.duitang.com/uploads/item/201407/27/20140727020134_v5iYu.thumb.700_0.jpeg",
            "http://cdn.duitang.com/uploads/item/201407/27/20140727020603_3fJeQ.thumb.700_0.jpeg",
            "http://img0.imgtn.bdimg.com/it/u=3003656471,1362777593&fm=206&gp=0.jpg",
            "http://img5.duitang.com/uploads/item/201407/27/20140727123328_JnKwx.thumb.700_0.jpeg",
            "http://img1.imgtn.bdimg.com/it/u=3578675529,72138100&fm=206&gp=0.jpg",
            "http://img4q.duitang.com/uploads/item/201407/27/20140727015333_ehaWH.jpeg",
            "http://attachments.gfan.com/forum/attachments2/201306/06/2246008ifc1yyyivg5cccc.jpg",
            "http://img4q.duitang.com/uploads/item/201407/27/20140727130126_RJrJZ.jpeg",
            "http://attachments.gfan.com/forum/attachments2/201306/06/224614olweeflppj5fww4k.jpg",
            "http://img5.duitang.com/uploads/item/201407/27/20140727021834_hrPL3.jpeg",
            "http://cdnq.duitang.com/uploads/item/201407/27/20140727015315_5VBTG.jpeg",
            "http://img5.duitang.com/uploads/item/201407/27/20140727020945_vX54S.jpeg",
            "http://img5.duitang.com/uploads/item/201407/27/20140727022248_XSH2j.thumb.700_0.jpeg",
            "http://attachments.gfan.com/forum/attachments2/201306/06/224543wtd59eiw57vrur08.jpg",
            "http://cdn.duitang.com/uploads/item/201407/27/20140727020843_raiKs.thumb.700_0.jpeg",
            "http://cdn.duitang.com/uploads/item/201407/27/20140727020815_RmhGm.thumb.700_0.jpeg",
            "http://download.pchome.net/wallpaper/pic-4912-11-1080x1920.jpg",
            "http://www.5djpg.com/uploads/allimg/140724/1-140H4120951.jpg",
            "http://www.5djpg.com/uploads/allimg/140331/1-1403311H417.jpg",
            "http://download.pchome.net/wallpaper/pic-5288-9-1080x1920.jpg",
            "http://www.5djpg.com/uploads/allimg/140612/1-140612160544.jpg",
            "http://cdnq.duitang.com/uploads/item/201407/27/20140727022218_WfXUZ.jpeg",
            "http://download.pchome.net/wallpaper/pic-5288-13-1080x1920.jpg",
            "http://img5.duitang.com/uploads/item/201407/27/20140727015257_niMk8.jpeg",
            "http://img4.duitang.com/uploads/item/201407/27/20140727125637_M2Vzf.thumb.700_0.jpeg",
            //Heavy images
            "http://tupian.enterdesk.com/2012/0626/xin/01/03.jpg",
            "http://tupian.enterdesk.com/2014/xll/03/04/2/shaosiming5.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1206/18/c0/12043463_1339987116996.jpg",
            "http://pic1.win4000.com/wallpaper/4/5359fe2a17ffc.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1212/14/c1/16599861_1355466819747.jpg",
            "http://pic2.desk.chinaz.com/file/10.08.10/7/bfengls13.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/dbb44aed2e738bd4f9ae406fa18b87d6267ff9e5.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/7dd98d1001e93901948c7f607bec54e737d196e6.jpg",
            "http://cdn.pcbeta.attachment.inimc.com/data/attachment/forum/201312/05/163819qu96rum6mfr62xvx.jpg",
            "http://f1.bj.anqu.com/down/OGMxMw==/allimg/1208/48-120P61A444-50.jpg",
            "http://pic1.desk.chinaz.com/file/11.07.10/8/lengsediaogaoq50.jpg",
            "http://g.hiphotos.baidu.com/lvpics/h=800/sign=10a3be0a2f381f30811980a999014c67/eaf81a4c510fd9f959a6d95b222dd42a2834a44b.jpg",
            "http://pic1.win4000.com/wallpaper/2/552e2f9c0a050.jpg",
            "http://img.taopic.com/uploads/allimg/120205/6756-1202051R33627.jpg",
            "http://img15.3lian.com/2015/f2/101/d/90.jpg",
            "http://pic1.win4000.com/wallpaper/5/53c74902d3a6b.jpg",
            "http://img2.3lian.com/2014/f3/70/d/86.jpg",
            "http://img2.3lian.com/2014/f3/70/d/87.jpg",
            "http://pic1.win4000.com/wallpaper/e/5382fc10796f2.jpg",
            "http://pic1.win4000.com/wallpaper/8/53f2e0cb93d15.jpg",
            "http://www.wallcoo.com/nature/Amazing_Color_Landscape_2560x1600/wallpapers/1680x1050/Amazing_Landscape_119.jpg",
            "http://pic1.win4000.com/wallpaper/5/5514fb3e64604.jpg",
            "http://pic1.win4000.com/wallpaper/2/5513a45a88a78.jpg",
            "http://img3.3lian.com/2014/c1/43/d/18.jpg",
            "http://img2.3lian.com/2014/f6/108/d/50.jpg",
            "http://img1.3lian.com/2015/w1/77/d/42.jpg",
            "http://pic1.win4000.com/wallpaper/a/5493c2874eab3.jpg",
            "http://pic1.win4000.com/wallpaper/0/551cf35a211e6.jpg",
            "http://img3.3lian.com/2013/v10/13/d/113.jpg",
            "http://img1.3lian.com/2015/w22/38/d/91.jpg",
            "http://pic1.win4000.com/wallpaper/e/5514fd0a18382.jpg",
            "http://img1.3lian.com/2015/w3/28/d/67.jpg",
            "http://img3.3lian.com/2013/v10/13/d/107.jpg",
            "http://img1.3lian.com/2015/w22/38/d/90.jpg",
            "http://static.xialv.com/scenery/2014/10/22/0c/14139493191627kexoz.jpg",
            "http://img3.3lian.com/2014/c1/43/d/23.jpg",
            "http://img1.3lian.com/2015/w3/28/d/62.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/023b5bb5c9ea15ce924c6192b6003af33b87b28f.jpg",
            "http://img1.3lian.com/2015/w3/28/d/62.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/1ad5ad6eddc451daa5128d70b6fd5266d1163240.jpg",
            "http://img1.gamedog.cn/2013/03/07/24-13030G011000.jpg",
            "http://pic24.nipic.com/20121016/7150141_142949978000_2.jpg",
            "http://www.1tong.com/uploads/wallpaper/illustration/138-5-1024x768.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1210/26/c2/14691424_1351245180941.jpg",
            "http://img4.duitang.com/uploads/item/201312/22/20131222103035_reRXW.jpeg",
            "http://www.bz55.com/uploads/allimg/140911/138-140911110108.jpg",
            // Light images
            "http://img0.imgtn.bdimg.com/it/u=2463502631,541546141&fm=206&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201507/02/20150702213840_TaFn4.thumb.700_0.jpeg",
            "http://uploads.xuexila.com/allimg/1507/641-150G31J417.jpg",
            "http://pic40.nipic.com/20140414/17922710_123056623115_2.jpg",
            "http://img5q.duitang.com/uploads/item/201506/02/20150602185303_UCukR.jpeg",
            "http://img4q.duitang.com/uploads/item/201504/08/20150408H0245_HTGh3.thumb.700_0.jpeg",
            "http://img15.3lian.com/2015/f1/169/d/121.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/6a600c338744ebf8efaf0794d9f9d72a6159a7bb.jpg",
            "http://cdn.duitang.com/uploads/item/201505/02/20150502235605_5KXvR.jpeg",
            "http://cdn.duitang.com/uploads/item/201411/08/20141108201327_sCxZX.jpeg",
            "http://img4q.duitang.com/uploads/item/201505/14/20150514160419_PmUCz.jpeg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/78310a55b319ebc4951f90ac8526cffc1e17169b.jpg",
            "http://pic.58pic.com/58pic/14/75/47/45e58PICQdS_1024.jpg",
            "http://img2.3lian.com/2014/f5/64/d/83.jpg",
            "http://www.bz55.com/uploads/allimg/130615/1-1306150Z129.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3139223101,3456061533&fm=206&gp=0.jpg",
            "http://img1.3lian.com/2015/w7/87/d/27.jpg",
            "http://tupian.enterdesk.com/2014/mxy/02/22/3/2.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/4e8c7b09c93d70cfe4350bf4f8dcd100b8a12baa.jpg",
            "http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
            "http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
            "http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
            "http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
            // Special cases
            "http://www.ioncannon.net/wp-content/uploads/2011/06/test9.webp", // WebP image
            "http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
    };
}
