package com.signal.ingest;

import com.signal.source.Source;
import java.util.List;

public interface SourceAdapter {
    String type();
    List<RawItem> fetch(Source source, FetchContext ctx) throws FetchException;
}

