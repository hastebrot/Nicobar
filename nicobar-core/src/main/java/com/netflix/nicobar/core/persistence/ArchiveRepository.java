/*
 *
 *  Copyright 2013 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.nicobar.core.persistence;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.netflix.nicobar.core.archive.JarScriptArchive;
import com.netflix.nicobar.core.archive.ModuleId;
import com.netflix.nicobar.core.archive.ScriptArchive;

/**
 * Interface to represent a persistence store for archives
 *
 * @author James Kojo
 * @author Vasanth Asokan
 */
public interface ArchiveRepository {
    /**
     * Get the ID of this repository
     * @return the id string.
     */
    public String getRepositoryId();

    /**
     * Get the default view into this repository.
     * @return the default repository view.
     */
    public RepositoryView getDefaultView();

    /**
     * Get a specific named view into this repository.
     *
     * @param view the name of the view.
     * @return a {@link RepositoryView} that matches the given name or null if
     *         one wasn't found.
     * @throws UnsupportedOperationException
     *             if this repository does not support named views.
     */
    public RepositoryView getView(String view);

    /**
     * Insert a Jar into the repository
     * @param jarScriptArchive script archive which describes the jar and
     *        the ModuleSpec which should be inserted
     */
    public void insertArchive(JarScriptArchive jarScriptArchive)
        throws IOException;

    /**
     * Insert a Jar into the repository
     * @param jarScriptArchive script archive which describes the jar and
     *        the ModuleSpec which should be inserted
     * @param initialDeploySpecs a set of initial deployment specs.
     * @throws UnsupportedOperationException if this repository does not support
     *         adding deploy specs to a module.
     */
    public void insertArchive(JarScriptArchive jarScriptArchive, Map <String, Object> initialDeploySpecs)
        throws IOException;

    /**
     * Get all of the {@link ScriptArchive}s for the given set of moduleIds.
     *
     * @param moduleIds keys to search for
     * @return set of ScriptArchives retrieved from the database
     */
    public Set<ScriptArchive> getScriptArchives(Set<ModuleId> moduleIds) throws IOException;

    /**
     * Delete an archive by ID
     * @param moduleId module id to delete
     * @throws IOException
     */
    public void deleteArchive(ModuleId moduleId) throws IOException;
}