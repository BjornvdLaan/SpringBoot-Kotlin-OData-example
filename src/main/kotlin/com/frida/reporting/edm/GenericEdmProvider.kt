package com.frida.reporting.edm

import com.frida.reporting.edm.providers.EntityProvider
import org.apache.olingo.commons.api.edm.FullQualifiedName
import org.apache.olingo.commons.api.edm.provider.*
import org.apache.olingo.commons.api.ex.ODataException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.*


@Component
class GenericEdmProvider(@Autowired val ctx: ApplicationContext) : CsdlAbstractEdmProvider() {

    @Throws(ODataException::class)
    override fun getEntityType(entityTypeName: FullQualifiedName): CsdlEntityType? {
        val entityProviders: Map<String, EntityProvider> = ctx
                .getBeansOfType(EntityProvider::class.java)

        for (entity in entityProviders.keys) {
            val entityProvider: EntityProvider = entityProviders[entity] ?: throw ODataException("")
            val entityType: CsdlEntityType = entityProvider.getEntityType()
            if (entityType.name == entityTypeName.name) {
                return entityType
            }
        }

        return null
    }

    @Throws(ODataException::class)
    override fun getEntitySet(entityContainer: FullQualifiedName?,
                              entitySetName: String?): CsdlEntitySet? {
        if (entityContainer != CONTAINER) {
            return null
        }

        val entityProviders = ctx
                .getBeansOfType(EntityProvider::class.java)

        for (entity in entityProviders.keys) {
            val entityProvider = entityProviders[entity]
            val entityType: CsdlEntityType = entityProvider!!.getEntityType()
            if (entityProvider.getEntitySetName() == entitySetName) {
                val result = CsdlEntitySet()
                result.name = entityProvider.getEntitySetName()
                result.setType(entityProvider.getFullyQualifiedName())
                return result
            }
        }

        return null
    }

    @Throws(ODataException::class)
    override fun getEntityContainer(): CsdlEntityContainer? {

        // create EntitySets
        val entitySets: MutableList<CsdlEntitySet> = ArrayList()
        val entityProviders = ctx
                .getBeansOfType(EntityProvider::class.java)
        for (entity in entityProviders.keys) {
            val entityProvider = entityProviders[entity]
            val entitySet = getEntitySet(CONTAINER, entityProvider!!.getEntitySetName())!!
            entitySets.add(entitySet)
        }

        // create EntityContainer
        val entityContainer = CsdlEntityContainer()
        entityContainer.name = CONTAINER_NAME
        entityContainer.entitySets = entitySets
        return entityContainer
    }

    @Throws(ODataException::class)
    override fun getSchemas(): List<CsdlSchema>? {
        // get all EntityProviders
        val entityProviders = ctx
                .getBeansOfType(EntityProvider::class.java)

        // create Schema
        val schema = CsdlSchema()
        schema.namespace = NAMESPACE

        // add EntityTypes
        val entityTypes: MutableList<CsdlEntityType> = ArrayList()
        for (entity in entityProviders.keys) {
            val entityProvider = entityProviders[entity]
            entityTypes.add(entityProvider!!.getEntityType())
        }
        schema.entityTypes = entityTypes

        // add EntityContainer
        schema.entityContainer = entityContainer

        // finally
        val schemas: MutableList<CsdlSchema> = ArrayList()
        schemas.add(schema)
        return schemas
    }

    /**
     * This method is invoked when displaying the service document
     * Example: http://localhost:8080/DemoService/DemoService.svc
     */
    @Throws(ODataException::class)
    override fun getEntityContainerInfo(
            entityContainerName: FullQualifiedName?): CsdlEntityContainerInfo? {

        if (entityContainerName == null
                || entityContainerName == CONTAINER) {
            val entityContainerInfo = CsdlEntityContainerInfo()
            entityContainerInfo.containerName = CONTAINER
            return entityContainerInfo
        }
        return null
    }

    companion object {
        const val NAMESPACE = "frida.odata"

        // EDM Container
        const val CONTAINER_NAME = "Container"
        val CONTAINER = FullQualifiedName(NAMESPACE, CONTAINER_NAME)
    }


}


