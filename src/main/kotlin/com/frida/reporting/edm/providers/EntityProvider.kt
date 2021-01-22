package com.frida.reporting.edm.providers

import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.edm.FullQualifiedName
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType
import org.apache.olingo.commons.api.ex.ODataRuntimeException
import java.net.URI
import java.net.URISyntaxException

interface EntityProvider {
    fun getEntityType(): CsdlEntityType
    fun getEntitySetName(): String
    fun getFullyQualifiedName(): FullQualifiedName
    fun getEntityCollection(): EntityCollection

    fun createId(entitySetName: String, id: Any): URI? {
        return try {
            URI("$entitySetName($id)")
        } catch (e: URISyntaxException) {
            throw ODataRuntimeException("Unable to create id for entity: $entitySetName", e)
        }
    }
}
