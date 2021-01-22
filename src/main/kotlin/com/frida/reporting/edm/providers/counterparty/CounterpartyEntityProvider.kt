package com.frida.reporting.edm.providers.counterparty

import com.frida.reporting.edm.providers.EntityProvider
import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.data.Property
import org.apache.olingo.commons.api.data.ValueType
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind
import org.apache.olingo.commons.api.edm.FullQualifiedName
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType
import org.apache.olingo.commons.api.edm.provider.CsdlProperty
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.ServiceMetadata
import org.springframework.stereotype.Component


@Component
class CounterpartyEntityProvider() : EntityProvider {

    var odata: OData? = null
    var serviceMetadata: ServiceMetadata? = null

    override fun getEntityType(): CsdlEntityType {
        // create EntityType properties
        val id: CsdlProperty = CsdlProperty().setName("ID").setType(
                EdmPrimitiveTypeKind.Int32.fullQualifiedName)
        val name: CsdlProperty = CsdlProperty().setName("FirstName").setType(
                EdmPrimitiveTypeKind.String.fullQualifiedName)
        val description: CsdlProperty = CsdlProperty().setName("LastName").setType(
                EdmPrimitiveTypeKind.String.fullQualifiedName)

        // create CsdlPropertyRef for Key element
        val propertyRef = CsdlPropertyRef()
        propertyRef.name = "ID"

        // configure EntityType
        val entityType = CsdlEntityType()
        entityType.name = ENTITY_NAME
        entityType.properties = listOf(id, name, description)
        entityType.key = listOf(propertyRef)
        return entityType
    }

    override fun getEntitySetName(): String {
        return ENTITY_SET_NAME
    }

    override fun getFullyQualifiedName(): FullQualifiedName {
        return FULLY_QUALIFIED_NAME
    }

    /*
    NOTE: This method should interact with database or other data source.
    For now we just have hardcoded data.
     */
    override fun getEntityCollection(): EntityCollection {
        val counterpartiesCollection = EntityCollection()
        val counterpartyList = counterpartiesCollection.entities

        // add some sample counterparty entities
        val e1 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 1))
                .addProperty(Property(null, "FirstName", ValueType.PRIMITIVE, "Adam"))
                .addProperty(Property(null, "LastName", ValueType.PRIMITIVE,
                        "Peters"))
        e1.id = createId("Counterparties", 1)
        counterpartyList.add(e1)
        val e2 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 2))
                .addProperty(Property(null, "FirstName", ValueType.PRIMITIVE, "Bjorn"))
                .addProperty(Property(null, "LastName", ValueType.PRIMITIVE,
                        "Hendersson"))
        e2.id = createId("Counterparties", 2)
        counterpartyList.add(e2)
        val e3 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 3))
                .addProperty(Property(null, "Name", ValueType.PRIMITIVE, "Charles"))
                .addProperty(Property(null, "Description", ValueType.PRIMITIVE,
                        "Dickens"))
        e3.id = createId("Counterparties", 3)
        counterpartyList.add(e3)

        return counterpartiesCollection
    }

    companion object {

        // Service Namespace
        const val NAMESPACE = "com.example.model"

        // Entity Types Names
        const val ENTITY_NAME = "Counterparty"
        val FULLY_QUALIFIED_NAME = FullQualifiedName(
                NAMESPACE, ENTITY_NAME)

        // Entity Set Names
        const val ENTITY_SET_NAME = "Counterparties"
    }
}