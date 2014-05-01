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
package com.netflix.nicobar.core.archive;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.modules.filter.PathFilter;
import org.jboss.modules.filter.PathFilters;

/**
 * Common configuration elements for converting a {@link ScriptArchive} to a module.
 * @author James Kojo
 * @author Vasanth Asokan
 * @author Aaron Tull
 */
public class ScriptModuleSpec {
    /**
     * Used to Construct a {@link ScriptModuleSpec}.
     */
    public static class Builder {
        private final ModuleId moduleId;
        private final Set<String> compilerPluginIds = new LinkedHashSet<String>();
        private final Map<String, Object> archiveMetadata = new LinkedHashMap<String, Object>();
        private final Set<ModuleId> moduleDependencies = new LinkedHashSet<ModuleId>();
        private final Set<String> moduleImportFilters = new LinkedHashSet<String>();
        private final Set<String> moduleExportFilters = new LinkedHashSet<String>();
        public static final PathFilter defaultPathFilter = PathFilters.acceptAll();

        public Builder(String moduleId) {
            this.moduleId = ModuleId.fromString(moduleId);
        }

        public Builder(ModuleId moduleId) {
            this.moduleId = moduleId;
        }
        /** Add a dependency on the named compiler plugin */
        public Builder addCompilerPluginId(String pluginId) {
            if (pluginId != null) {
                compilerPluginIds.add(pluginId);
            }
            return this;
        }
        /** Add a dependency on the named compiler plugin */
        public Builder addCompilerPluginIds(Set<String> pluginIds) {
            if (pluginIds != null) {
                compilerPluginIds.addAll(pluginIds);
            }
            return this;
        }
        /** Append all of the given metadata. */
        public Builder addMetadata(Map<String, Object> metadata) {
            if (metadata != null) {
                archiveMetadata.putAll(metadata);
            }
            return this;
        }
        /** Append the given metadata. */
        public Builder addMetadata(String property, Object value) {
            if (property != null && value != null) {
                archiveMetadata.put(property, value);
            }
            return this;
        }
        /** Add Module dependency. */
        public Builder addModuleDependency(String dependencyName) {
            if (dependencyName != null) {
                moduleDependencies.add(ModuleId.fromString(dependencyName));
            }
            return this;
        }
        /** Add Module dependency. */
        public Builder addModuleDependency(ModuleId dependency) {
            if (dependency != null) {
                moduleDependencies.add(dependency);
            }
            return this;
        }
        /** Add Module dependencies. */
        public Builder addModuleDependencies(Set<ModuleId> dependencies) {
            if (dependencies != null) {
                for (ModuleId dependency: dependencies) {
                    addModuleDependency(dependency);
                }
            }
            return this;
        }
        /** Add Module import filter paths. */
        public Builder addModuleImportFilters(Set<String> filterPaths) {
            if (filterPaths != null) {
                for (String path: filterPaths) {
                    addModuleImportFilter(path);
                }
            }
            return this;
        }
        /** Add a Module import filter path. */
        public Builder addModuleImportFilter(String filterPath) {
            if (filterPath != null) {
                moduleImportFilters.add(filterPath);
            }
            return this;
        }
        /** Add Module export filter paths. */
        public Builder addModuleExportFilters(Set<String> filterPaths) {
            if (filterPaths != null) {
                for (String path: filterPaths) {
                    addModuleExportFilter(path);
                }
            }
            return this;
        }
        /** Add a Module export filter path. */
        public Builder addModuleExportFilter(String filterPath) {
            if (filterPath != null) {
                moduleExportFilters.add(filterPath);
            }
            return this;
        }
        /** Build the {@link PathScriptArchive}. */
        public ScriptModuleSpec build() {
            return new ScriptModuleSpec(moduleId,
               Collections.unmodifiableMap(new HashMap<String, Object>(archiveMetadata)),
               Collections.unmodifiableSet(new LinkedHashSet<ModuleId>(moduleDependencies)),
               Collections.unmodifiableSet(new LinkedHashSet<String>(compilerPluginIds)),
               Collections.unmodifiableSet(new LinkedHashSet<String>(moduleImportFilters)),
               Collections.unmodifiableSet(new LinkedHashSet<String>(moduleExportFilters)));
        }
    }

    private final ModuleId moduleId;
    private final Map<String, Object> archiveMetadata;
    private final Set<ModuleId> moduleDependencies;
    private final Set<String> compilerPluginIds;
    private final Set<String> importFilters;
    private final Set<String> exportFilters;

    protected ScriptModuleSpec(ModuleId moduleId, Map<String, Object> archiveMetadata, Set<ModuleId> moduleDependencies, Set<String> pluginIds, Set<String> importFilters, Set<String> exportFilters) {
        this.moduleId = Objects.requireNonNull(moduleId, "moduleId");
        this.compilerPluginIds = Objects.requireNonNull(pluginIds, "compilerPluginIds");
        this.archiveMetadata = Objects.requireNonNull(archiveMetadata, "archiveMetadata");
        this.moduleDependencies = Objects.requireNonNull(moduleDependencies, "dependencies");
        this.importFilters = Objects.requireNonNull(importFilters, "import filters");
        this.exportFilters = Objects.requireNonNull(exportFilters, "export filters");
    }

    /**
     * @return id of the archive and the subsequently created module
     */
    public ModuleId getModuleId() {
        return moduleId;
    }

    /**
     * @return Application specific metadata about this archive. This metadata will
     * be transferred to the Module after it's been created
     */
    public Map<String, Object> getMetadata() {
        return archiveMetadata;
    }

    /**
     * @return the names of the modules that this archive depends on
     */
    public Set<ModuleId> getModuleDependencies() {
        return moduleDependencies;
    }

    /**
     * @return the string IDs of the compiler plugins that should process this archive
     */
    public Set<String> getCompilerPluginIds() {
        return compilerPluginIds;
    }

    /**
     * @return the {@link PathFilter} to apply to the paths imported into the module from dependencies
     */
    public Set<String> getModuleImportFilterPaths() {
        return importFilters != null ? importFilters : Collections.<String>emptySet();
    }

    /**
     * @return the {@link PathFilter} to apply to the paths exported to all modules depending on this module
     */
    public Set<String> getModuleExportFilterPaths() {
        return exportFilters != null ? exportFilters : Collections.<String>emptySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ScriptModuleSpec other = (ScriptModuleSpec) o;
        return Objects.equals(this.moduleId, other.moduleId) &&
            Objects.equals(this.archiveMetadata, other.archiveMetadata) &&
            Objects.equals(this.compilerPluginIds, other.compilerPluginIds) &&
            Objects.equals(this.moduleDependencies, other.moduleDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, moduleId, compilerPluginIds, moduleDependencies);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("moduleId", moduleId)
            .append("archiveMetadata", archiveMetadata)
            .append("compilerPlugins", compilerPluginIds)
            .append("dependencies", moduleDependencies)
            .toString();
    }
}

