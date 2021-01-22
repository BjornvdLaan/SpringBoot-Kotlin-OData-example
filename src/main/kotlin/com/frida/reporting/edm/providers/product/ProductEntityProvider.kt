package com.frida.reporting.edm.providers.product

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
class ProductEntityProvider() : EntityProvider {

    var odata: OData? = null
    var serviceMetadata: ServiceMetadata? = null

    override fun getEntityType(): CsdlEntityType {
        // create EntityType properties
        val id: CsdlProperty = CsdlProperty().setName("ID").setType(
                EdmPrimitiveTypeKind.Int32.fullQualifiedName)
        val name: CsdlProperty = CsdlProperty().setName("Name").setType(
                EdmPrimitiveTypeKind.String.fullQualifiedName)
        val description: CsdlProperty = CsdlProperty().setName("Description").setType(
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
        val productsCollection = EntityCollection()
        val productList = productsCollection.entities

        // add some sample product entities
        val e1 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 1))
                .addProperty(Property(null, "Name", ValueType.PRIMITIVE, "Notebook Basic 15"))
                .addProperty(Property(null, "Description", ValueType.PRIMITIVE,
                        "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"))
        e1.id = createId("Products", 1)
        productList.add(e1)
        val e2 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 2))
                .addProperty(Property(null, "Name", ValueType.PRIMITIVE, "1UMTS PDA"))
                .addProperty(Property(null, "Description", ValueType.PRIMITIVE,
                        "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network"))
        e2.id = createId("Products", 2)
        productList.add(e2)
        val e3 = Entity()
                .addProperty(Property(null, "ID", ValueType.PRIMITIVE, 3))
                .addProperty(Property(null, "Name", ValueType.PRIMITIVE, "Ergo Screen"))
                .addProperty(Property(null, "Description", ValueType.PRIMITIVE,
                        "19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"))
        e3.id = createId("Products", 3)
        productList.add(e3)

        return productsCollection
    }

    companion object {

        // Service Namespace
        const val NAMESPACE = "com.example.model"

        // Entity Types Names
        const val ENTITY_NAME = "Product"
        val FULLY_QUALIFIED_NAME = FullQualifiedName(
                NAMESPACE, ENTITY_NAME)

        // Entity Set Names
        const val ENTITY_SET_NAME = "Products"
    }
}