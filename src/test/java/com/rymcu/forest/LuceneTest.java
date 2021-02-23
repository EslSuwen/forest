package com.rymcu.forest;

import com.rymcu.forest.lucene.lucene.ArticleBeanIndex;
import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.util.IndexUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LuceneTest {

  private final String indexPath = "lucene/index";

  @Test
  public void createIndex() throws Exception {
    ArticleBeanIndex index = new ArticleBeanIndex(indexPath, 777);
    index.indexDoc(
        ArticleLucene.builder()
            .idArticle("1777")
            .articleTitle("手动测试")
            .articleContent("手动content")
            .build());
  }

  @Test
  public void simpleCreatIndex() throws IOException {
    Document doc = new Document();
    Field id = new Field("id", "123", TextField.TYPE_STORED);
    Field title = new Field("title", "简单的测试", TextField.TYPE_STORED);
    Field summary = new Field("summary", "简单的测试", TextField.TYPE_STORED);
    // 添加到Document中
    doc.add(id);
    doc.add(title);
    doc.add(summary);
    IndexWriter writer = IndexUtil.getIndexWriter(indexPath + "/index" + 777, true);
    System.out.println(writer.getConfig().getOpenMode());
    writer.updateDocument(new Term("id", "更新"), doc);
    writer.close();
  }
}
