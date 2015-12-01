package org.blub.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.blub.domain.Directory;
import org.blub.repository.DirectoryRepository;
import org.blub.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Directory.
 */
@RestController
@RequestMapping("/api")
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);

    @Inject
    private DirectoryRepository directoryRepository;

    /**
     * POST  /directorys -> Create a new directory.
     */
    @RequestMapping(value = "/directorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Directory> createDirectory(@RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to save Directory : {}", directory);
        if (directory.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new directory cannot already have an ID").body(null);
        }
        Directory result = directoryRepository.save(directory);
        return ResponseEntity.created(new URI("/api/directorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("directory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directorys -> Updates an existing directory.
     */
    @RequestMapping(value = "/directorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Directory> updateDirectory(@RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to update Directory : {}", directory);
        if (directory.getId() == null) {
            return createDirectory(directory);
        }
        Directory result = directoryRepository.save(directory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("directory", directory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directorys -> get all the directorys.
     */
    @RequestMapping(value = "/directorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Directory> getAllDirectorys() {
        log.debug("REST request to get all Directorys");
        return directoryRepository.findAll();
    }

    /**
     * GET  /directorys/:id -> get the "id" directory.
     */
    @RequestMapping(value = "/directorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Directory> getDirectory(@PathVariable String id) {
        log.debug("REST request to get Directory : {}", id);
        return Optional.ofNullable(directoryRepository.findOne(id))
            .map(directory -> new ResponseEntity<>(
                directory,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /directorys/:id -> delete the "id" directory.
     */
    @RequestMapping(value = "/directorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDirectory(@PathVariable String id) {
        log.debug("REST request to delete Directory : {}", id);
        directoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("directory", id.toString())).build();
    }
}
