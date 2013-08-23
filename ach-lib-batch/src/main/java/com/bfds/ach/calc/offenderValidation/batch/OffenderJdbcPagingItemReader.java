package com.bfds.ach.calc.offenderValidation.batch;

import com.bfds.ach.calc.validation.domain.Offender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;


public class OffenderJdbcPagingItemReader extends JdbcPagingItemReader<Offender> {

    private static final Logger logger = LoggerFactory.getLogger(OffenderJdbcPagingItemReader.class);

    private ChunkPreProcessor chunkPreProcessor;

    @Override
    protected void doReadPage() {
        super.doReadPage();
        chunkPreProcessor.processOffenders(this.results);
        if(logger.isTraceEnabled()){
    		logger.trace(String.format("loading offenders from the offender database:%s ", this.results));
    		}

    }

    public void setChunkPreProcessor(ChunkPreProcessor chunkPreProcessor) {
        this.chunkPreProcessor = chunkPreProcessor;
    }
}
