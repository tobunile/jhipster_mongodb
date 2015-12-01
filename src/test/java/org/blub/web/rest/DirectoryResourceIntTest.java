package org.blub.web.rest;

import org.blub.Application;
import org.blub.domain.Directory;
import org.blub.repository.DirectoryRepository;

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
 * Test class for the DirectoryResource REST controller.
 *
 * @see DirectoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DirectoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private DirectoryRepository directoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectoryResource directoryResource = new DirectoryResource();
        ReflectionTestUtils.setField(directoryResource, "directoryRepository", directoryRepository);
        this.restDirectoryMockMvc = MockMvcBuilders.standaloneSetup(directoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        directoryRepository.deleteAll();
        directory = new Directory();
        directory.setName(DEFAULT_NAME);
    }

    @Test
    public void createDirectory() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory

        restDirectoryMockMvc.perform(post("/api/directorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directory)))
                .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directorys = directoryRepository.findAll();
        assertThat(directorys).hasSize(databaseSizeBeforeCreate + 1);
        Directory testDirectory = directorys.get(directorys.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllDirectorys() throws Exception {
        // Initialize the database
        directoryRepository.save(directory);

        // Get all the directorys
        restDirectoryMockMvc.perform(get("/api/directorys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getDirectory() throws Exception {
        // Initialize the database
        directoryRepository.save(directory);

        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directorys/{id}", directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(directory.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDirectory() throws Exception {
        // Initialize the database
        directoryRepository.save(directory);

		int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory
        directory.setName(UPDATED_NAME);

        restDirectoryMockMvc.perform(put("/api/directorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directory)))
                .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directorys = directoryRepository.findAll();
        assertThat(directorys).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directorys.get(directorys.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deleteDirectory() throws Exception {
        // Initialize the database
        directoryRepository.save(directory);

		int databaseSizeBeforeDelete = directoryRepository.findAll().size();

        // Get the directory
        restDirectoryMockMvc.perform(delete("/api/directorys/{id}", directory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Directory> directorys = directoryRepository.findAll();
        assertThat(directorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
