
import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.util.Version;

public class SimpleSearcher {
    
    public static void main(String[] args) throws Exception {
        
    	File indexDir = new File("/Users/ravitandon/Projects/ParaBlogging/DocSimMod/");    	
        String query = "Kernel";
        int hits = 100;
        
        SimpleSearcher searcher = new SimpleSearcher();
        searcher.searchIndex(indexDir, query, hits);
        
    }
    
    private void searchIndex(File indexDir, String queryStr, int maxHits) 
            throws Exception {
    	Version matchVersion = Version.LUCENE_47;
        Directory directory = FSDirectory.open(indexDir);

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
        QueryBuilder builder = new QueryBuilder(new StandardAnalyzer(matchVersion));
        Query query = builder.createBooleanQuery("contents", queryStr);
        
        TopDocs topDocs = searcher.search(query, maxHits);
        
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
        }
        
        System.out.println("Found " + hits.length);
        
    }

}
