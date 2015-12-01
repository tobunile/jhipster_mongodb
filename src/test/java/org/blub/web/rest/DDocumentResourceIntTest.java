package org.blub.web.rest;

import org.blub.Application;
import org.blub.domain.DDocument;
import org.blub.repository.DDocumentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DDocumentResource REST controller.
 *
 * @see DDocumentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DDocumentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DIRECTORIES = "AAAAA";
    private static final String UPDATED_DIRECTORIES = "BBBBB";

    @Inject
    private DDocumentRepository dDocumentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDDocumentMockMvc;

    private DDocument dDocument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DDocumentResource dDocumentResource = new DDocumentResource();
        ReflectionTestUtils.setField(dDocumentResource, "dDocumentRepository", dDocumentRepository);
        this.restDDocumentMockMvc = MockMvcBuilders.standaloneSetup(dDocumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dDocumentRepository.deleteAll();
        dDocument = new DDocument();
        dDocument.setName(DEFAULT_NAME);
        dDocument.setDirectories(DEFAULT_DIRECTORIES);
    }

    @Test
    public void createDDocument() throws Exception {
        int databaseSizeBeforeCreate = dDocumentRepository.findAll().size();

        // Create the DDocument

        restDDocumentMockMvc.perform(post("/api/dDocuments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dDocument)))
                .andExpect(status().isCreated());

        // Validate the DDocument in the database
        List<DDocument> dDocuments = dDocumentRepository.findAll();
        assertThat(dDocuments).hasSize(databaseSizeBeforeCreate + 1);
        DDocument testDDocument = dDocuments.get(dDocuments.size() - 1);
        assertThat(testDDocument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDDocument.getDirectories()).isEqualTo(DEFAULT_DIRECTORIES);
    }

    @Test
    public void getAllDDocuments() throws Exception {
        // Initialize the database
        dDocumentRepository.save(dDocument);

        // Get all the dDocuments
        restDDocumentMockMvc.perform(get("/api/dDocuments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dDocument.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].directories").value(hasItem(DEFAULT_DIRECTORIES.toString())));
    }

    @Test
    public void getDDocument() throws Exception {
        // Initialize the database
        dDocumentRepository.save(dDocument);

        // Get the dDocument
        restDDocumentMockMvc.perform(get("/api/dDocuments/{id}", dDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dDocument.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.directories").value(DEFAULT_DIRECTORIES.toString()));
    }

    @Test
    public void getNonExistingDDocument() throws Exception {
        // Get the dDocument
        restDDocumentMockMvc.perform(get("/api/dDocuments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDDocument() throws Exception {
        // Initialize the database
        dDocumentRepository.save(dDocument);

		int databaseSizeBeforeUpdate = dDocumentRepository.findAll().size();

        // Update the dDocument
        dDocument.setName(UPDATED_NAME);
        dDocument.setDirectories(UPDATED_DIRECTORIES);

        restDDocumentMockMvc.perform(put("/api/dDocuments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dDocument)))
                .andExpect(status().isOk());

        // Validate the DDocument in the database
        List<DDocument> dDocuments = dDocumentRepository.findAll();
        assertThat(dDocuments).hasSize(databaseSizeBeforeUpdate);
        DDocument testDDocument = dDocuments.get(dDocuments.size() - 1);
        assertThat(testDDocument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDDocument.getDirectories()).isEqualTo(UPDATED_DIRECTORIES);
    }

    @Test
    public void deleteDDocument() throws Exception {
        // Initialize the database
        dDocumentRepository.save(dDocument);

		int databaseSizeBeforeDelete = dDocumentRepository.findAll().size();

        // Get the dDocument
        restDDocumentMockMvc.perform(delete("/api/dDocuments/{id}", dDocument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DDocument> dDocuments = dDocumentRepository.findAll();
        assertThat(dDocuments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
