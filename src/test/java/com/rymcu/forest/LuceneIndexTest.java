package com.rymcu.forest;

import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.util.ArticleIndexUtil;
import com.rymcu.forest.lucene.util.IndexUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;

public class LuceneIndexTest {

  private final String[] ids = {"1", "2", "3"};
  private final String[] ids2 = {"91", "81", "71"};
  private final String[] citys2 = {"再试试看吧", "这个完全不太行啊", "这个看作一个词就不分词了"};
  private final String[] citys = {"qingdao", "nanjing", "shanghai"};
  private final String[] descs = {
    "Qingdao is a beautiful city.", "Nanjing is a city of culture.", "Shanghai is a bustling city."
  };
  private final String[] descs2 = {
    "需求：点击上传txt文件，先读取txt文件中的内容进行展示。\n" + "\n" + "上传使用的是element的upload如下：",
    "传的TXT文件...好像也乱码了，下面这种的 閯遍槼婀栧悇姘存枃绔欏叏绾垮憡鎬� 476涓� 2 63宀佷腑鍥界埛鐖锋垚娌圭\uE178缃戠孩 459涓� 3 姘斿姛澶у笀鍙戝姛閫€娲\uE047\uE766鎵瑰摋浼楀彇瀹犳柊",
    "如果不是读取本地文件，而是读取服务器发送过来的文件这个怎么弄？"
  };

  private Directory dir; // 目录

  @Test
  public void fun() {
    try {
      ArticleIndexUtil.updateIndex(
          ArticleLucene.builder()
              .idArticle("123")
              .articleTitle("好像也行")
              .articleContent("好像也行")
              .build());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 添加文档
   *
   * @throws Exception
   */
  @Test
  public void setUp() throws Exception {
    IndexWriter writer =
        IndexUtil.getIndexWriter(
            System.getProperty("user.dir") + "/lucene/index/index777", true); // 得到索引
    for (int i = 0; i < ids.length; i++) {
      Document doc = new Document(); // 创建文档
      doc.add(new StringField("id", ids[i], Field.Store.YES)); // 将id属性存入内存中
      doc.add(new StringField("city", citys[i], Field.Store.NO));
      doc.add(new TextField("desc", descs[i], Field.Store.NO));
      writer.addDocument(doc); // 添加文档
    }
    writer.close();
  }

  @Test
  public void setUp1() throws Exception {
    IndexWriter writer = IndexUtil.getIndexWriter("lucene/index/index777", false); // 得到索引
    for (int i = 0; i < ids.length; i++) {
      Document doc = new Document(); // 创建文档
      doc.add(new StringField("id", ids2[i], Field.Store.YES)); // 将id属性存入内存中
      doc.add(new TextField("city", citys2[i], Field.Store.YES));
      doc.add(new TextField("desc", descs2[i], Field.Store.NO));
      writer.addDocument(doc); // 添加文档
    }
    writer.close();
  }

  /**
   * 测试删除 在合并前
   *
   * @throws Exception
   */
  @Test
  public void testDeleteBeforeMerge() throws Exception {
    IndexWriter writer = IndexUtil.getIndexWriter("lucene/index/index777", false); // 得到索引
    System.out.println("删除前：" + writer.numRamDocs());
    writer.deleteDocuments(new Term("id", "2")); // term：根据id找到为1的
    writer.commit();
    System.out.println("writer.maxDoc()：" + writer.getMaxCompletedSequenceNumber());
    System.out.println("writer.numDocs()：" + writer.numRamDocs());
    writer.close();
  }

  /**
   * 测试删除 在合并后
   *
   * @throws Exception
   */
  @Test
  public void testDeleteAfterMerge() throws Exception {
    IndexWriter writer = IndexUtil.getIndexWriter("lucene/index/index777", false); // 得到索引
    System.out.println("删除前：" + writer.numRamDocs());
    writer.deleteDocuments(new Term("id", "2"));
    writer.forceMergeDeletes(); // 强制删除
    writer.commit();
    System.out.println("writer.maxDoc()：" + writer.getMaxCompletedSequenceNumber());
    System.out.println("writer.numDocs()：" + writer.numRamDocs());
    writer.close();
  }

  /**
   * 测试更新
   *
   * @throws Exception
   */
  @Test
  public void testUpdate() throws Exception {
    IndexWriter writer = IndexUtil.getIndexWriter("lucene/index/index777", false); // 得到索引
    Document doc = new Document();
    doc.add(new StringField("id", "1", Field.Store.YES));
    doc.add(new StringField("city", "qingdao", Field.Store.YES));
    doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
    writer.updateDocument(new Term("id", "1"), doc);
    writer.close();
  }

  @Test
  public void deleteIndex() throws Exception {
    IndexWriter writer = IndexUtil.getIndexWriter("lucene/index/index777", false);
    writer.deleteDocuments(new Term("id", "11558"));
    writer.forceMergeDeletes(); // 强制删除
    writer.commit();
    writer.close();
  }
}
