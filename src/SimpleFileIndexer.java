
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class SimpleFileIndexer {

    public static void main(String[] args) throws Exception {
        
        File indexDir = new File("/Users/ravitandon/Projects/ParaBlogging/DocSimMod/");
        File dataDir = new File("/Users/ravitandon/Desktop/Research");
        String suffix = "pdf";
        
        SimpleFileIndexer indexer = new SimpleFileIndexer();
        
        int numIndex = indexer.index(indexDir, dataDir, suffix);
        
        System.out.println("Total files indexed " + numIndex);
        
    }
    
    private int index(File indexDir, File dataDir, String suffix) throws Exception {
    	Version matchVersion = Version.LUCENE_47;    	
    	Analyzer analyzer = new StandardAnalyzer(matchVersion);
    		 
    	IndexWriterConfig indexWriterConfig = new IndexWriterConfig(matchVersion, analyzer);
        IndexWriter indexWriter = new IndexWriter(
                FSDirectory.open(indexDir),  indexWriterConfig);
        
        indexDirectory(indexWriter, dataDir, suffix);
        
        int numIndexed = indexWriter.maxDoc();
        indexWriter.close();
        
        return numIndexed;
        
    }
    
    private void indexDirectory(IndexWriter indexWriter, File dataDir, 
           String suffix) throws IOException {

        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(indexWriter, f, suffix);
            }
            else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }

    }
    
    private void indexFileWithIndexWriter(IndexWriter indexWriter, File f, 
            String suffix) throws IOException {

        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
            return;
        }
        if (suffix!=null && !f.getName().endsWith(suffix)) {
            return;
        }
        System.out.println("Indexing file " + f.getCanonicalPath());
        
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(f)));        
        doc.add(new StringField("filename", f.getCanonicalPath(), Field.Store.YES));
        
        indexWriter.addDocument(doc);

    }

}