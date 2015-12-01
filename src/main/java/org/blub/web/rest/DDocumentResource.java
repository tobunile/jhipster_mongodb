package org.blub.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.blub.domain.DDocument;
import org.blub.repository.DDocumentRepository;
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
 * REST controller for managing DDocument.
 */
@RestController
@RequestMapping("/api")
public class DDocumentResource {

    private final Logger log = LoggerFactory.getLogger(DDocumentResource.class);

    @Inject
    private DDocumentRepository dDocumentRepository;

    /**
     * POST  /dDocuments -> Create a new dDocument.
     */
    @RequestMapping(value = "/dDocuments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DDocument> createDDocument(@RequestBody DDocument dDocument) throws URISyntaxException {
        log.debug("REST request to save DDocument : {}", dDocument);
        if (dDocument.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dDocument cannot already have an ID").body(null);
        }
        DDocument result = dDocumentRepository.save(dDocument);
        return ResponseEntity.created(new URI("/api/dDocuments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dDocument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dDocuments -> Updates an existing dDocument.
     */
    @RequestMapping(value = "/dDocuments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DDocument> updateDDocument(@RequestBody DDocument dDocument) throws URISyntaxException {
        log.debug("REST request to update DDocument : {}", dDocument);
        if (dDocument.getId() == null) {
            return createDDocument(dDocument);
        }
        DDocument result = dDocumentRepository.save(dDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dDocument", dDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dDocuments -> get all the dDocuments.
     */
    @RequestMapping(value = "/dDocuments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DDocument> getAllDDocuments() {
        log.debug("REST request to get all DDocuments");
        return dDocumentRepository.findAll();
    }

    /**
     * GET  /dDocuments/:id -> get the "id" dDocument.
     */
    @RequestMapping(value = "/dDocuments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DDocument> getDDocument(@PathVariable String id) {
        log.debug("REST request to get DDocument : {}", id);
        return Optional.ofNullable(dDocumentRepository.findOne(id))
            .map(dDocument -> new ResponseEntity<>(
                dDocument,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dDocuments/:id -> delete the "id" dDocument.
     */
    @RequestMapping(value = "/dDocuments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDDocument(@PathVariable String id) {
        log.debug("REST request to delete DDocument : {}", id);
        dDocumentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dDocument", id.toString())).build();
    }
}
