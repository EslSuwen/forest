package com.rymcu.forest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class LuceneIndexTest {

  private String ids[] = {"1", "2", "3"};
  private String citys[] = {"qingdao", "nanjing", "shanghai"};
  private String descs[] = {
    "Qingdao is a beautiful city.", "Nanjing is a city of culture.", "Shanghai is a bustling city."
  };

  private Directory dir; // 目录

  /**
   * 获取IndexWriter实例
   *
   * @return
   * @throws Exception
   */
  private IndexWriter getWriter() throws Exception {
    Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
    return new IndexWriter(dir, iwc);
  }

  /**
   * 添加文档
   *
   * @throws Exception
   */
  @Before("")
  public void setUp() throws Exception {
    dir = FSDirectory.open(Paths.get("lucene/index")); // 得到luceneIndex目录
    IndexWriter writer = getWriter(); // 得到索引
    for (int i = 0; i < ids.length; i++) {
      Document doc = new Document(); // 创建文档
      doc.add(new StringField("id", ids[i], Field.Store.YES)); // 将id属性存入内存中
      doc.add(new StringField("city", citys[i], Field.Store.YES));
      doc.add(new TextField("desc", descs[i], Field.Store.NO));
      writer.addDocument(doc); // 添加文档
    }
    writer.close();
  }

  /**
   * 测试写了几个文档
   *
   * @throws Exception
   */
  @Test
  public void testIndexWriter() throws Exception {
    IndexWriter writer = getWriter();
    System.out.println("写入了" + writer.numRamDocs() + "个文档");
    writer.close();
  }

  /**
   * 测试读取文档
   *
   * @throws Exception
   */
  @Test
  public void testIndexReader() throws Exception {
    IndexReader reader = DirectoryReader.open(dir);
    System.out.println("最大文档数：" + reader.maxDoc());
    System.out.println("实际文档数：" + reader.numDocs());
    reader.close();
  }

  /**
   * 测试删除 在合并前
   *
   * @throws Exception
   */
  @Test
  public void testDeleteBeforeMerge() throws Exception {
    IndexWriter writer = getWriter();
    System.out.println("删除前：" + writer.numRamDocs());
    writer.deleteDocuments(new Term("id", "1")); // term：根据id找到为1的
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
    IndexWriter writer = getWriter();
    System.out.println("删除前：" + writer.numRamDocs());
    writer.deleteDocuments(new Term("id", "1"));
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
    IndexWriter writer = getWriter();
    Document doc = new Document();
    doc.add(new StringField("id", "1", Field.Store.YES));
    doc.add(new StringField("city", "qingdao", Field.Store.YES));
    doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
    writer.updateDocument(new Term("id", "1"), doc);
    writer.close();
  }
}
