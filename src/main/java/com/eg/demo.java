package com.eg;

import com.eg.utils.MongoDBUtil;
import com.eg.utils.PinYinUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.bson.Document;
import org.hamcrest.Condition;
import org.junit.Test;


import javax.management.Query;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: Eg
 * @Date: 2021/11/11 15:38
 */
public class demo {

    MongoDBUtil util = MongoDBUtil.getInstance();

    MongoDatabase database = util.getDatabase("u_friends");

    MongoCollection<Document> list = database.getCollection("0");
    PinYinUtil pinYinUtil = new PinYinUtil();

    @Test
    public void dbRegexTest(){

        BasicDBObject basicDBObject = new BasicDBObject();

        String str = "客服";

        String quanpin=pinYinUtil.getPingYin(str);
        String head=pinYinUtil.getPinYinHeadChar(str);

        Pattern pattern = Pattern.compile(str + "++", Pattern.CASE_INSENSITIVE);
        basicDBObject.put("toNickname", pattern);

        FindIterable<Document> documents = list.find(basicDBObject);

        MongoCursor<Document> mongoCursor = documents.iterator();

        while (mongoCursor.hasNext()){
            System.out.println("iterator.document = " + mongoCursor.next());
        }

    }

    @Test
    public void test() {

        String iptS = "lx";
        FindIterable<Document> documents = list.find().sort(new Document("toNickname",1));
        MongoCursor<Document> mongoCursor = documents.iterator();

        while (mongoCursor.hasNext()) {

            String str = String.valueOf(mongoCursor.next().get("toNickname"));

            String quanpin=pinYinUtil.getPingYin(str);
            String head=pinYinUtil.getPinYinHeadChar(str);

            if (quanpin.contains(iptS) || head.contains(iptS)){
                System.out.println(str);
            }
        }
    }




}
