package com.cabinet.bezir;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/4/20
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class LyricContent {
    String data;
    long time;


    public void setLyric(String splitLrc_datum) {
        data = splitLrc_datum;
    }

    public void setLyricTime(long lyricTime) {
        time = lyricTime;
    }
}
