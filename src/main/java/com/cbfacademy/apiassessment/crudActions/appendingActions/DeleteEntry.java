package com.cbfacademy.apiassessment.crudActions.appendingActions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cbfacademy.apiassessment.exceptions.ItemNotFoundException;
import com.cbfacademy.apiassessment.model.Watchlist;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeleteEntry {

    private static final Logger log = LoggerFactory.getLogger(DeleteEntry.class);

    public ReadExistingWatchlist readList;
    public UpdateAndWrite updateAndWrite;

    @Autowired
    public DeleteEntry(ReadExistingWatchlist readList, UpdateAndWrite updateAndWrite) {
        this.readList = readList;
        this.updateAndWrite = updateAndWrite;
    }

        private void deleteWatchlistItem(List<Watchlist> existingWatchlists, UUID uuid){
            Iterator<Watchlist> iterator = existingWatchlists.iterator();
            while(iterator.hasNext()){
                Watchlist watchlistEntry = iterator.next();
                if(watchlistEntry.getUuid().equals(uuid)){
                    iterator.remove();
                }
            }
        // for(int i = 0; i < existingWatchlists.size(); i++){
        //     if("uuid".equals(existingWatchlists.get(i).getUuid())){
        //         existingWatchlists.remove(i);
        //         break;
        //     }
            log.info("item with the UUID of " + uuid + " has been located.");
        }
    // }
    public List<Watchlist> deleteEntry(@RequestBody List<Watchlist> existingWatchlist, String jsonRepo, ObjectMapper mapper, UUID uuid){
        try {
            existingWatchlist = readList.readExistingWatchlist(jsonRepo, mapper);
            deleteWatchlistItem(existingWatchlist, uuid);
            updateAndWrite.writeUpdatedWatchlist(jsonRepo, mapper, existingWatchlist);
            // return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ItemNotFoundException e) {
            log.error("Jackson Exception has occurred in while trying to delete watchlist entry.", e.getMessage());
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.error("IOException has ocurred while trying to delete entry", e);
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return existingWatchlist;
    }
}